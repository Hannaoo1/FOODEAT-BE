package com.lgcns.foodeat.domain.map.dto.response;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "지도용 식사일지 정보")
public class MapDiaryResponse {

    @Schema(description = "일지 ID", example = "1")
    private Long diaryId;

    @Schema(description = "식당명", example = "엽기떡볶이 강남역점")
    private String restaurantName;

    @Schema(description = "카테고리", example = "한식")
    private String category;

    @Schema(description = "메뉴명", example = "로제엽떡")
    private String menuName;

    @Schema(description = "가격", example = "15000")
    private Integer price;

    @Schema(description = "별점 (1~5)", example = "5")
    private Integer rating;

    @Schema(description = "위도 (마커 표시용)", example = "37.4980")
    private Double latitude;

    @Schema(description = "경도 (마커 표시용)", example = "127.0276")
    private Double longitude;

    @Schema(description = "썸네일 이미지 URL", example = "https://foodeat-images.s3.ap-northeast-2.amazonaws.com/diary/image.jpg")
    private String thumbnailUrl;

    public static MapDiaryResponse from(FoodDiary diary, String thumbnailUrl) {
        return MapDiaryResponse.builder()
                .diaryId(diary.getId())
                .restaurantName(diary.getRestaurantName())
                .category(diary.getCategory())
                .menuName(diary.getMenuName())
                .price(diary.getPrice())
                .rating(diary.getRating())
                .latitude(diary.getLatitude())
                .longitude(diary.getLongitude())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
