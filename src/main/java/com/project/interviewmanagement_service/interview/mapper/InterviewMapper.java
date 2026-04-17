package com.project.interviewmanagement_service.interview.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.interview.dto.InterviewResponse;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface InterviewMapper {


    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "interviewers", target = "interviewerIds")
    InterviewResponse toInterviewResponse(Interview interview);


    default Long map(Interviewer interviewer) {
        return interviewer != null ? interviewer.getId() : null;
    }
}
