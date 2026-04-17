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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for InterviewService.
 * Covers interview creation, validation, conflict detection,
 * and search functionality with pagination.
 */
@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {
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

    /**
     * Verifies successful interview creation when all inputs are valid.
     */
    @Test
    void shouldCreateInterviewSuccessfully() {

        // GIVEN
        Candidate candidate = Candidate.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        Interviewer interviewer = Interviewer.builder().id(2L).build();

        InterviewRequest request = new InterviewRequest();
        request.setCandidateId(1L);
        request.setInterviewerIds(List.of(2L));
        request.setScheduledAt(LocalDateTime.now());
        request.setEndAt(LocalDateTime.now().plusHours(1));

        Interview savedInterview = Interview.builder()
                .id(10L)
                .candidate(candidate)
                .interviewers(List.of(interviewer))
                .build();

        InterviewResponse response = new InterviewResponse();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L)))
                .thenReturn(List.of(interviewer));
        when(interviewRepository.findConflictingInterviews(any(), any(), any()))
                .thenReturn(List.of());
        when(interviewRepository.save(any())).thenReturn(savedInterview);
        when(interviewMapper.toInterviewResponse(savedInterview)).thenReturn(response);

        // WHEN
        InterviewResponse result = interviewService.createInterview(request);

        // THEN
        assertNotNull(result);
        verify(notificationService).sendNotification(savedInterview);
    }


    /**
     * Verifies exception when candidate is not found.
     */
    @Test
    void shouldThrowWhenCandidateNotFound() {

        InterviewRequest request = new InterviewRequest();
        request.setCandidateId(1L);

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }

    /**
     * Verifies exception when interviewer list is empty.
     */
    @Test
    void shouldThrowWhenInterviewerNotFound() {

        Candidate candidate = Candidate.builder().id(1L).build();

        InterviewRequest request = new InterviewRequest();
        request.setCandidateId(1L);
        request.setInterviewerIds(List.of(2L));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(List.of(2L)))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }


    /**
     * Verifies exception when some interviewer IDs are invalid.
     */
    @Test
    void shouldThrowWhenSomeInterviewersMissing() {

        Candidate candidate = Candidate.builder().id(1L).build();

        InterviewRequest request = new InterviewRequest();
        request.setCandidateId(1L);
        request.setInterviewerIds(List.of(2L, 3L));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));


        when(interviewerRepository.findAllById(List.of(2L, 3L)))
                .thenReturn(List.of(Interviewer.builder().id(2L).build()));

        assertThrows(ResourceNotFoundException.class,
                () -> interviewService.createInterview(request));
    }


    /**
     * Verifies exception when interview time conflicts with existing interviews.
     */
    @Test
    void shouldThrowWhenTimeConflictExists() {

        Candidate candidate = Candidate.builder().id(1L).build();
        Interviewer interviewer = Interviewer.builder().id(2L).build();

        InterviewRequest request = new InterviewRequest();
        request.setCandidateId(1L);
        request.setInterviewerIds(List.of(2L));
        request.setScheduledAt(LocalDateTime.now());
        request.setEndAt(LocalDateTime.now().plusHours(1));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        when(interviewerRepository.findAllById(List.of(2L)))
                .thenReturn(List.of(interviewer));

        when(interviewRepository.findConflictingInterviews(any(), any(), any()))
                .thenReturn(List.of(new Interview()));

        assertThrows(BusinessException.class,
                () -> interviewService.createInterview(request));
    }


    /**
     * Verifies successful interview search with filters and pagination.
     */
    @Test
    void shouldSearchInterviewsSuccessfully() {

        // GIVEN
        Interview interview = Interview.builder().id(1L).build();

        InterviewResponse response = new InterviewResponse();

        Page<Interview> page = new PageImpl<>(
                List.of(interview),
                PageRequest.of(0, 5),
                1
        );

        when(interviewRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        when(interviewMapper.toInterviewResponse(interview))
                .thenReturn(response);

        // WHEN
        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews(
                        "balaji",
                        "john",
                        0,
                        5,
                        null,
                        null
                );

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());

        verify(interviewRepository).findAll(
                any(Specification.class),
                any(Pageable.class)
        );
        verify(interviewMapper).toInterviewResponse(interview);
    }


    /**
     * Verifies validation error when date range is invalid.
     */
    @Test
    void shouldThrowWhenInvalidDateRange() {

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.minusDays(1);

        assertThrows(ValidationException.class,
                () -> interviewService.searchInterviews(
                        null,
                        null,
                        0,
                        5,
                        from,
                        to
                ));
    }

    /**
     * Verifies search works when no filters are applied.
     */
    @Test
    void shouldWorkWithNoFilters() {

        Page<Interview> page = new PageImpl<>(List.of());

        when(interviewRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews(
                        null,
                        null,
                        0,
                        5,
                        null,
                        null
                );

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    /**
     * Verifies pagination metadata is correctly mapped.
     */
    @Test
    void shouldReturnCorrectPaginationDetails() {

        Page<Interview> page = new PageImpl<>(
                List.of(new Interview(), new Interview()),
                PageRequest.of(0, 2),
                6
        );

        when(interviewRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<InterviewResponse> result =
                interviewService.searchInterviews(null, null, 0, 2, null, null);

        assertEquals(6, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }

}
