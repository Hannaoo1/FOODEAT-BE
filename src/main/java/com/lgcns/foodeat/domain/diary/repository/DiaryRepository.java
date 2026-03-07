package com.lgcns.foodeat.domain.diary.repository;

import com.lgcns.foodeat.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUserIdOrderByVisitDateDesc(Long userId);

    List<Diary> findByUserIdAndVisitDateBetweenOrderByVisitDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId " +
           "AND d.latitude BETWEEN :minLat AND :maxLat " +
           "AND d.longitude BETWEEN :minLng AND :maxLng")
    List<Diary> findByUserIdAndLocationBounds(
            @Param("userId") Long userId,
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng);

    int countByUserId(Long userId);
}
