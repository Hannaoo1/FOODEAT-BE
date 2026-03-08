package com.lgcns.foodeat.domain.diary.dto.response;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import lombok.*;
import java.time.LocalDate;

@Getter
@Builder
public class DiaryListResponse {
    private Long diaryId;
    private String restaurantName;
    private String menuName;
    private String category;
    private Integer rating;
    private LocalDate visitedAt;
    private String thumbnailUrl;  // 첫 번째 이미지

    public static DiaryListResponse from(FoodDiary diary, String thumbnailUrl) {
        return DiaryListResponse.builder()
                .diaryId(diary.getId())
                .restaurantName(diary.getRestaurantName())
                .menuName(diary.getMenuName())
                .category(diary.getCategory())
                .rating(diary.getRating())
                .visitedAt(diary.getVisitedAt())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}