package com.project.interviewmanagement_service.interviewer.repository;

import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer,Long> {

    List<Interviewer> findByExpertiseContainingIgnoreCase(String expertise);
}
