package com.project.interviewmanagement_service.feedback.dto;

import lombok.Data;

@Data
public class FeedBackResponse {

    private Long id;
    private Long interviewId;
    private Long interviewerId;
    private String rating;
    private String comments;
}
