package com.lgcns.foodeat.domain.auth.service;

import com.lgcns.foodeat.domain.auth.dto.request.LoginRequest;
import com.lgcns.foodeat.domain.auth.dto.request.SignupRequest;
import com.lgcns.foodeat.domain.auth.dto.response.LoginResponse;
import com.lgcns.foodeat.domain.user.entity.User;
import com.lgcns.foodeat.domain.user.repository.UserRepository;
import com.lgcns.foodeat.global.exception.*;
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
    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateNicknameException();
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
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = null;

        if (Boolean.TRUE.equals(request.getAutoLogin())) {
            refreshToken = jwtUtil.generateRefreshToken(user.getId());
            redisTemplate.opsForValue().set(
                    REFRESH_TOKEN_PREFIX + user.getId(),
                    refreshToken,
                    14,
                    TimeUnit.DAYS
            );
        }

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .nickname(user.getNickname())
                .foodtiNumber(user.getFoodtiNumber())
                .build();
    }

    public void logout(Long userId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    public LoginResponse reissue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String storedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new InvalidTokenException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtUtil.generateAccessToken(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId,
                newRefreshToken,
                14,
                TimeUnit.DAYS
        );

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
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
