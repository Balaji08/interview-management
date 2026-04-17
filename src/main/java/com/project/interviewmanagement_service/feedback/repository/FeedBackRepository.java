package com.project.interviewmanagement_service.feedback.repository;

import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
}
