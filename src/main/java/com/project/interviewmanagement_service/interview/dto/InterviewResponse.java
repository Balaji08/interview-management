package com.project.interviewmanagement_service.interview.dto;

import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Interview details response")
public record InterviewResponse(

        Long id,
        LocalDateTime scheduledAt,
        LocalDateTime endAt,
        Long candidateId,
        List<Long> interviewerIds,
        InterviewStatus status

) {}
