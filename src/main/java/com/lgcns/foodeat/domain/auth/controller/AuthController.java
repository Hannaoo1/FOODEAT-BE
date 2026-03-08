package com.lgcns.foodeat.domain.auth.controller;

import com.lgcns.foodeat.domain.auth.dto.request.LoginRequest;
import com.lgcns.foodeat.domain.auth.dto.request.ReissueRequest;
import com.lgcns.foodeat.domain.auth.dto.request.SignupRequest;
import com.lgcns.foodeat.domain.auth.dto.response.LoginResponse;
import com.lgcns.foodeat.domain.auth.dto.response.SignupResponse;
import com.lgcns.foodeat.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)", content = @Content),
            @ApiResponse(responseCode = "409", description = "이메일 또는 닉네임 중복", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        Long userId = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.of(userId));
    }

    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치", content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long userId) {
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content)
    })
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@Valid @RequestBody ReissueRequest request) {
        return ResponseEntity.ok(authService.reissue(request.getRefreshToken()));
    }

    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(Map.of("available", authService.checkEmail(email)));
    }

    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(Map.of("available", authService.checkNickname(nickname)));
    }
}
