package com.lgcns.foodeat.domain.diary.service;

import com.lgcns.foodeat.domain.diary.dto.request.*;
import com.lgcns.foodeat.domain.diary.dto.response.*;
import com.lgcns.foodeat.domain.diary.entity.*;
import com.lgcns.foodeat.domain.diary.repository.*;
import com.lgcns.foodeat.domain.user.entity.User;
import com.lgcns.foodeat.domain.user.repository.UserRepository;
import com.lgcns.foodeat.global.exception.BusinessException;
import com.lgcns.foodeat.infra.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createDiary(Long userId, DiaryCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND));

        FoodDiary diary = FoodDiary.builder()
                .user(user)
                .restaurantName(request.getRestaurantName())
                .restaurantAddress(request.getRestaurantAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .visitedAt(request.getVisitedAt())
                .category(request.getCategory())
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        FoodDiary savedDiary = diaryRepository.save(diary);

        // 이미지 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                DiaryImage image = DiaryImage.builder()
                        .diary(savedDiary)
                        .imageUrl(request.getImageUrls().get(i))
                        .displayOrder(i)
                        .build();
                diaryImageRepository.save(image);
            }
        }

        user.incrementDiaryCount();
        return savedDiary.getId();
    }

    /**
     * 식사일지 목록 조회 (정렬 + 필터링)
     */
    public DiaryPageResponse getDiaries(Long userId, int page, int size,
                                        String sort, String category,
                                        Integer minPrice, Integer maxPrice, Integer rating) {
        // 정렬 설정 (기본값: 최신순)
        Sort sortOrder = "price".equals(sort)
                ? Sort.by(Sort.Direction.ASC, "price")
                : Sort.by(Sort.Direction.DESC, "visitedAt");

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // 동적 쿼리로 필터링 조회
        Page<FoodDiary> diaries = diaryRepository.findWithFilters(
                userId, category, minPrice, maxPrice, rating, pageable
        );

        Page<DiaryListResponse> diaryPage = diaries.map(diary -> {
            List<DiaryImage> images = diaryImageRepository.findByDiaryIdOrderByDisplayOrder(diary.getId());
            String thumbnailUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
            return DiaryListResponse.from(diary, thumbnailUrl);
        });

        return DiaryPageResponse.from(diaryPage);
    }

    public DiaryResponse getDiary(Long diaryId, Long userId) {
        FoodDiary diary = diaryRepository.findByIdAndNotDeleted(diaryId);

        if (diary == null) {
            throw new BusinessException("일지를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        }

        if (!diary.getUser().getId().equals(userId)) {
            throw new BusinessException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        List<String> imageUrls = diaryImageRepository.findByDiaryIdOrderByDisplayOrder(diaryId)
                .stream()
                .map(DiaryImage::getImageUrl)
                .collect(Collectors.toList());

        return DiaryResponse.from(diary, imageUrls);
    }

    @Transactional
    public void updateDiary(Long diaryId, Long userId, DiaryUpdateRequest request) {
        FoodDiary diary = diaryRepository.findByIdAndNotDeleted(diaryId);

        if (diary == null) {
            throw new BusinessException("일지를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        }

        if (!diary.getUser().getId().equals(userId)) {
            throw new BusinessException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        diary.update(request.getMenuName(), request.getPrice(),
                request.getRating(), request.getComment());
    }

    @Transactional
    public void deleteDiary(Long diaryId, Long userId) {
        FoodDiary diary = diaryRepository.findByIdAndNotDeleted(diaryId);

        if (diary == null) {
            throw new BusinessException("일지를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        }

        if (!diary.getUser().getId().equals(userId)) {
            throw new BusinessException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        // S3에서 이미지 삭제
        List<DiaryImage> images = diaryImageRepository.findByDiaryIdOrderByDisplayOrder(diaryId);
        for (DiaryImage image : images) {
            s3Service.delete(image.getImageUrl());
        }

        diary.delete();
        diary.getUser().decrementDiaryCount();
    }
}