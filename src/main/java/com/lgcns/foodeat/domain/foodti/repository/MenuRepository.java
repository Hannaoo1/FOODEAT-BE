package com.lgcns.foodeat.domain.foodti.repository;

import com.lgcns.foodeat.domain.foodti.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query(value = "SELECT * FROM menus WHERE foodti_number = :foodtiNumber ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Menu> findRandomByFoodtiNumber(@Param("foodtiNumber") Integer foodtiNumber, @Param("limit") int limit);

    @Query(value = "SELECT * FROM menus ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Menu> findRandomMenus(@Param("limit") int limit);
}
