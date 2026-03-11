package com.lgcns.foodeat.domain.foodti.service;

import com.lgcns.foodeat.domain.foodti.dto.response.RouletteResponse;
import com.lgcns.foodeat.domain.foodti.entity.Menu;
import com.lgcns.foodeat.domain.foodti.repository.MenuRepository;
import com.lgcns.foodeat.domain.user.entity.User;
import com.lgcns.foodeat.domain.user.repository.UserRepository;
import com.lgcns.foodeat.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public RouletteResponse spin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND));

        Integer foodtiNumber = user.getFoodtiNumber();
        List<Menu> randomMenus;

        if (foodtiNumber != null) {
            // Case 1: 푸디티 번호 있음 → 해당 타입 16개 중 8개 랜덤
            randomMenus = menuRepository.findRandomByFoodtiNumber(foodtiNumber, 8);
        } else {
            // Case 2: 푸디티 번호 없음 → 전체 256개 중 8개 랜덤
            randomMenus = menuRepository.findRandomMenus(8);
        }

        List<RouletteResponse.MenuDto> menuDtos = randomMenus.stream()
                .map(menu -> RouletteResponse.MenuDto.builder()
                        .menuId(menu.getId())
                        .name(menu.getName())
                        .category(menu.getCategory())
                        .build())
                .collect(Collectors.toList());

        return RouletteResponse.builder()
                .foodtiNumber(foodtiNumber)
                .menus(menuDtos)
                .build();
    }
}