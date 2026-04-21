package com.project.interviewmanagement_service.interviewer.dto;

import jakarta.validation.constraints.NotBlank;

public record InterviewerRequest(

        @NotBlank(message = "Name is required")
        String name,

        String expertise

) {}