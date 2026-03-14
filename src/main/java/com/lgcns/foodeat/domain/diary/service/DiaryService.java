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

    /**
     * Create a new diary entry for the given user and persist any provided images.
     *
     * @param userId the ID of the user creating the diary
     * @param request the diary creation payload containing diary fields and optional image URLs
     * @return the ID of the created diary
     * @throws BusinessException if the user with the given ID does not exist
     */
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

    public DiaryPageResponse getDiaries(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FoodDiary> diaries = diaryRepository.findByUserIdAndNotDeleted(userId, pageable);

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

    /**
     * Delete a diary along with its stored images and decrement the diary owner's count.
     *
     * Deletes all images associated with the diary from S3, marks the diary as deleted, and decrements the owner's diary count.
     *
     * @param diaryId the ID of the diary to delete
     * @param userId  the ID of the user attempting the deletion (used for authorization)
     * @throws BusinessException if no diary exists with the given ID (results in HTTP 404)
     * @throws BusinessException if the specified user is not the diary owner (results in HTTP 403)
     */
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