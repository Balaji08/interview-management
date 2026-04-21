package com.project.interviewmanagement_service.candidate.mapper;

import com.project.interviewmanagement_service.candidate.dto.CandidateRequest;
import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

/**
 * Maps Candidate entity to CandidateResponse DTO.
 */
@Mapper(config = CentralMapperConfig.class)
public interface CandidateResponseMapper {

    /**
     * Converts Candidate entity to response DTO.
     */
    CandidateResponse toCandidateResponse(Candidate candidate);

    /**
     * Converts CandidateRequest to Entity.
     */
    Candidate toCandidateEntity(CandidateRequest candidateRequest);
}
