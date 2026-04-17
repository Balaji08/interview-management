package com.project.interviewmanagement_service.candidate.service;

import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.mapper.CandidateResponseMapper;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateResponseMapper mapper;

    public CandidateResponse createCandidate(Candidate candidate)
    {
      return mapper.toCandidateResponse( candidateRepository.save(candidate));
    }

    public CandidateResponse getCandidateById(Long id)
    {
        return mapper.toCandidateResponse( candidateRepository.findById(id).orElseThrow(()->new RuntimeException("Candidate not found")));
    }

}
