package com.project.interviewmanagement_service.interviewer.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewerResponse {
    private Long id;
    private  String name;
    private String expertise;
}
