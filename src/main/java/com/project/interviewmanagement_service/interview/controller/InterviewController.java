package com.project.interviewmanagement_service.interview.controller;

import com.project.interviewmanagement_service.common.dto.ApiResponse;
import com.project.interviewmanagement_service.interview.dto.*;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.service.InterviewService;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interviews")
@Tag(name = "Interview-service", description = "Interview management APIs")
public class InterviewController {

    private final InterviewService interviewService;

    /**
     * Schedules a new interview.
     *
     * @param interviewRequest request payload with candidate, interviewers, and schedule
     * @return created interview response
     */
    @Operation(summary = "Schedule new Interview")
    @PostMapping
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(@Valid @RequestBody InterviewRequest interviewRequest)
    {
      InterviewResponse response = interviewService.createInterview(interviewRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success( "Interview scheduled successfully", response));

    }



    /**
     * Fetches interviews using optional filters and pagination.
     */
    @Operation(summary = "Search & Filter Interviews")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InterviewResponse>>> getAllInterview(@RequestParam(required = false) String candidateName,
                                                                           @RequestParam(required = false) String interviewerName,
                                                                           @RequestParam(required = false) LocalDateTime fromDate,
                                                                           @RequestParam(required = false) LocalDateTime toDate,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size)

    {
        PageResponse<InterviewResponse> interviewList = interviewService.searchInterviews(candidateName, interviewerName, page,size, fromDate , toDate);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Interviews fetched successfully",interviewList));

    }


    /**
     * Updates interview status (e.g., SCHEDULED → COMPLETED/CANCELLED).
     */
    @Operation(summary = "Update interview status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InterviewResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody InterviewUpdateStatusRequest statusRequest)
    {
        InterviewResponse interviewResponse = interviewService.updateStatus(id, statusRequest.status());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Interview status updated successfully",interviewResponse));
    }


    /**
     * Reschedules an existing interview if it is in SCHEDULED state.
     */
    @Operation(summary = "Reschedules an existing interview")
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<ApiResponse<InterviewResponse>> rescheduleInterview(@PathVariable Long id, @Valid @RequestBody RescheduleRequest rescheduleRequest)
    {
        InterviewResponse interviewResponse = interviewService.rescheduleInterview(id, rescheduleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Interview rescheduled successfully",interviewResponse));
    }

}
