package com.project.interviewmanagement_service.feedback.service;

import com.project.interviewmanagement_service.common.exception.BusinessException;
import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
import com.project.interviewmanagement_service.feedback.dto.FeedBackRequest;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.mapper.FeedBackMapper;
import com.project.interviewmanagement_service.feedback.repository.FeedBackRepository;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.repository.InterviewRepository;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private  final InterviewRepository interviewRepository;
    private final InterviewerRepository interviewerRepository;
    private  final FeedBackMapper mapper;


    /**
     * Creates feedback for a given interview by a specific interviewer.
     * Validates interview existence, interviewer existence, and ensures
     * the interviewer is part of the interview before accepting feedback.
     */
    @Transactional
    public FeedBackResponse createFeedBack(Long interviewId, FeedBackRequest request)
    {
        log.info("Creating feedback for interviewId={} by interviewerId={}",
                interviewId, request.interviewerId());
        // Validate interview existence
        Interview interview = interviewRepository.findById(interviewId).orElseThrow(()-> {
                log.error("Interview not found with id={}", interviewId);
                return new ResourceNotFoundException(ErrorCode.INTERVIEW_NOT_FOUND, interviewId);});


        // Validate interviewer existence
        Interviewer interviewer = interviewerRepository.findById(request.interviewerId()).orElseThrow(()-> {
                log.error("Interviewer not found with id={}", request.interviewerId());
                return new ResourceNotFoundException(ErrorCode.INTERVIEWER_NOT_FOUND,request.interviewerId());});



        // Ensure interviewer is assigned to this interview
        if(!interview.getInterviewers().contains(interviewer))
        {
            log.warn("Interviewer {} is not assigned to interview {}",
                    request.interviewerId(), interviewId);
            throw new BusinessException(ErrorCode.INTERVIEWER_NOT_ASSIGNED);
        }


        // Build and persist feedback
        FeedBack feedBack = FeedBack.builder().interview(interview)
                .interviewer(interviewer)
                .rating(request.rating())
                .comments(request.comments())
                .build();


        FeedBack result = feedBackRepository.save(feedBack);

        log.info("Feedback created successfully with id={} for interviewId={}",
                result.getId(), interviewId);
        return mapper.toFeedBackResponse(result);


    }



}
