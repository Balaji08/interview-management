package com.project.interviewmanagement_service.feedback.controller;


import com.project.interviewmanagement_service.feedback.dto.FeedBackRequest;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.service.FeedBackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping("/{interviewId}/feedback")
    public ResponseEntity<FeedBackResponse> createFeedBack(@PathVariable Long interviewId, @Valid @RequestBody FeedBackRequest request)
    {

        FeedBackResponse result = feedBackService.createFeedBack(interviewId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
