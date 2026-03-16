package com.lgcns.foodeat.domain.map.controller;

import com.lgcns.foodeat.domain.map.dto.response.MapDiariesResponse;
import com.lgcns.foodeat.domain.map.service.MapService;
import com.lgcns.foodeat.global.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Map", description = "지도 기반 식사일지 조회 API")
@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @Operation(
            summary = "반경 내 식사일지 조회",
            description = """
                    지정한 좌표 기준 반경(km) 내의 내 식사일지를 조회합니다.

                    **사용 시나리오:**
                    1. 프론트에서 "우리집" 선택 시: User의 homeLatitude, homeLongitude 전달
                    2. 프론트에서 "현재 위치" 선택 시: GPS 좌표 전달

                    **응답 설명:**
                    - regionName: 카카오 역지오코딩으로 변환된 동 이름 (예: "역삼동")
                    - diaries: 마커 표시용 좌표 포함된 일지 목록

                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 (반경 범위 초과 등)", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @GetMapping("/diaries")
    public ResponseEntity<MapDiariesResponse> getDiariesInRadius(
            @AuthenticationPrincipal Long userId,

            @Parameter(description = "중심 위도 (예: 강남역 37.4980)", example = "37.4980", required = true)
            @RequestParam Double latitude,

            @Parameter(description = "중심 경도 (예: 강남역 127.0276)", example = "127.0276", required = true)
            @RequestParam Double longitude,

            @Parameter(description = "반경 (km), 1~5 범위, 기본값 3", example = "3")
            @RequestParam(defaultValue = "3") Integer radius) {

        // 반경 유효성 검증
        if (radius < 1 || radius > 5) {
            throw new BusinessException("반경은 1~5km 범위만 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        // 위도 유효성 검증 (대한민국 범위: 33~43)
        if (latitude < 33 || latitude > 43) {
            throw new BusinessException("유효하지 않은 위도입니다.", HttpStatus.BAD_REQUEST);
        }

        // 경도 유효성 검증 (대한민국 범위: 124~132)
        if (longitude < 124 || longitude > 132) {
            throw new BusinessException("유효하지 않은 경도입니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(mapService.getDiariesInRadius(userId, latitude, longitude, radius));
    }
}
