package com.project.interviewmanagement_service.feedback.service;

import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.feedback.dto.FeedBackRequest;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import com.project.interviewmanagement_service.feedback.mapper.FeedBackMapper;
import com.project.interviewmanagement_service.feedback.repository.FeedBackRepository;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.repository.InterviewRepository;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedBackServiceTest {

    @InjectMocks
    private FeedBackService feedBackService;

    @Mock
    private FeedBackRepository feedBackRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private FeedBackMapper mapper;

    /**
     * Verifies feedback creation when interview and interviewer are valid.
     */
    @Test
    void shouldCreateFeedbackSuccessfully() {

        // GIVEN
        Long interviewId = 1L;

        Interviewer interviewer = Interviewer.builder().id(2L).build();

        Interview interview = Interview.builder()
                .id(interviewId)
                .interviewers(List.of(interviewer))
                .build();

        FeedBackRequest request = new FeedBackRequest();
        request.setInterviewerId(2L);
        request.setComments("Good");
        request.setRating(FeedBackRating.HIRE);

        FeedBack saved = FeedBack.builder().id(10L).build();
        FeedBackResponse response = new FeedBackResponse();

        when(interviewRepository.findById(interviewId))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.of(interviewer));

        when(feedBackRepository.save(any(FeedBack.class)))
                .thenReturn(saved);

        when(mapper.toFeedBackResponse(saved))
                .thenReturn(response);

        // WHEN
        FeedBackResponse result =
                feedBackService.createFeedBack(interviewId, request);

        // THEN
        assertNotNull(result);
        assertEquals(response, result); // stronger assertion
        verify(feedBackRepository).save(any());
        verify(mapper).toFeedBackResponse(saved);
    }

    /**
     * Verifies exception when interview is not found.
     */
    @Test
    void shouldThrowWhenInterviewNotFound() {

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        FeedBackRequest request = new FeedBackRequest();
        request.setInterviewerId(2L);

        assertThrows(ResourceNotFoundException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }

    /**
     * Verifies exception when interviewer is not found.
     */
    @Test
    void shouldThrowWhenInterviewerNotFound() {

        Interview interview = Interview.builder().id(1L).build();

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.empty());

        FeedBackRequest request = new FeedBackRequest();
        request.setInterviewerId(2L);

        assertThrows(ResourceNotFoundException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }

    /**
     * Verifies exception when interviewer is not assigned to the interview.
     */
    @Test
    void shouldThrowWhenInterviewerNotAssigned() {

        Interviewer interviewer = Interviewer.builder().id(2L).build();

        Interview interview = Interview.builder()
                .id(1L)
                .interviewers(List.of()) // interviewer not assigned
                .build();

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.of(interviewer));

        FeedBackRequest request = new FeedBackRequest();
        request.setInterviewerId(2L);

        assertThrows(RuntimeException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }
}