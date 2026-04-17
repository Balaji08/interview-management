package com.project.interviewmanagement_service.interviewer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Interviewer details response")
public record InterviewerResponse(

        Long id,
        String name,
        String expertise

) {}
