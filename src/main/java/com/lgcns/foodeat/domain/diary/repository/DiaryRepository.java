package com.lgcns.foodeat.domain.diary.repository;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<FoodDiary, Long> {

    @Query("SELECT d FROM FoodDiary d WHERE d.user.id = :userId AND d.deletedAt IS NULL ORDER BY d.visitedAt DESC")
    Page<FoodDiary> findByUserIdAndNotDeleted(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT d FROM FoodDiary d WHERE d.id = :id AND d.deletedAt IS NULL")
    FoodDiary findByIdAndNotDeleted(@Param("id") Long id);
}