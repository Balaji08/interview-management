package com.project.interviewmanagement_service.feedback.mapper;

import com.project.interviewmanagement_service.common.config.mapper.CentralMapperConfig;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class )

public interface FeedBackMapper {

    @Mapping(source = "interview.id", target = "interviewId")
    @Mapping(source = "interviewer.id", target = "interviewerId")
    FeedBackResponse toFeedBackResponse (FeedBack feedBack);

//    default String map(FeedBackRating rating) {
//        return rating != null ? rating.name().toLowerCase() : null;
  //  }
}
