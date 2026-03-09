package com.lgcns.foodeat.domain.diary.dto.response;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "식사 일지 상세 응답")
public class DiaryResponse {

    @Schema(description = "일지 ID", example = "1")
    private Long diaryId;

    // 식당 정보
    @Schema(description = "식당명", example = "엽기떡볶이 강남역점")
    private String restaurantName;

    @Schema(description = "식당 주소", example = "서울 강남구 강남대로 396")
    private String restaurantAddress;

    @Schema(description = "위도", example = "37.4979")
    private Double latitude;

    @Schema(description = "경도", example = "127.0276")
    private Double longitude;

    // 작성 정보
    @Schema(description = "방문 날짜", example = "2026-03-08")
    private LocalDate visitedAt;

    @Schema(description = "카테고리", example = "한식")
    private String category;

    @Schema(description = "메뉴명", example = "엽기떡볶이")
    private String menuName;

    @Schema(description = "가격", example = "15000")
    private Integer price;

    @Schema(description = "별점 (1~5)", example = "5")
    private Integer rating;

    @Schema(description = "코멘트", example = "맛있어요! 혼밥하기 좋아요")
    private String comment;

    // 이미지
    @Schema(description = "이미지 URL 목록")
    private List<String> imageUrls;

    @Schema(description = "작성 일시", example = "2026-03-08T15:30:00")
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