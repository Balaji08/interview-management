package interview.service;

import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.common.exception.BusinessException;
import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import com.project.interviewmanagement_service.common.exception.ValidationException;
import com.project.interviewmanagement_service.interview.dto.InterviewRequest;
import com.project.interviewmanagement_service.interview.dto.InterviewResponse;
import com.project.interviewmanagement_service.interview.dto.PageResponse;
import com.project.interviewmanagement_service.interview.entity.Interview;
import com.project.interviewmanagement_service.interview.entity.InterviewStatus;
import com.project.interviewmanagement_service.interview.mapper.InterviewMapper;
import com.project.interviewmanagement_service.interview.repository.InterviewRepository;
import com.project.interviewmanagement_service.interview.service.InterviewService;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import com.project.interviewmanagement_service.notification.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {

    @InjectMocks
    private InterviewService interviewService;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private InterviewMapper interviewMapper;

    private final LocalDateTime START = LocalDateTime.of(2026, 4, 20, 10, 0);
    private final LocalDateTime END = START.plusHours(1);

    /**
     * Should create interview successfully when:
     * - Candidate exists
     * - Interviewers exist
     * - No time conflicts
     */
    @Test
    void shouldCreateInterviewSuccessfully() {

        Candidate candidate = Candidate.builder().id(1L).email("test@gmail.com").build();
        Interviewer interviewer = Interviewer.builder().id(2L).build();

        InterviewRequest request = new InterviewRequest(
                START,
                END,
                List.of(2L),
                1L
        );

        Interview saved = Interview.builder()
                .id(10L)
                .candidate(candidate)
                .interviewers(List.of(interviewer))
                .build();

        InterviewResponse response = new InterviewResponse(
                10L,
                START,
                END,
                1L,
                List.of(2L),
                InterviewStatus.SCHEDULED
        );

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L))).thenReturn(List.of(interviewer));
        when(interviewRepository.findConflictingInterviews(any(), any(), any())).thenReturn(List.of());
        when(interviewRepository.save(any())).thenReturn(saved);
        when(interviewMapper.toInterviewResponse(saved)).thenReturn(response);

        InterviewResponse result = interviewService.createInterview(request);

        assertNotNull(result);
        verify(notificationService).sendNotification(saved);
    }

    /**
     * Should throw when candidate not found
     */
    @Test
    void shouldThrowWhenCandidateNotFound() {

        InterviewRequest request = new InterviewRequest(
                START, END, List.of(2L), 1L
        );

        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }

    /**
     * Should throw when no interviewer found
     */
    @Test
    void shouldThrowWhenInterviewerNotFound() {

        Candidate candidate = Candidate.builder().id(1L).build();

        InterviewRequest request = new InterviewRequest(
                START, END, List.of(2L), 1L
        );

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L))).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }

    /**
     * Should throw when some interviewer IDs are invalid
     */
    @Test
    void shouldThrowWhenSomeInterviewersMissing() {

        Candidate candidate = Candidate.builder().id(1L).build();

        InterviewRequest request = new InterviewRequest(
                START, END, List.of(2L, 3L), 1L
        );

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L, 3L)))
                .thenReturn(List.of(Interviewer.builder().id(2L).build()));

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }

    /**
     * Should throw when time conflict exists
     */
    @Test
    void shouldThrowWhenTimeConflictExists() {

        Candidate candidate = Candidate.builder().id(1L).build();
        Interviewer interviewer = Interviewer.builder().id(2L).build();

        InterviewRequest request = new InterviewRequest(
                START, END, List.of(2L), 1L
        );

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L))).thenReturn(List.of(interviewer));
        when(interviewRepository.findConflictingInterviews(any(), any(), any()))
                .thenReturn(List.of(new Interview()));

        assertThrows(BusinessException.class,
                () -> interviewService.createInterview(request));
    }

    /**
     * Should search interviews successfully
     */
    @Test
    void shouldSearchInterviewsSuccessfully() {

        Interview interview = Interview.builder().id(1L).build();

        InterviewResponse response = new InterviewResponse(
                1L, START, END, 1L, List.of(2L), InterviewStatus.SCHEDULED
        );

        Page<Interview> page = new PageImpl<>(List.of(interview), PageRequest.of(0, 5), 1);

        when(interviewRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(page);
        when(interviewMapper.toInterviewResponse(interview)).thenReturn(response);

        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews("balaji", "john", 0, 5, null, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
    }

    /**
     * Should throw when invalid date range
     */
    @Test
    void shouldThrowWhenInvalidDateRange() {

        LocalDateTime from = START;
        LocalDateTime to = START.minusDays(1);

        assertThrows(ValidationException.class,
                () -> interviewService.searchInterviews(null, null, 0, 5, from, to));
    }

    /**
     * Should work with no filters
     */
    @Test
    void shouldWorkWithNoFilters() {

        Page<Interview> page = new PageImpl<>(List.of());

        when(interviewRepository.findAll(
                org.mockito.ArgumentMatchers.<Specification<Interview>>any(),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews(null, null, 0, 5, null, null);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    /**
     * Should return correct pagination details
     */
    @Test
    void shouldReturnCorrectPaginationDetails() {

        Page<Interview> page = new PageImpl<>(
                List.of(new Interview(), new Interview()),
                PageRequest.of(0, 2),
                6
        );

        when(interviewRepository.findAll(
                org.mockito.ArgumentMatchers.<Specification<Interview>>any(),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews(null, null, 0, 2, null, null);

        assertEquals(6, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }
}