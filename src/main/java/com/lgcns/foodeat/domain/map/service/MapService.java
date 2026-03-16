package com.lgcns.foodeat.domain.map.service;

import com.lgcns.foodeat.domain.diary.entity.DiaryImage;
import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import com.lgcns.foodeat.domain.diary.repository.DiaryImageRepository;
import com.lgcns.foodeat.domain.diary.repository.DiaryRepository;
import com.lgcns.foodeat.domain.map.dto.response.MapDiariesResponse;
import com.lgcns.foodeat.domain.map.dto.response.MapDiaryResponse;
import com.lgcns.foodeat.infra.kakao.KakaoGeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final KakaoGeoService kakaoGeoService;

    public MapDiariesResponse getDiariesInRadius(Long userId, Double latitude, Double longitude, Integer radiusKm) {
        // km -> m 변환
        Double radiusMeters = radiusKm * 1000.0;

        // 반경 내 일지 조회
        List<FoodDiary> diaries = diaryRepository.findDiariesWithinRadius(
                userId, latitude, longitude, radiusMeters
        );

        log.info("반경 {}km 내 식사일지 {}개 조회 - userId={}, lat={}, lng={}",
                radiusKm, diaries.size(), userId, latitude, longitude);

        // 일지별 썸네일 조회 및 DTO 변환
        List<MapDiaryResponse> diaryResponses = diaries.stream()
                .map(diary -> {
                    List<DiaryImage> images = diaryImageRepository.findByDiaryIdOrderByDisplayOrder(diary.getId());
                    String thumbnailUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
                    return MapDiaryResponse.from(diary, thumbnailUrl);
                })
                .toList();

        // 카카오 역지오코딩으로 지역명(동) 조회
        String regionName = kakaoGeoService.getRegionName(latitude, longitude);

        return MapDiariesResponse.of(regionName, radiusKm, diaryResponses);
    }
}
