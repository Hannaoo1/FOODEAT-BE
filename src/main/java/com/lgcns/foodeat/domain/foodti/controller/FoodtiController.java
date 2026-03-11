package com.lgcns.foodeat.domain.foodti.controller;

import com.lgcns.foodeat.domain.foodti.dto.request.FoodtiSubmitRequest;
import com.lgcns.foodeat.domain.foodti.dto.response.FoodtiSubmitResponse;
import com.lgcns.foodeat.domain.foodti.dto.response.MyFoodtiResponse;
import com.lgcns.foodeat.domain.foodti.service.FoodtiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FOODTI", description = "식성테스트 API")
@RestController
@RequestMapping("/api/v1/foodti")
@RequiredArgsConstructor
public class FoodtiController {

    private final FoodtiService foodtiService;

    @Operation(summary = "식성테스트 제출", description = "4개 답변을 받아 FOODTI 번호를 저장하고 추천 메뉴 4개를 반환합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping("/submit")
    public ResponseEntity<FoodtiSubmitResponse> submit(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody FoodtiSubmitRequest request) {
        return ResponseEntity.ok(foodtiService.submit(userId, request));
    }

    @Operation(summary = "내 FOODTI 번호 조회", description = "현재 로그인한 사용자의 FOODTI 번호를 반환합니다 (없으면 null)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<MyFoodtiResponse> getMyFoodtiNumber(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(foodtiService.getMyFoodti(userId));
    }
}
