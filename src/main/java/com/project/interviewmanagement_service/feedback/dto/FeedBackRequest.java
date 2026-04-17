package com.project.interviewmanagement_service.feedback.dto;

import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class FeedBackRequest {

    @NotNull(message = "Please provide candidate id")
    private Long interviewerId;

    @NotNull(message =  "provide feedback rating")
    private FeedBackRating rating;


    private String comments;


}
