package com.lgcns.foodeat.domain.foodti.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "식성테스트 제출 요청")
public class FoodtiSubmitRequest {

    @NotNull(message = "답변은 필수입니다")
    @Size(min = 4, max = 4, message = "답변은 4개여야 합니다")
    @Schema(description = "답변 리스트 (S/P, M/V, R/N, B/D)", example = "[\"S\", \"M\", \"R\", \"B\"]")
    private List<String> answers;
}
