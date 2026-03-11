package com.lgcns.foodeat.domain.foodti.controller;

import com.lgcns.foodeat.domain.foodti.dto.response.RouletteResponse;
import com.lgcns.foodeat.domain.foodti.service.RouletteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Roulette", description = "메뉴 룰렛 API")
@RestController
@RequestMapping("/api/v1/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @Operation(summary = "룰렛 돌리기", description = "FOODTI 번호가 있으면 해당 타입 16개 중 8개, 없으면 전체 256개 중 8개를 랜덤 반환합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "룰렛 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RouletteResponse> spin(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(rouletteService.spin(userId));
    }
}
