package com.project.interviewmanagement_service.interview.entity;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @ManyToMany()
    @JoinTable(name = "interviewer_table", joinColumns = @JoinColumn(name="interview_id"),
    inverseJoinColumns = @JoinColumn(name="interviewer_id"))
    private List<Interviewer> interviewers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;


    private LocalDateTime scheduledAt;

    private LocalDateTime endAt;
}
