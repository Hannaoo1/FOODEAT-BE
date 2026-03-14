package com.lgcns.foodeat.infra;

import com.lgcns.foodeat.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Uploads an image file to the configured S3 bucket under the specified directory and returns its public URL.
     *
     * @param file the image file to upload
     * @param directory the target directory/key prefix in the bucket (e.g., "diary", "profile")
     * @return the public S3 URL of the uploaded image
     * @throws BusinessException if the upload fails (e.g., I/O error while reading the file)
     */
    public String upload(MultipartFile file, String directory) {
        validateImageFile(file);

        String fileName = generateFileName(file, directory);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return generateFileUrl(fileName);
        } catch (IOException e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            throw new BusinessException("이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an image from the configured S3 bucket using its public URL.
     *
     * @param imageUrl the public S3 URL of the image; the S3 object key will be derived from this URL
     */
    public void delete(String imageUrl) {
        String fileName = extractFileName(imageUrl);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("이미지 삭제 완료: {}", fileName);
        } catch (Exception e) {
            log.error("이미지 삭제 실패: {}", e.getMessage());
        }
    }

    /**
     * Validate that the provided file is a non-empty image and does not exceed 10 MB.
     *
     * @param file the multipart file to validate
     * @throws BusinessException if the file is empty, its content type does not start with "image/", or its size is greater than 10 MB
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("파일이 비어있습니다.", HttpStatus.BAD_REQUEST);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("이미지 파일만 업로드 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        // 10MB 제한
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BusinessException("파일 크기는 10MB 이하여야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Generate a unique S3 object key under the specified directory while preserving the
     * original file's extension if present.
     *
     * @param file the source multipart file used to derive the original filename and extension
     * @param directory the S3 directory (key prefix) where the file will be placed
     * @return the generated object key in the form "directory/{UUID}{extension}" (extension included if present)
     */
    private String generateFileName(MultipartFile file, String directory) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        return directory + "/" + UUID.randomUUID() + extension;
    }

    /**
     * Builds the public S3 URL for the specified object key.
     *
     * @param fileName the S3 object key or path within the bucket (e.g. "directory/uuid.ext")
     * @return the HTTPS URL to access the object in the configured bucket and region
     */
    private String generateFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    /**
     * Extracts the S3 object key (path and filename) from a full S3 URL.
     *
     * @param imageUrl the public S3 URL in the form https://{bucket}.s3.{region}.amazonaws.com/{objectKey}
     * @return the object key within the bucket (e.g. "directory/filename.jpg")
     */
    private String extractFileName(String imageUrl) {
        // URL에서 파일명 추출: https://bucket.s3.region.amazonaws.com/directory/filename.jpg
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        return imageUrl.replace(prefix, "");
    }
}
