package com.project.interviewmanagement_service.candidate.mapper;

import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface CandidateResponseMapper {

    CandidateResponse toCandidateResponse(Candidate candidate);
}
