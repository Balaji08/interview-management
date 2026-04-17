package com.project.interviewmanagement_service.interview.service;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.common.exception.BaseException;
import com.project.interviewmanagement_service.common.exception.BusinessException;
import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.common.exception.ValidationException;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
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
import com.project.interviewmanagement_service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterviewService {


    private final NotificationService  notificationService;
    private final InterviewerRepository interviewerRepository;
    private  final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private  final InterviewMapper interviewMapper;


    /**
     * Creates a new interview after validating candidate, interviewers,
     * and checking for scheduling conflicts.
     */
    public InterviewResponse createInterview(InterviewRequest request)
    {
        log.info("Creating interview for candidateId={} with interviewers={} from {} to {}",
                request.getCandidateId(),
                request.getInterviewerIds(),
                request.getScheduledAt(),
                request.getEndAt());

        // Validate candidate existence
        Candidate candidate = candidateRepository.findById(request.getCandidateId()).
                orElseThrow(()-> new ResourceNotFoundException(ErrorCode.CANDIDATE_NOT_FOUND, request.getCandidateId()));


        // Fetch interviewers and ensure all requested IDs exist
        List<Interviewer> interviewerList = interviewerRepository.findAllById(request.getInterviewerIds());

        if(interviewerList.isEmpty())
        {
            log.error("No interviewers found for ids={}", request.getInterviewerIds());
            throw  new ResourceNotFoundException(ErrorCode.INTERVIEWER_NOT_FOUND,request.getInterviewerIds());

        }

        if(interviewerList.size()!= request.getInterviewerIds().size())
        {
            log.error("Some interviewers not found. Requested={}, Found={}",
                    request.getInterviewerIds(), interviewerList.size());
            throw  new ResourceNotFoundException(ErrorCode.SOME_INTERVIEWER_NOT_FOUND,request.getInterviewerIds());
        }

        // Check for time conflicts (excluding cancelled interviews at DB level)
        List<Interview> conflicts = interviewRepository.findConflictingInterviews(
                request.getInterviewerIds(),
                request.getScheduledAt(),
                request.getEndAt()
        );

        if (!conflicts.isEmpty()) {
            log.warn("Interview time conflict for interviewers={} between {} and {}",
                    request.getInterviewerIds(),
                    request.getScheduledAt(),
                    request.getEndAt());
            throw new BusinessException(ErrorCode.INTERVIEW_TIME_CONFLICT);
        }


        Interview interview = Interview.builder().candidate(candidate)
                .interviewers(interviewerList)
                .scheduledAt(request.getScheduledAt())
                .endAt(request.getEndAt())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview result = interviewRepository.save(interview);

        log.info("Interview scheduled successfully with id={}", result.getId());

        log.debug("Sending notification for interviewId={}", result.getId());
        // TODO: Move notification to async/event after transaction commit
        notificationService.sendNotification(result);
        return  interviewMapper.toInterviewResponse(result);


    }


    /**
     * Searches interviews using dynamic filters with pagination.
     */
    public PageResponse<InterviewResponse> searchInterviews(String candidateName, String interviewerName, int page, int size , LocalDateTime fromDate , LocalDateTime toDate)
    {
        log.info("Searching interviews with candidateName={}, interviewerName={}, page={}, size={}, fromDate={}, toDate={}",
                candidateName, interviewerName, page, size, fromDate, toDate);

        // Validate date range
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            log.warn("Invalid date range: fromDate={} is after toDate={}", fromDate, toDate);
            throw new ValidationException(ErrorCode.INVALID_DATE_RANGE);
        }

        Pageable pageable = PageRequest.of(page, size);


        Page<Interview> pageResponse=  interviewRepository.findAll(
                InterviewSpecification.filter(candidateName, interviewerName, fromDate , toDate),
                pageable);
        log.debug("Found {} interviews (page {} of {})",
                pageResponse.getTotalElements(),
                pageResponse.getNumber(),
                pageResponse.getTotalPages());

        return PageResponse.<InterviewResponse>builder().content(
                pageResponse.stream().map(interviewMapper::toInterviewResponse).toList())
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .last(pageResponse.isLast())
                .build();


    }



    /**
     * Updates interview status with strict transition rules.
     */
    @Transactional
    public InterviewResponse updateStatus (Long id, InterviewStatus newStatus)
    {
        log.info("Updating interview status for id={} to {}", id, newStatus);
        Interview interview = interviewRepository.findById(id).orElseThrow(()->{
            log.error("Interview not found with id={}", id);
            return new ResourceNotFoundException(ErrorCode.INTERVIEWER_NOT_FOUND);

        });


        validateStatusTransition(interview.getStatus(),newStatus);

        interview.setStatus(newStatus);

        log.info("Interview status updated successfully for id={} to {}", id, newStatus);
        return  interviewMapper.toInterviewResponse(interview);

    }


    /**
     * Validates allowed status transitions.
     * Ensures interview lifecycle integrity.
     */
    private void validateStatusTransition(InterviewStatus current, InterviewStatus target) {

        log.debug("Validating status transition from {} to {}", current, target);
        if (current == target) {
            log.warn("Attempted to set same status: {}", current);
            throw new ValidationException(ErrorCode.STATUS_ALREADY_EXIST);
        }

        switch (current) {

            case SCHEDULED:
                if (target != InterviewStatus.COMPLETED &&
                        target != InterviewStatus.CANCELLED) {
                    log.warn("Invalid transition from {} to {}", current, target);
                    throw new ValidationException(ErrorCode.INVALID_STATUS_TRANSITION, InterviewStatus.SCHEDULED, InterviewStatus.SCHEDULED);
                }
                break;

            case COMPLETED:
                log.warn("Attempt to change COMPLETED interview status");
                throw new ValidationException(
                        ErrorCode.CANNOT_CHANGE_COMPLETED
                );
            case CANCELLED:
                log.warn("Attempt to change CANCELLED interview status");
                throw new ValidationException(
                        ErrorCode.CANNOT_CHANGE_CANCELLED);
        }
    }


    /**
     * Reschedules an interview if it is in SCHEDULED state.
     * Performs time validation and conflict checks before updating.
     */

    @Transactional
    public InterviewResponse rescheduleInterview(Long id, RescheduleRequest request)
    {
        log.info("Rescheduling interview id={} to newStart={} newEnd={}",
                id, request.getScheduledAt(), request.getEndAt());


        // Fetch interview
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Interview not found with id={}", id);
                    return new RuntimeException("Interview not found");});


        // Only scheduled interviews can be rescheduled
        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            log.warn("Cannot reschedule interview id={} with status={}",
                    id, interview.getStatus());
            throw new BaseException(ErrorCode.SCHEDULED_CAN_BE_RESCHEDULED) {
            };
        }

        LocalDateTime newStart = request.getScheduledAt();
        LocalDateTime newEnd = request.getEndAt();

        // Validate time range
        if (!newStart.isBefore(newEnd)) {
            log.warn("Invalid reschedule time range for id={} start={} end={}",
                    id, newStart, newEnd);
            throw new ValidationException(ErrorCode.INVALID_DATE_RANGE);
        }

        // Prevent scheduling in the past
        if (newStart.isBefore(LocalDateTime.now())) {
            log.warn("Attempt to reschedule interview id={} to past time={}", id, newStart);
            throw new ValidationException(ErrorCode.CANNOT_RESCHEDULE);
        }

        // Extract interviewer IDs for conflict check
        List<Long> interviewerIds = interview.getInterviewers()
                .stream()
                .map(i -> i.getId())
                .toList();

        // Check for conflicts
        List<Interview> conflicts = interviewRepository.findConflictingInterviews(
                interviewerIds,
                request.getScheduledAt(),
                request.getEndAt()
        );

        if (!conflicts.isEmpty()) {
            log.warn("Reschedule conflict for interview id={} with interviewers={}",
                    id, interviewerIds);
            throw new BusinessException(ErrorCode.INTERVIEW_TIME_CONFLICT);
        }


        // Apply new schedule
        interview.setScheduledAt(newStart);
        interview.setEndAt(newEnd);
        log.info("Interview rescheduled successfully for id={}", id);

        log.debug("Sending notification for rescheduled interview id={}", id);
        // TODO: Implement actual email notification
        notificationService.sendNotification(interview);


        return interviewMapper.toInterviewResponse(interview);



    }

}
