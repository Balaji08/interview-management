package com.project.interviewmanagement_service.interviewer.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerRequest;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.mapstruct.Mapper;

/**
 * Maps between Interviewer entity and DTOs.
 */
@Mapper(config = CentralMapperConfig.class)
public interface InterviewerMapper {

    /** Entity → Response DTO */
    InterviewerResponse toInterviewerResponse(Interviewer interviewer);

    /** Request DTO → Entity */
    Interviewer toInterviewerEntity(InterviewerRequest request);
}
