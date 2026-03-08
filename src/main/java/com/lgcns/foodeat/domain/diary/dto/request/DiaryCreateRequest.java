package com.lgcns.foodeat.domain.diary.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryCreateRequest {

    // 식당 정보 (카카오 API에서 받아옴)
    @NotBlank
    @Size(max = 100)
    private String restaurantName;

    private String restaurantAddress;  // 주소
    private Double latitude;            // 위도
    private Double longitude;           // 경도

    // 작성 정보
    @NotNull
    private LocalDate visitedAt;  // 방문 날짜

    @NotBlank
    @Pattern(regexp = "한식|양식|중식|일식")
    private String category;

    @NotBlank
    @Size(max = 100)
    private String menuName;

    private Integer price;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;  // 별점 1-5

    @Size(max = 300)
    private String comment;  // 코멘트 (최대 300자)
}