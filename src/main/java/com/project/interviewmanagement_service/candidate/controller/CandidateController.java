package com.project.interviewmanagement_service.candidate.controller;

import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.candidate.service.CandidateService;
import com.project.interviewmanagement_service.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponse>> createCandidate(@RequestBody @Valid Candidate candidate)
    {
        CandidateResponse response = candidateService.createCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<CandidateResponse>(true, "Candidate created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateResponse>> getCandidateById(@PathVariable Long id)
    {
        CandidateResponse response = candidateService.getCandidateById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<CandidateResponse>(true, "Candidate fetched successfully", response));
    }
}
