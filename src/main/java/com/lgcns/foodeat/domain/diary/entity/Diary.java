package com.lgcns.foodeat.domain.diary.entity;

import com.lgcns.foodeat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String restaurantAddress;

    private String kakaoPlaceId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDate visitDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory foodCategory;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comment;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DiaryImage> images = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String restaurantName, String restaurantAddress, String kakaoPlaceId,
                       Double latitude, Double longitude, LocalDate visitDate,
                       FoodCategory foodCategory, String menuName, Integer price,
                       Integer rating, String comment) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.kakaoPlaceId = kakaoPlaceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visitDate = visitDate;
        this.foodCategory = foodCategory;
        this.menuName = menuName;
        this.price = price;
        this.rating = rating;
        this.comment = comment;
    }

    public void addImage(DiaryImage image) {
        this.images.add(image);
        image.setDiary(this);
    }

    public void clearImages() {
        this.images.clear();
    }
}
