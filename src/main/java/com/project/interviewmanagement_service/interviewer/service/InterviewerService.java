package com.project.interviewmanagement_service.interviewer.service;

import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;


    public Interviewer createInterviewer(Interviewer interviewer)
    {
        return interviewerRepository.save(interviewer);
    }

    public List<Interviewer> getAllInterviewer()
    {
        return interviewerRepository.findAll();
    }

}
