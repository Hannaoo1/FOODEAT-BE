package com.lgcns.foodeat.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {
    private List<String> imageUrls;

    /**
     * Create an ImageUploadResponse containing the given image URLs.
     *
     * @param imageUrls list of image URLs to include in the response
     * @return a new ImageUploadResponse initialized with the provided image URLs
     */
    public static ImageUploadResponse of(List<String> imageUrls) {
        return new ImageUploadResponse(imageUrls);
    }
}
