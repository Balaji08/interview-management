package com.project.interviewmanagement_service.candidate.repository;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {

}
