package com.project.interviewmanagement_service.feedback.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Maps FeedBack entity to FeedBackResponse DTO.
 * Flattens nested relationships into simple ID fields for API response.
 */
@Mapper(config = CentralMapperConfig.class )
public interface FeedBackMapper {

    /**
     * Converts FeedBack entity to FeedBackResponse.
     * - Maps interview.id → interviewId
     * - Maps interviewer.id → interviewerId
     */
    @Mapping(source = "interview.id", target = "interviewId")
    @Mapping(source = "interviewer.id", target = "interviewerId")
    FeedBackResponse toFeedBackResponse (FeedBack feedBack);


}
