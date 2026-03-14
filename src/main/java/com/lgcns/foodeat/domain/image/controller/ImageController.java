package com.lgcns.foodeat.domain.image.controller;

import com.lgcns.foodeat.domain.image.dto.response.ImageUploadResponse;
import com.lgcns.foodeat.infra.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Image", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    /**
     * Uploads up to three diary images to S3 and returns the uploaded images' URLs.
     *
     * @param files list of multipart image files to upload (maximum 3)
     * @return an ImageUploadResponse containing the uploaded images' URLs
     * @throws IllegalArgumentException if more than 3 files are provided
     */
    @Operation(summary = "식사 일지 이미지 업로드 (최대 3장)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파일 형식 또는 크기 초과", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping(value = "/diary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadDiaryImages(
            @AuthenticationPrincipal Long userId,
            @RequestParam("files") List<MultipartFile> files) {

        if (files.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드 가능합니다.");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = s3Service.upload(file, "diary");
            imageUrls.add(url);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ImageUploadResponse.of(imageUrls));
    }

    /**
     * Deletes the image located at the given URL from storage.
     *
     * @param imageUrl the URL of the image to delete
     * @return a response with HTTP 204 No Content when the image is successfully deleted
     */
    @Operation(summary = "이미지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteImage(
            @AuthenticationPrincipal Long userId,
            @RequestParam String imageUrl) {
        s3Service.delete(imageUrl);
        return ResponseEntity.noContent().build();
    }
}
