package com.project.interviewmanagement_service.feedback.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private  final InterviewRepository interviewRepository;
    private final InterviewerRepository interviewerRepository;
    private  final FeedBackMapper mapper;


    @Transactional
    public FeedBackResponse createFeedBack(Long interviewId, FeedBackRequest request)
    {
        Interview interview = interviewRepository.findById(interviewId).orElseThrow(()-> new RuntimeException("Interview not found"));

        Interviewer interviewer = interviewerRepository.findById(request.getInterviewerId()).orElseThrow(()->new RuntimeException("Interviewer not found"));


        if(!interview.getInterviewers().contains(interviewer))
        {
            throw new RuntimeException("Interviewer was not assigned to this interview");
        }


        FeedBack feedBack = FeedBack.builder().interview(interview)
                .interviewer(interviewer)
                .rating(request.getRating())
                .comments(request.getComments())
                .build();

        FeedBack result = feedBackRepository.save(feedBack);

        return mapper.toFeedBackResponse(result);


    }



}
