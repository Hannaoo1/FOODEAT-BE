package com.lgcns.foodeat.domain.foodti.repository;

import com.lgcns.foodeat.domain.foodti.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByFoodtiNumber(Integer foodtiNumber);
}
