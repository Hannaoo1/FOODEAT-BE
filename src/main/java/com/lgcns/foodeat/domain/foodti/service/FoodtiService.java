package com.lgcns.foodeat.domain.foodti.service;

import com.lgcns.foodeat.domain.foodti.dto.request.FoodtiSubmitRequest;
import com.lgcns.foodeat.domain.foodti.dto.response.FoodtiSubmitResponse;
import com.lgcns.foodeat.domain.foodti.dto.response.MyFoodtiResponse;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodtiService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private static final Map<String, Integer> FOODTI_MAP = Map.ofEntries(
            Map.entry("SMRB", 1),
            Map.entry("SMRD", 2),
            Map.entry("SMNB", 3),
            Map.entry("SMND", 4),
            Map.entry("SVRB", 5),
            Map.entry("SVRD", 6),
            Map.entry("SVNB", 7),
            Map.entry("SVND", 8),
            Map.entry("PMRB", 9),
            Map.entry("PMRD", 10),
            Map.entry("PMNB", 11),
            Map.entry("PMND", 12),
            Map.entry("PVRB", 13),
            Map.entry("PVRD", 14),
            Map.entry("PVNB", 15),
            Map.entry("PVND", 16)
    );

    @Transactional
    public FoodtiSubmitResponse submit(Long userId, FoodtiSubmitRequest request) {
        // 답변 리스트 → 코드 문자열 생성 (예: ["S","M","R","B"] → "SMRB")
        String foodtiCode = String.join("", request.getAnswers()).toUpperCase();

        Integer foodtiNumber = FOODTI_MAP.get(foodtiCode);
        if (foodtiNumber == null) {
            throw new BusinessException("유효하지 않은 FOODTI 답변입니다: " + foodtiCode, HttpStatus.BAD_REQUEST);
        }

        // users 테이블에 foodti_number 저장
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND));
        user.updateFoodtiNumber(foodtiNumber);

        // 해당 번호 메뉴 중 4개 랜덤 반환
        List<Menu> randomMenus = menuRepository.findRandomByFoodtiNumber(foodtiNumber, 4);

        List<FoodtiSubmitResponse.MenuDto> menuDtos = randomMenus.stream()
                .map(menu -> FoodtiSubmitResponse.MenuDto.builder()
                        .menuId(menu.getId())
                        .name(menu.getName())
                        .category(menu.getCategory())
                        .build())
                .collect(Collectors.toList());

        return FoodtiSubmitResponse.builder()
                .foodtiNumber(foodtiNumber)
                .foodtiCode(foodtiCode)
                .recommendedMenus(menuDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public MyFoodtiResponse getMyFoodti(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND));
        return MyFoodtiResponse.of(user.getFoodtiNumber());
    }
}