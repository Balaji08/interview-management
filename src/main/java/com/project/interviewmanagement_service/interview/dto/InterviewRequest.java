package com.project.interviewmanagement_service.interview.dto;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewRequest {

    @NotNull(message="Schedule time is required")
    @Future(message = "Interview should be scheduled in future time")
    private LocalDateTime scheduledAt;

    @NotNull(message="Schedule time is required")
    @Future(message = "Interview should be scheduled in future time")
    private LocalDateTime endAt;

    @NotEmpty(message = "Aleast one Interviewer required")
    private List<Long> interviewerIds;

    @NotNull(message = "candidate id required")
    private Long candidateId;

}
