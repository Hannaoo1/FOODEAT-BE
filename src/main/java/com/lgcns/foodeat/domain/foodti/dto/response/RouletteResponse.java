package com.lgcns.foodeat.domain.foodti.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "룰렛 응답")
public class RouletteResponse {

    @Schema(description = "FOODTI 번호 (없으면 null)", example = "1")
    private Integer foodtiNumber;

    @Schema(description = "랜덤 메뉴 목록 (8개)")
    private List<MenuDto> menus;

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
