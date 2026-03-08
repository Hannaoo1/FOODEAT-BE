package com.lgcns.foodeat.domain.diary.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryUpdateRequest {

    @NotBlank
    private String menuName;

    private Integer price;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    @Size(max = 300)
    private String comment;
}