package com.project.interviewmanagement_service.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Candidate response payload")
public record CandidateResponse(

        @Schema(description = "Unique identifier of the candidate", example = "1")
        Long id,

        @Schema(description = "Full name of the candidate", example = "Balaji V S")
        String name,

        @Schema(description = "Email address of the candidate", example = "balaji@example.com")
        String email

) {}
