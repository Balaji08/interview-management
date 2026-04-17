package com.project.interviewmanagement_service.interview.repository;

import com.project.interviewmanagement_service.interview.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {
    @Query("""
    SELECT i FROM Interview i
    JOIN i.interviewers iv
    WHERE iv.id IN :interviewerIds
    AND i.status IN (
                        com.project.interviewmanagement_service.interview.entity.InterviewStatus.SCHEDULED,
                        com.project.interviewmanagement_service.interview.entity.InterviewStatus.COMPLETED
                    )
    AND i.scheduledAt < :endAt
    AND i.endAt > :startAt
""")
    List<Interview> findConflictingInterviews(
            List<Long> interviewerIds,
            LocalDateTime startAt,
            LocalDateTime endAt
    );
}

