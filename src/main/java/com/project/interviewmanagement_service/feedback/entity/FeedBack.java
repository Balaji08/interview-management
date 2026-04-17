package com.project.interviewmanagement_service.feedback.entity;

import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import jakarta.persistence.*;
import lombok.*;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@Entity
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comments;

    @Enumerated(EnumType.STRING)
    private FeedBackRating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    private Interviewer interviewer;
}
