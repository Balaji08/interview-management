package com.project.interviewmanagement_service.interview.service;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.candidate.service.CandidateService;
import com.project.interviewmanagement_service.interview.dto.InterviewRequest;
import com.project.interviewmanagement_service.interview.dto.InterviewResponse;
import com.project.interviewmanagement_service.interview.dto.PageResponse;
import com.project.interviewmanagement_service.interview.dto.RescheduleRequest;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import com.project.interviewmanagement_service.interview.mapper.InterviewMapper;
import com.project.interviewmanagement_service.interview.repository.InterviewRepository;
import com.project.interviewmanagement_service.interview.specification.InterviewSpecification;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import com.project.interviewmanagement_service.interviewer.service.InterviewerService;
import com.project.interviewmanagement_service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InterviewService {


    private final NotificationService  notificationService;
    private final InterviewerRepository interviewerRepository;
    private  final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private  final InterviewMapper interviewMapper;


    public Interview createInterview(InterviewRequest request)
    {
        Candidate candidate = candidateRepository.findById(request.getCandidateId()).
                orElseThrow(()-> new RuntimeException("Candidate Not Found"));


        List<Interviewer> interviewerList = interviewerRepository.findAllById(request.getInterviewerIds());

        if(interviewerList.isEmpty())
        {
            throw  new RuntimeException("Interviewers not Found");

        }
        if(interviewerList.size()!= request.getInterviewerIds().size())
        {
            throw  new RuntimeException("Some Interviewers not Found");
        }


        List<Interview> conflicts = interviewRepository.findConflictingInterviews(
                request.getInterviewerIds(),
                request.getScheduledAt(),
                request.getEndAt()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Interviewer has conflicting interview");
        }


        Interview interview = Interview.builder().candidate(candidate)
                .interviewers(interviewerList)
                .scheduledAt(request.getScheduledAt())
                .endAt(request.getEndAt())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview result = interviewRepository.save(interview);


        notificationService.sendNotification(result);
        return  result;


    }



    public PageResponse<InterviewResponse> searchInterviews(String candidateName, String interviewerName, int page, int size , LocalDateTime fromDate , LocalDateTime toDate)
    {
        // ✅ Validation
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new RuntimeException("fromDate cannot be after toDate");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Interview> pageResponse=  interviewRepository.findAll(
                InterviewSpecification.filter(candidateName, interviewerName, fromDate , toDate),
                pageable);
        return PageResponse.<InterviewResponse>builder().content(
                pageResponse.stream().map(interviewMapper::toInterviewResponse).toList())
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .last(pageResponse.isLast())
                .build();


    }



    @Transactional
    public InterviewResponse updateStatus (Long id, InterviewStatus newStatus)
    {
        Interview interview = interviewRepository.findById(id).orElseThrow(()-> new RuntimeException("Interview Not Found "));

        validateStatusTransition(interview.getStatus(),newStatus);

        interview.setStatus(newStatus);

        return  interviewMapper.toInterviewResponse(interview);

    }


    private void validateStatusTransition(InterviewStatus current, InterviewStatus target) {

        if (current == target) {
            //throw new BadRequestException("Status is already " + current);
            throw new RuntimeException("Status is already " + current);
        }

        switch (current) {

            case SCHEDULED:
                if (target != InterviewStatus.COMPLETED &&
                        target != InterviewStatus.CANCELLED) {
                  //  throw new BadRequestException("Invalid status transition");
                    throw new RuntimeException("Invalid status transition");
                }
                break;

            case COMPLETED:
            case CANCELLED:
                throw new RuntimeException(
                        "Cannot change status once it is " + current);
        }
    }



    public InterviewResponse rescheduleInterview(Long id, RescheduleRequest request)
    {
        // 1️⃣ Fetch interview
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        // 2️⃣ Rule: Only SCHEDULED can be rescheduled
        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new RuntimeException("Only scheduled interviews can be rescheduled");
        }

        LocalDateTime newStart = request.getScheduledAt();
        LocalDateTime newEnd = request.getEndAt();

        // 3️⃣ Validate time range
        if (!newStart.isBefore(newEnd)) {
            throw new RuntimeException("scheduledAt must be before endAt");
        }

        // 4️⃣ Optional: prevent past scheduling
        if (newStart.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot reschedule to past time");
        }

        List<Long> interviewerIds = interview.getInterviewers()
                .stream()
                .map(i -> i.getId())
                .toList();

        List<Interview> conflicts = interviewRepository.findConflictingInterviews(
                interviewerIds,
                request.getScheduledAt(),
                request.getEndAt()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Interviewer has conflicting interview");
        }


        // 6️⃣ Apply update
        interview.setScheduledAt(newStart);
        interview.setEndAt(newEnd);

        notificationService.sendNotification(interview);


        // 7️⃣ Return response
        return interviewMapper.toInterviewResponse(interview);



    }

}
