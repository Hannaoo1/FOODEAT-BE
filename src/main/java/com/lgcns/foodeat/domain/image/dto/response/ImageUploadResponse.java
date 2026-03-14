package com.lgcns.foodeat.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "이미지 업로드 응답")
@Getter
@AllArgsConstructor
public class ImageUploadResponse {

    @Schema(description = "업로드된 이미지 URL 목록", example = "[\"https://foodeat-images.s3.ap-northeast-2.amazonaws.com/diary/uuid.jpg\"]")
    private List<String> imageUrls;

    public static ImageUploadResponse of(List<String> imageUrls) {
        return new ImageUploadResponse(imageUrls);
    }
}
