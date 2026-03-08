package com.lgcns.foodeat.domain.diary.controller;

import com.lgcns.foodeat.domain.diary.dto.request.DiaryCreateRequest;
import com.lgcns.foodeat.domain.diary.dto.request.DiaryUpdateRequest;
import com.lgcns.foodeat.domain.diary.dto.response.DiaryListResponse;
import com.lgcns.foodeat.domain.diary.dto.response.DiaryResponse;
import com.lgcns.foodeat.domain.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Diary", description = "식사 일지 API")
@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "식사 일지 작성")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDiary(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody DiaryCreateRequest request) {
        Long diaryId = diaryService.createDiary(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("diaryId", diaryId));
    }

    @Operation(summary = "식사 일지 목록 조회")
    @GetMapping
    public ResponseEntity<Page<DiaryListResponse>> getDiaries(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(diaryService.getDiaries(userId, page, size));
    }

    @Operation(summary = "식사 일지 상세 조회")
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(diaryId, userId));
    }

    @Operation(summary = "식사 일지 수정")
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId,
            @Valid @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(diaryId, userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "식사 일지 삭제")
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId, userId);
        return ResponseEntity.noContent().build();
    }
}
