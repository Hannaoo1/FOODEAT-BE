package com.lgcns.foodeat.infra.kakao;

import com.lgcns.foodeat.infra.kakao.dto.KakaoGeoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * 카카오 지도 API 서비스
 * 역지오코딩: 좌표 -> 지역명 변환
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoGeoService {

    private final WebClient webClient;

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private static final String DEFAULT_REGION_NAME = "알 수 없는 지역";

    public String getRegionName(Double latitude, Double longitude) {
        try {
            KakaoGeoResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/geo/coord2regioncode.json")
                            .queryParam("x", longitude)
                            .queryParam("y", latitude)
                            .build())
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .retrieve()
                    .bodyToMono(KakaoGeoResponse.class)
                    .block();

            if (response != null && response.getDocuments() != null && !response.getDocuments().isEmpty()) {
                // 행정동(H)을 우선 반환, 없으면 법정동(B) 반환
                return response.getDocuments().stream()
                        .filter(doc -> "H".equals(doc.getRegionType()))
                        .findFirst()
                        .orElse(response.getDocuments().get(0))
                        .getRegion3depthName();
            }

            log.warn("카카오 역지오코딩 응답이 비어있습니다. lat={}, lng={}", latitude, longitude);
            return DEFAULT_REGION_NAME;

        } catch (WebClientResponseException e) {
            log.error("카카오 API 호출 실패 - HTTP {}: {}", e.getStatusCode(), e.getMessage());
            return DEFAULT_REGION_NAME;
        } catch (Exception e) {
            log.error("카카오 역지오코딩 실패: {}", e.getMessage());
            return DEFAULT_REGION_NAME;
        }
    }
}
