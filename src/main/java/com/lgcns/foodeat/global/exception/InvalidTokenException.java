package com.lgcns.foodeat.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);
    }
}
