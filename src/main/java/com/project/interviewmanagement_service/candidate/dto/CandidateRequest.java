package com.project.interviewmanagement_service.candidate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CandidateRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Please provide valid email")
        @NotBlank(message = "Email is required")
        String email

) {}