package com.ssafy.kidslink.application.schedule.repository;

import com.ssafy.kidslink.application.kindergarten.domain.Kindergarten;
import com.ssafy.kidslink.application.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public List<Schedule> findByKindergarten(Kindergarten kindergarten);
    public List<Schedule> findByKindergartenAndScheduleDate(Kindergarten kindergarten, LocalDate scheduleDate);
    @Query("SELECT s.scheduleDate FROM Schedule s WHERE s.kindergarten = :kindergarten AND YEAR(s.scheduleDate) = :year AND MONTH(s.scheduleDate) = :month")
    List<LocalDate> findScheduleDatesByKindergartenAndYearAndMonth(Kindergarten kindergarten, int year, int month);
}
