package com.project.interviewmanagement_service.interviewer.controller;


import com.project.interviewmanagement_service.common.dto.ApiResponse;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.service.InterviewerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewers")
@RequiredArgsConstructor
@Tag(name = "Interviewer-service", description = "Interviewer management APIs")
public class InterviewerController {

    private final InterviewerService interviewerService;

    /**
     * Creates a new interviewer.
     *
     * @param interviewer request payload containing interviewer details
     * @return created interviewer response
     */
    @Operation(summary = "Create new interviewer")
    @PostMapping
    public ResponseEntity<ApiResponse<InterviewerResponse>> createInterviewer(@Valid @RequestBody Interviewer interviewer)
    {
        InterviewerResponse result = interviewerService.createInterviewer(interviewer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Interviewer created successfully", result));

    }


    /**
     * Retrieves all interviewers.
     *
     * @return list of interviewer responses
     */
    @Operation(summary = "Fetch all interviewers")
    @GetMapping
    public  ResponseEntity<ApiResponse<List<InterviewerResponse>>> getAllInterviewer()
    {
        List<InterviewerResponse> result = interviewerService.getAllInterviewer();
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Interviewers fetched successfully", result));
    }

}
