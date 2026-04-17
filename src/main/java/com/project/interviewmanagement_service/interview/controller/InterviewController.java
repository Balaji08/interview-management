package com.project.interviewmanagement_service.interview.controller;

import com.project.interviewmanagement_service.interview.dto.*;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;


    @PostMapping
    public ResponseEntity<Interview> scheduleInterview(@Valid @RequestBody InterviewRequest interviewRequest)
    {
      Interview result = interviewService.createInterview(interviewRequest);

      return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }



    @GetMapping
    public ResponseEntity<PageResponse<InterviewResponse>> getAllInterview(@RequestParam(required = false) String candidateName,
                                                                           @RequestParam(required = false) String interviewerName,
                                                                           @RequestParam(required = false)
                                                                               LocalDateTime fromDate,
                                                                           @RequestParam(required = false) LocalDateTime toDate,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size)

    {
        PageResponse<InterviewResponse> result = interviewService.searchInterviews(candidateName, interviewerName, page,size, fromDate , toDate);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<InterviewResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody InterviewUpdateStatusRequest statusRequest)
    {
        InterviewResponse interviewResponse = interviewService.updateStatus(id, statusRequest.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(interviewResponse);
    }


    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<InterviewResponse> rescheduleInterview(@PathVariable Long id, @Valid @RequestBody RescheduleRequest rescheduleRequest)
    {
        InterviewResponse interviewResponse = interviewService.rescheduleInterview(id, rescheduleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(interviewResponse);
    }

}
