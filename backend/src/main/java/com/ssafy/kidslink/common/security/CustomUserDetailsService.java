package com.ssafy.kidslink.common.security;

import com.ssafy.kidslink.application.parent.domain.Parent;
import com.ssafy.kidslink.application.parent.repository.ParentRepository;
import com.ssafy.kidslink.application.teacher.domain.Teacher;
import com.ssafy.kidslink.application.teacher.repository.TeacherRepository;
import com.ssafy.kidslink.common.service.UserService;
import com.ssafy.kidslink.common.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("CustomUserDetailsService.loadUserByUsername username - {} ", username);

        User userData = new User();
        userData.setUsername(username);
        if (userService.isExistByUsernameForTeacher(username)) {
            Teacher teacher = teacherRepository.findByTeacherUsername(username);
            if (teacher == null) {
                return null;
            }
            userData.setPassword(teacher.getTeacherPassword());
            userData.setRole("ROLE_TEACHER");
        } else {
            Parent parent = parentRepository.findByParentUsername(username);
            if (parent == null) {
                return null;
            }
            userData.setPassword(parent.getParentPassword());
            userData.setRole("ROLE_PARENT");
        }
        log.debug("CustomUserDetailsService.loadUserByUsername userData - {} ", userData);

        return new CustomUserDetails(userData, Collections.emptyMap());
    }
}
