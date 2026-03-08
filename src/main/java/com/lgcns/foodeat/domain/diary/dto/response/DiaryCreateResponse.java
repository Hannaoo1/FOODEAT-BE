package com.lgcns.foodeat.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "식사 일지 작성 응답")
public class DiaryCreateResponse {

    @Schema(description = "생성된 일지 ID", example = "1")
    private Long diaryId;

    public static DiaryCreateResponse of(Long diaryId) {
        return new DiaryCreateResponse(diaryId);
    }
}
