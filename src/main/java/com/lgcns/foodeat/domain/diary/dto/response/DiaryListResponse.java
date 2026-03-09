package com.lgcns.foodeat.domain.diary.dto.response;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;

@Getter
@Builder
@Schema(description = "식사 일지 목록 항목")
public class DiaryListResponse {

    @Schema(description = "일지 ID", example = "1")
    private Long diaryId;

    @Schema(description = "식당명", example = "엽기떡볶이 강남역점")
    private String restaurantName;

    @Schema(description = "메뉴명", example = "엽기떡볶이")
    private String menuName;

    @Schema(description = "카테고리", example = "한식")
    private String category;

    @Schema(description = "별점 (1~5)", example = "5")
    private Integer rating;

    @Schema(description = "방문 날짜", example = "2026-03-08")
    private LocalDate visitedAt;

    @Schema(description = "썸네일 이미지 URL (없으면 null)", example = "https://s3.amazonaws.com/foodeat/image.jpg")
    private String thumbnailUrl;

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