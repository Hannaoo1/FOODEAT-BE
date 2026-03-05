package com.lgcns.foodeat.global.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }
}
