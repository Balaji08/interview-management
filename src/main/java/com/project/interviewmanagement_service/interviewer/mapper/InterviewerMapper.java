package com.project.interviewmanagement_service.interviewer.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface InterviewerMapper {

    InterviewerResponse toInterviewerResponse (Interviewer interviewer);
}
