package com.project.interviewmanagement_service.interview.dto;

import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewResponse {
    private Long id;
    private LocalDateTime scheduledAt;
    private LocalDateTime endAt;
    private Long candidateId;
    private List<Long> interviewerIds;
    private InterviewStatus status;
}
