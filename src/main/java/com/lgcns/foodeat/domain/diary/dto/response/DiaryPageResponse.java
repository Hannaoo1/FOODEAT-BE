package com.lgcns.foodeat.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "식사 일지 목록 응답")
public class DiaryPageResponse {

    @Schema(description = "일지 목록")
    private List<DiaryListResponse> diaries;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    public static DiaryPageResponse from(Page<DiaryListResponse> page) {
        return DiaryPageResponse.builder()
                .diaries(page.getContent())
                .hasNext(!page.isLast())
                .build();
    }
}
