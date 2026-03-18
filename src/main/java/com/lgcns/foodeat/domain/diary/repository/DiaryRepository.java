package com.lgcns.foodeat.domain.diary.repository;

import com.lgcns.foodeat.domain.diary.entity.FoodDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryRepository extends JpaRepository<FoodDiary, Long> {

    @Query("SELECT d FROM FoodDiary d WHERE d.user.id = :userId AND d.deletedAt IS NULL ORDER BY d.visitedAt DESC")
    Page<FoodDiary> findByUserIdAndNotDeleted(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT d FROM FoodDiary d WHERE d.id = :id AND d.deletedAt IS NULL")
    FoodDiary findByIdAndNotDeleted(@Param("id") Long id);

    /**
     반경 내 식사일지 조회
     **/
    @Query(value = """
            SELECT fd.* FROM food_diaries fd
            WHERE fd.user_id = :userId
              AND fd.deleted_at IS NULL
              AND fd.latitude IS NOT NULL
              AND fd.longitude IS NOT NULL
              AND ST_Distance_Sphere(
                  POINT(fd.longitude, fd.latitude),
                  POINT(:longitude, :latitude)
              ) <= :radiusMeters
            ORDER BY ST_Distance_Sphere(
                POINT(fd.longitude, fd.latitude),
                POINT(:longitude, :latitude)
            )
            """, nativeQuery = true)
    List<FoodDiary> findDiariesWithinRadius(
            @Param("userId") Long userId,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusMeters") Double radiusMeters
    );

    /**
     * 정렬 및 필터링 조회 (동적 쿼리)
     * 모든 필터 파라미터는 Optional (NULL이면 조건 무시)
     */
    @Query("""
            SELECT d FROM FoodDiary d
            WHERE d.user.id = :userId
              AND d.deletedAt IS NULL
              AND (:category IS NULL OR d.category = :category)
              AND (:minPrice IS NULL OR d.price >= :minPrice)
              AND (:maxPrice IS NULL OR d.price <= :maxPrice)
              AND (:rating IS NULL OR d.rating = :rating)
            """)
    Page<FoodDiary> findWithFilters(
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("rating") Integer rating,
            Pageable pageable
    );
}