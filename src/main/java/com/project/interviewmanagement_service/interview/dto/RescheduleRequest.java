package com.project.interviewmanagement_service.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Request to reschedule an interview")
public record RescheduleRequest(

        @NotNull(message = "scheduledAt is required")
        LocalDateTime scheduledAt,

        @NotNull(message = "endAt is required")
        LocalDateTime endAt

) {}
