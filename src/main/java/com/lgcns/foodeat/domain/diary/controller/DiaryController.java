package com.lgcns.foodeat.domain.diary.controller;

import com.lgcns.foodeat.domain.diary.dto.request.DiaryCreateRequest;
import com.lgcns.foodeat.domain.diary.dto.request.DiaryUpdateRequest;
import com.lgcns.foodeat.domain.diary.dto.response.DiaryCreateResponse;
import com.lgcns.foodeat.domain.diary.dto.response.DiaryPageResponse;
import com.lgcns.foodeat.domain.diary.dto.response.DiaryResponse;
import com.lgcns.foodeat.domain.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Diary", description = "식사 일지 API")
@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "식사 일지 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "일지 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DiaryCreateResponse> createDiary(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody DiaryCreateRequest request) {
        Long diaryId = diaryService.createDiary(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DiaryCreateResponse.of(diaryId));
    }

    @Operation(summary = "식사 일지 목록 조회 (정렬 + 필터링)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @GetMapping
    public ResponseEntity<DiaryPageResponse> getDiaries(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준: 기본값(작성일순), visitedAt(방문일순), price(가격낮은순)")
            @RequestParam(required = false) String sort,
            @Parameter(description = "카테고리 필터: 한식, 양식, 중식, 일식")
            @RequestParam(required = false) String category,
            @Parameter(description = "최소 가격 필터")
            @RequestParam(required = false) Integer minPrice,
            @Parameter(description = "최대 가격 필터")
            @RequestParam(required = false) Integer maxPrice,
            @Parameter(description = "별점 필터 (1~5)")
            @RequestParam(required = false) Integer rating) {
        return ResponseEntity.ok(diaryService.getDiaries(userId, page, size, sort, category, minPrice, maxPrice, rating));
    }

    @Operation(summary = "식사 일지 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인의 일지가 아님", content = @Content),
            @ApiResponse(responseCode = "404", description = "일지를 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(diaryId, userId));
    }

    @Operation(summary = "식사 일지 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인의 일지가 아님", content = @Content),
            @ApiResponse(responseCode = "404", description = "일지를 찾을 수 없음", content = @Content)
    })
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId,
            @Valid @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(diaryId, userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "식사 일지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인의 일지가 아님", content = @Content),
            @ApiResponse(responseCode = "404", description = "일지를 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId, userId);
        return ResponseEntity.noContent().build();
    }
}
