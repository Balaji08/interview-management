package com.project.interviewmanagement_service.interviewer.service;

import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.mapper.InterviewerMapper;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;
    private final InterviewerMapper interviewerMapper;

    /**
     * Creates a new interviewer and returns the mapped response DTO.
     */
    public InterviewerResponse createInterviewer(Interviewer interviewer)
    {
        // Persist interviewer and convert to response DTO
        return interviewerMapper.toInterviewerResponse(interviewerRepository.save(interviewer));
    }

    /**
     * Retrieves all interviewers and maps them to response DTOs.
     */
    public List<InterviewerResponse> getAllInterviewer()
    {
        // Fetch all interviewers and map each entity to response DTO
        return interviewerRepository.findAll().stream().map(interviewerMapper::toInterviewerResponse).toList();
    }

}
