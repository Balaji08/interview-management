package com.project.interviewmanagement_service.candidate.service;

import com.project.interviewmanagement_service.candidate.dto.CandidateRequest;
import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.mapper.CandidateResponseMapper;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateResponseMapper mapper;

    /**
     * Creates a new candidate by persisting the entity and converting it to response DTO.
     *
     * @param candidate Candidate entity containing input details
     * @return CandidateResponse mapped DTO
     */

    public CandidateResponse createCandidate(CandidateRequest candidate)
    {

        Candidate savedCandidate = candidateRepository.save(mapper.toCandidateEntity(candidate));
        log.debug("Candidate created successfully with ID: {}", savedCandidate.getId());

        // map to response DTO
         return mapper.toCandidateResponse( savedCandidate);
    }

    /**
     * Retrieves a candidate by ID.
     *
     * @param id Candidate ID
     * @return CandidateResponse mapped DTO
     * @throws ResourceNotFoundException if candidate is not found
     */


    public CandidateResponse getCandidateById(Long id)
    {
        // Fetch candidate or throw exception if not present
        Candidate retrivedCandidate = candidateRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(ErrorCode.CANDIDATE_NOT_FOUND, id));
        log.debug("Candidate retrieved successfully: {}", retrivedCandidate.getEmail());


        return mapper.toCandidateResponse( retrivedCandidate);

    }

    /**
     * Deletes a candidate by ID.
     *
     * @param id the unique identifier of the candidate
     * @throws ResourceNotFoundException if candidate does not exist
     */
    public void deleteCandidate(Long id) {

        log.info("Request received to delete candidate with id: {}", id);

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Delete failed. Candidate not found with id: {}", id);
                    return new ResourceNotFoundException(
                            ErrorCode.CANDIDATE_NOT_FOUND, id
                    );
                });

        candidateRepository.delete(candidate);

        log.info("Candidate deleted successfully with id: {}", id);
    }

}
