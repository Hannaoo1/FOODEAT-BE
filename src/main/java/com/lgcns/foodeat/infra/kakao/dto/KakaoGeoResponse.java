package com.lgcns.foodeat.infra.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 카카오 역지오코딩 API 응답 DTO
 */

@Getter
@NoArgsConstructor
public class KakaoGeoResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("total_count")
        private Integer totalCount;
    }

    @Getter
    @NoArgsConstructor
    public static class Document {
        /**
         * 지역 타입
         * - B: 법정동
         * - H: 행정동
         */
        @JsonProperty("region_type")
        private String regionType;

        @JsonProperty("region_1depth_name")
        private String region1depthName;

        /**
         * 구/군 단위
         */
        @JsonProperty("region_2depth_name")
        private String region2depthName;

        /**
         * 동 단위
         */
        @JsonProperty("region_3depth_name")
        private String region3depthName;

        @JsonProperty("region_4depth_name")
        private String region4depthName;
    }
}
