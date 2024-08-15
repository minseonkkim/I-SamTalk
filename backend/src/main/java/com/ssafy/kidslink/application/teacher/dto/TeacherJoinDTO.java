package com.ssafy.kidslink.application.teacher.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class TeacherJoinDTO {
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String nickname;
    private String tel;
    private int kindergartenId;
    private int kindergartenClassId;
    private MultipartFile profile;
}
