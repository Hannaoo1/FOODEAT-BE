package com.lgcns.foodeat.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {
    private List<String> imageUrls;

    public static ImageUploadResponse of(List<String> imageUrls) {
        return new ImageUploadResponse(imageUrls);
    }
}
