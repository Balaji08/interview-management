package com.project.interviewmanagement_service.interview.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.interview.dto.InterviewResponse;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Maps Interview entity to InterviewResponse DTO.
 * Converts nested objects (candidate, interviewers) into flat response fields.
 */
@Mapper(config = CentralMapperConfig.class)
public interface InterviewMapper {

    /**
     * Converts Interview entity to InterviewResponse.
     * - Maps candidate.id → candidateId
     * - Maps interviewer list → interviewerIds
     */
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "interviewers", target = "interviewerIds")
    InterviewResponse toInterviewResponse(Interview interview);


    /**
     * Custom mapping method used by MapStruct to convert Interviewer → interviewerId.
     * Applied automatically when mapping List<Interviewer> to List<Long>.
     */
    default Long map(Interviewer interviewer) {
        return interviewer != null ? interviewer.getId() : null;
    }
}
