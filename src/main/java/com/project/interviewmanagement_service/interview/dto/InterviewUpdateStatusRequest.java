package com.project.interviewmanagement_service.interview.dto;

import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update interview status")
public record InterviewUpdateStatusRequest(

        @NotNull(message = "Status is required")
        InterviewStatus status

) {}