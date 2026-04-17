package com.project.interviewmanagement_service.feedback.dto;

import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for submitting interviewer feedback")
public record FeedBackRequest(

        @NotNull(message = "Please provide interviewer id")
        @Schema(description = "Unique identifier of the interviewer", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
        Long interviewerId,

        @NotNull(message = "Provide feedback rating")
        @Schema(description = "Rating given by the interviewer", requiredMode = Schema.RequiredMode.REQUIRED,
                example = "EXCELLENT")
        FeedBackRating rating,

        @Schema(description = "Additional comments about the candidate", example = "Strong problem-solving skills")
        String comments

) {}
