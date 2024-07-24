package com.ssafy.kidslink.application.meetingtime.service;

import com.ssafy.kidslink.application.meetingschedule.domain.MeetingSchedule;
import com.ssafy.kidslink.application.meetingschedule.repository.MeetingScheduleRepository;
import com.ssafy.kidslink.application.meetingtime.domain.MeetingTime;
import com.ssafy.kidslink.application.meetingtime.dto.MeetingTimeDTO;
import com.ssafy.kidslink.application.meetingtime.dto.OpenMeetingTimeDTO;
import com.ssafy.kidslink.application.meetingtime.dto.ReserveMeetingDTO;
import com.ssafy.kidslink.application.meetingtime.mapper.MeetingTimeMapper;
import com.ssafy.kidslink.application.meetingtime.repository.MeetingTimeRepository;
import com.ssafy.kidslink.application.parent.domain.Parent;
import com.ssafy.kidslink.application.parent.repository.ParentRepository;
import com.ssafy.kidslink.application.teacher.domain.Teacher;
import com.ssafy.kidslink.application.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingTimeService {

    private final TeacherRepository teacherRepository;
    private final MeetingTimeRepository meetingTimeRepository;
    private final ParentRepository parentRepository;
    private final MeetingTimeMapper meetingTimeMapper;
    private final MeetingScheduleRepository meetingScheduleRepository;

    public void openMeetingTimes(String teacherUsername, List<OpenMeetingTimeDTO> openMeetingTimeDTOList) {
        Teacher teacher = teacherRepository.findByTeacherUsername(teacherUsername);

        for(OpenMeetingTimeDTO openMeetingTimeDTO : openMeetingTimeDTOList) {
            for(String time : openMeetingTimeDTO.getTimes()) {
                MeetingTime meetingTime =  new MeetingTime();
                meetingTime.setTeacher(teacher);
                meetingTime.setMeetingDate(openMeetingTimeDTO.getDate());
                meetingTime.setMeetingTime(time);
                meetingTimeRepository.save(meetingTime);
            }

        }
    }

    public List<MeetingTimeDTO> getMeetingTimes(String parentUsername) {
        int classId = parentRepository.findByParentUsername(parentUsername).getChildren().stream().findFirst().get()
                .getKindergartenClass().getKindergartenClassId();
        List<MeetingTimeDTO> meetingTimes = new ArrayList<>();
        for(MeetingTime meetingTime : meetingTimeRepository.findByClassId(classId)) {
            meetingTimes.add(meetingTimeMapper.toDTO(meetingTime));
        }

        return meetingTimes;

    }

    public void reserveMeeting(String parentUsername, ReserveMeetingDTO reserveMeetingDTO) {
        Parent parent = parentRepository.findByParentUsername(parentUsername);

        MeetingSchedule meetingSchedule = new MeetingSchedule();
        meetingSchedule.setMeetingScheduleDate(reserveMeetingDTO.getMeetingDate());
        meetingSchedule.setParent(parent);
        meetingSchedule.setMeetingScheduleTime(reserveMeetingDTO.getMeetingTime());
        meetingSchedule.setTeacher(teacherRepository.findByKindergartenClass(
                parent.getChildren().stream().findFirst().get().getKindergartenClass()));
        meetingScheduleRepository.save(meetingSchedule);


    }
}
