# 🍽️ FOODEAT (푸딧)

> 혼밥러를 위한 식사 기록 & 메뉴 추천 앱

<br/>

<!-- 서비스 소개 이미지 -->
<img width="677" height="446" alt="푸딧 서비스 설명" src="https://github.com/user-attachments/assets/10e89508-6945-4810-abfb-45cd14365e36" />


<br/><br/>

## 👥 팀원

| 역할 | 이름 |
|:---:|:---:|
| Frontend | 김민선 |
| Backend | 정한나 |

<br/>

## 📅 개발 기간

2026.02 ~ 진행 중

<br/>

## 🛠️ 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring Security, Spring Data JPA, Spring Data Redis |
| Database | MySQL 8.0 |
| Cache | Redis 7 |
| Authentication | JWT (Access Token + Refresh Token) |
| Cloud | AWS S3 |
| Infra | Docker, Docker Compose |
| Documentation | Swagger (Springdoc OpenAPI) |
| Build | Gradle |

<br/>

## ✨ 주요 기능

### 1. 인증 (Auth)
- 회원가입 - 이메일/닉네임 중복 검사, BCrypt 암호화
- JWT 기반 로그인 - Access Token + Refresh Token 발급
- 자동 로그인 - Refresh Token을 Redis에 저장
- 토큰 재발급 - Refresh Token Rotation 방식
- 로그아웃 - Redis에서 토큰 삭제

### 2. 식사 일지 (Diary)
- 식사 일지 CRUD - 작성 / 조회 / 수정 / 삭제
- 정렬 - 작성일순 / 방문날짜순 / 가격낮은순
- 필터링 - 카테고리(한식/중식/양식/일식), 가격대, 별점

### 3. 이미지 업로드 (Image)
- AWS S3 연동 - 일지당 최대 3장 업로드
- 이미지 삭제

### 4. 식성 테스트 — FOODTI
- 4개 질문 답변 → 16가지 식성 타입 계산 및 저장
- FOODTI 기반 추천 메뉴 4개 랜덤 반환
- 내 FOODTI 조회

### 5. 메뉴 룰렛 (Roulette)
- FOODTI 있으면 해당 타입 16개 중 8개 랜덤 반환
- FOODTI 없으면 전체 256개 중 8개 랜덤 반환

### 6. 지도 (Map)
- 우리집 또는 현재 위치 기준 설정
- 반경 1km ~ 5km 내 내 식사일지 식당 조회
- MySQL ST_Distance_Sphere로 반경 계산

<br/>

## 📱 서비스 화면

<!-- 아래 URL을 GitHub Issues에서 이미지 드래그해서 받은 URL로 교체 -->
<table>
  <tr>
    <td align="center"><b>식사 일지</b></td>
    <td align="center"><b>식사 일지 작성</b></td>
    <td align="center"><b>필터</b></td>
    <td align="center"><b>지도</b></td>
  </tr>
  <tr>
    <td><img width="200" alt="식사 일지" src="https://github.com/user-attachments/assets/be57f0d9-d22b-4c1a-8b92-5be2f130dc06"/></td>
    <td><img width="200" alt="식사 일지 작성" src="https://github.com/user-attachments/assets/18f93282-8291-45fe-a1e4-847afd856b46"/></td>
    <td><img width="200" alt="식사 일지 필터" src="https://github.com/user-attachments/assets/f69a5958-a66b-4b50-af71-b04e1e56aa67"/></td>
    <td><img width="200" alt="지도" src="https://github.com/user-attachments/assets/b9d0e102-cefb-431b-a32c-57d3e1e9e292"/></td>
  </tr>
  <tr>
    <td align="center"><b>FOODTI (1)</b></td>
    <td align="center"><b>FOODTI (2)</b></td>
    <td align="center"><b>FOODTI 결과</b></td>
    <td align="center"><b>룰렛</b></td>
  </tr>
  <tr>
    <td><img width="200" alt="푸디티아이" src="https://github.com/user-attachments/assets/d9632b80-287c-483e-a88f-666cd1b0e72d"/></td>
    <td><img width="200" alt="테스트 진행" src="https://github.com/user-attachments/assets/be1588aa-670b-4d2d-94d3-df79cd4b895d"/></td>
    <td><img width="200" alt="식성테스트 결과" src="https://github.com/user-attachments/assets/f9880987-3312-47cf-a5c9-828be921a8ff"/></td>
    <td><img width="200" alt="룰렛 이미지" src="https://github.com/user-attachments/assets/7db69ba2-dc55-4f11-9687-93bf93b1ae6c"/></td>
  </tr>
</table>

<br/>

## 📁 프로젝트 구조

```

src/main/java/com/lgcns/foodeat
├── config/         
├── domain/
│   ├── auth/        # 인증 
│   ├── diary/       # 식사 일지 CRUD
│   ├── foodti/      # 식성 테스트 & 메뉴 룰렛
│   ├── image/       # 이미지 업로드
│   ├── map/         # 지도 기반 조회
│   └── user/        # 사용자 Entity
├── global/          # 예외 처리
├── infra/           # 외부 서비스 (S3)
└── security/        # JWT 인증 필터

```

<br/>

## 📌 앞으로 해야 할 것

### 필수
- [ ] 소셜 로그인 (카카오 / 구글 / 네이버 OAuth2)
- [ ] 배포 (AWS EC2 + CI/CD 파이프라인)
- [ ] 통합테스트


