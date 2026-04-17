package com.project.interviewmanagement_service.interview.dto;

import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewUpdateStatusRequest {
    @NotNull(message = "Status is required")
    private InterviewStatus status;

}
