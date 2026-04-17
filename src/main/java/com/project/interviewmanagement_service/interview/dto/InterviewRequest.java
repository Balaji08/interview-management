package com.project.interviewmanagement_service.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Interview scheduling request")
public record InterviewRequest(

        @NotNull @Future
        LocalDateTime scheduledAt,

        @NotNull @Future
        LocalDateTime endAt,

        @NotEmpty
        List<Long> interviewerIds,

        @NotNull
        Long candidateId

) {}