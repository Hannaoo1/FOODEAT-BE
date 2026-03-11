package com.lgcns.foodeat.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    private String homeAddress;      // 집 주소
    private Double homeLatitude;     // 집 위도
    private Double homeLongitude;    // 집 경도
    private Integer foodtiNumber;

    @Builder.Default
    private Integer diaryCount = 0;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void updateFoodtiNumber(Integer foodtiNumber) {
        this.foodtiNumber = foodtiNumber;
    }

    public void incrementDiaryCount() {
        this.diaryCount++;
    }

    public void decrementDiaryCount() {
        if (this.diaryCount > 0) {
            this.diaryCount--;
        }
    }
}