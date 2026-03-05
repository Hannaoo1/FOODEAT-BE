package com.lgcns.foodeat.domain.auth.controller;

import com.lgcns.foodeat.domain.auth.dto.request.LoginRequest;
import com.lgcns.foodeat.domain.auth.dto.request.ReissueRequest;
import com.lgcns.foodeat.domain.auth.dto.request.SignupRequest;
import com.lgcns.foodeat.domain.auth.dto.response.LoginResponse;
import com.lgcns.foodeat.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignupRequest request) {
        Long userId = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("userId", userId));
    }

    @Operation(summary = "로그인")
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
