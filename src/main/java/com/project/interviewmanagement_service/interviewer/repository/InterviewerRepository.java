package com.project.interviewmanagement_service.interviewer.repository;

import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer,Long> {


}
