package com.lgcns.foodeat.domain.diary.entity;

import com.lgcns.foodeat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_diaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 식당 정보
    @Column(nullable = false, length = 100)
    private String restaurantName;  // 업기떡볶이 건대입구점

    private String restaurantAddress;  // 서울시 광진구...

    private Double latitude;

    private Double longitude;

    // 작성 정보
    @Column(nullable = false)
    private LocalDate visitedAt;  // 방문 날짜

    @Column(nullable = false, length = 10)
    private String category;  // 한식/양식/중식/일식

    @Column(nullable = false, length = 100)
    private String menuName;  // 메뉴명

    private Integer price;  // 가격

    @Column(nullable = false)
    private Integer rating;  // 별점 1-5

    @Column(length = 300)
    private String comment;  // 코멘트

    // 시스템
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void update(String menuName, Integer price, Integer rating, String comment) {
        this.menuName = menuName;
        this.price = price;
        this.rating = rating;
        this.comment = comment;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}