package com.lgcns.foodeat.domain.foodti.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "내 FOODTI 조회 응답")
public class MyFoodtiResponse {

    @Schema(description = "FOODTI 번호 (없으면 null)", example = "1")
    private Integer foodtiNumber;

    public static MyFoodtiResponse of(Integer foodtiNumber) {
        return MyFoodtiResponse.builder()
                .foodtiNumber(foodtiNumber)
                .build();
    }
}
