package com.project.interviewmanagement_service.interview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleRequest {
    @NotNull(message = "scheduledAt is required")
    private LocalDateTime scheduledAt;

    @NotNull(message = "endAt is required")
    private LocalDateTime endAt;
}
