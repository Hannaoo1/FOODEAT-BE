package com.lgcns.foodeat.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

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

    @Schema(description = "업로드된 이미지 URL 목록 (최대 3장)", example = "[\"https://s3.../image1.jpg\"]")
    @Size(max = 3, message = "이미지는 최대 3장까지 업로드 가능합니다")
    private List<@NotBlank(message = "이미지 URL은 비어있을 수 없습니다") String> imageUrls;
}