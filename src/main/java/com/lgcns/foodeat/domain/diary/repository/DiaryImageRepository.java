package com.lgcns.foodeat.domain.diary.repository;

import com.lgcns.foodeat.domain.diary.entity.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
    List<DiaryImage> findByDiaryIdOrderByDisplayOrder(Long diaryId);
}
