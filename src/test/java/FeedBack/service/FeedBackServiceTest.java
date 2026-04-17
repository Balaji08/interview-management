package FeedBack.service;

import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.feedback.dto.FeedBackRequest;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.entity.FeedBack;
import com.project.interviewmanagement_service.feedback.entity.FeedBackRating;
import com.project.interviewmanagement_service.feedback.mapper.FeedBackMapper;
import com.project.interviewmanagement_service.feedback.repository.FeedBackRepository;
import com.project.interviewmanagement_service.feedback.service.FeedBackService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
     * Should create feedback successfully when:
     * - Interview exists
     * - Interviewer exists
     * - Interviewer is assigned to the interview
     */
    @Test
    void shouldCreateFeedbackSuccessfully() {

        Long interviewId = 1L;

        Interviewer interviewer = Interviewer.builder().id(2L).build();

        Interview interview = Interview.builder()
                .id(interviewId)
                .interviewers(List.of(interviewer))
                .build();

        FeedBackRequest request = new FeedBackRequest(
                2L,
                FeedBackRating.HIRE,
                "Good"
        );

        FeedBack saved = FeedBack.builder().id(10L).build();

        FeedBackResponse response = new FeedBackResponse(
                10L,
                interviewId,
                2L,
                "HIRE",
                "Good"
        );

        when(interviewRepository.findById(interviewId))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.of(interviewer));

        when(feedBackRepository.save(any(FeedBack.class)))
                .thenReturn(saved);

        when(mapper.toFeedBackResponse(saved))
                .thenReturn(response);

        FeedBackResponse result =
                feedBackService.createFeedBack(interviewId, request);

        assertNotNull(result);
        verify(feedBackRepository).save(any());
        verify(mapper).toFeedBackResponse(saved);
    }

    /**
     * Should throw ResourceNotFoundException when:
     * - Interview does not exist
     */
    @Test
    void shouldThrowWhenInterviewNotFound() {

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        FeedBackRequest request = new FeedBackRequest(
                2L,
                FeedBackRating.HIRE,
                null
        );

        assertThrows(ResourceNotFoundException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }

    /**
     * Should throw ResourceNotFoundException when:
     * - Interview exists
     * - Interviewer does NOT exist
     */
    @Test
    void shouldThrowWhenInterviewerNotFound() {

        Interview interview = Interview.builder().id(1L).build();

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.empty());

        FeedBackRequest request = new FeedBackRequest(
                2L,
                FeedBackRating.HIRE,
                null
        );

        assertThrows(ResourceNotFoundException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }

    /**
     * Should throw RuntimeException when:
     * - Interview exists
     * - Interviewer exists
     * - BUT interviewer is NOT assigned to the interview
     */
    @Test
    void shouldThrowWhenInterviewerNotAssigned() {

        Interviewer interviewer = Interviewer.builder().id(2L).build();

        Interview interview = Interview.builder()
                .id(1L)
                .interviewers(List.of()) // Not assigned
                .build();

        when(interviewRepository.findById(1L))
                .thenReturn(Optional.of(interview));

        when(interviewerRepository.findById(2L))
                .thenReturn(Optional.of(interviewer));

        FeedBackRequest request = new FeedBackRequest(
                2L,
                FeedBackRating.HIRE,
                null
        );

        assertThrows(RuntimeException.class,
                () -> feedBackService.createFeedBack(1L, request));
    }
}