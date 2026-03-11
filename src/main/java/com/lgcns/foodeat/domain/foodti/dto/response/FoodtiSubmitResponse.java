package com.lgcns.foodeat.domain.foodti.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "식성테스트 제출 응답")
public class FoodtiSubmitResponse {

    @Schema(description = "FOODTI 번호 (1~16)", example = "1")
    private Integer foodtiNumber;

    @Schema(description = "FOODTI 코드", example = "SMRB")
    private String foodtiCode;

    @Schema(description = "추천 메뉴 목록 (4개)")
    private List<MenuDto> recommendedMenus;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "메뉴 정보")
    public static class MenuDto {
        @Schema(description = "메뉴 ID", example = "1")
        private Long menuId;

        @Schema(description = "메뉴명", example = "김치찌개")
        private String name;

        @Schema(description = "카테고리", example = "한식")
        private String category;
    }
}
