package com.lgcns.foodeat.domain.diary.dto.response;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DiaryResponse {
    private Long diaryId;

    // 식당 정보
    private String restaurantName;
    private String restaurantAddress;
    private Double latitude;
    private Double longitude;

    // 작성 정보
    private LocalDate visitedAt;
    private String category;
    private String menuName;
    private Integer price;
    private Integer rating;
    private String comment;

    // 이미지
    private List<String> imageUrls;

    private LocalDateTime createdAt;

    public static DiaryResponse from(FoodDiary diary, List<String> imageUrls) {
        return DiaryResponse.builder()
                .diaryId(diary.getId())
                .restaurantName(diary.getRestaurantName())
                .restaurantAddress(diary.getRestaurantAddress())
                .latitude(diary.getLatitude())
                .longitude(diary.getLongitude())
                .visitedAt(diary.getVisitedAt())
                .category(diary.getCategory())
                .menuName(diary.getMenuName())
                .price(diary.getPrice())
                .rating(diary.getRating())
                .comment(diary.getComment())
                .imageUrls(imageUrls)
                .createdAt(diary.getCreatedAt())
                .build();
    }
}