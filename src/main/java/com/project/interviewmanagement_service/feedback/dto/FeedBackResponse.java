package com.project.interviewmanagement_service.feedback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing feedback details")
public record FeedBackResponse(

        @Schema(description = "Unique identifier of the feedback", example = "1")
        Long id,

        @Schema(description = "Associated interview ID", example = "2001")
        Long interviewId,

        @Schema(description = "Interviewer ID who provided the feedback", example = "101")
        Long interviewerId,

        @Schema(description = "Rating given by the interviewer", example = "EXCELLENT")
        String rating,

        @Schema(description = "Additional comments about the candidate", example = "Good communication and problem-solving skills")
        String comments

) {}
