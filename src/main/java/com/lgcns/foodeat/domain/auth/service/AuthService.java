package com.lgcns.foodeat.domain.auth.service;

import com.lgcns.foodeat.domain.auth.dto.request.LoginRequest;
import com.lgcns.foodeat.domain.auth.dto.request.SignupRequest;
import com.lgcns.foodeat.domain.auth.dto.response.LoginResponse;
import com.lgcns.foodeat.domain.user.entity.User;
import com.lgcns.foodeat.domain.user.repository.UserRepository;
import com.lgcns.foodeat.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .homeLatitude(request.getHomeLatitude())
                .homeLongitude(request.getHomeLongitude())
                .build();

        return userRepository.save(user).getId();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = null;

        if (Boolean.TRUE.equals(request.getAutoLogin())) {
            refreshToken = jwtUtil.generateRefreshToken(user.getId());
            redisTemplate.opsForValue().set(refreshToken, String.valueOf(user.getId()), 14, TimeUnit.DAYS);
        }

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .nickname(user.getNickname())
                .foodtiNumber(user.getFoodtiNumber())
                .build();
    }

    public boolean checkEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean checkNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
