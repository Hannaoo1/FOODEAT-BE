package com.lgcns.foodeat.domain.map.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
@Schema(description = "지도 기반 식사일지 조회 응답")
public class MapDiariesResponse {

    @Schema(description = "검색 반경 (km)", example = "3")
    private Integer radius;

    @Schema(description = "식사일지 목록")
    private List<MapDiaryResponse> diaries;

    public static MapDiariesResponse of(Integer radius, List<MapDiaryResponse> diaries) {
        return MapDiariesResponse.builder()
                .radius(radius)
                .diaries(diaries)
                .build();
    }
}
