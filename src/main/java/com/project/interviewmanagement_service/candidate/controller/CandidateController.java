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

    /**
     * Creates a new candidate.
     *
     * @param candidate request payload containing candidate details
     * @return created candidate response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponse>> createCandidate(@RequestBody @Valid Candidate candidate)
    {
        CandidateResponse response = candidateService.createCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Candidate created successfully", response));
    }

    /**
     * Fetches a candidate by ID.
     *
     * @param id candidate identifier
     * @return candidate response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateResponse>> getCandidateById(@PathVariable Long id)
    {
        CandidateResponse response = candidateService.getCandidateById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success( "Candidate fetched successfully", response));
    }
}
