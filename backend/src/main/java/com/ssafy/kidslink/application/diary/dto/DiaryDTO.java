package com.ssafy.kidslink.application.diary.dto;

import com.ssafy.kidslink.application.image.dto.ImageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
public class DiaryDTO {
    private int diaryId;
    private String teacherName;
    private LocalDate createDate;
    private String thumbnail;
    private String content;
    private Set<ImageDTO> images;
}
