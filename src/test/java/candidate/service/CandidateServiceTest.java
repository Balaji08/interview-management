package candidate.service;

import com.project.interviewmanagement_service.candidate.dto.CandidateRequest;
import com.project.interviewmanagement_service.candidate.dto.CandidateResponse;
import com.project.interviewmanagement_service.candidate.entity.Candidate;
import com.project.interviewmanagement_service.candidate.mapper.CandidateResponseMapper;
import com.project.interviewmanagement_service.candidate.repository.CandidateRepository;
import com.project.interviewmanagement_service.candidate.service.CandidateService;
import com.project.interviewmanagement_service.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @InjectMocks
    private CandidateService candidateService;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateResponseMapper mapper;


    /**
     * Should create a candidate successfully when:
     * - Valid candidate entity is provided
     * - Repository saves the entity
     * - Mapper converts entity to response DTO
     */
    @Test
    void shouldCreateCandidateSuccessfully() {

        // GIVEN
        Candidate candidate = Candidate.builder()
                .name("Balaji")
                .email("balaji@gmail.com")
                .build();
        CandidateRequest candidateRequest =
                new CandidateRequest("Balaji", "balaji@gmail.com");

        Candidate saved = Candidate.builder()
                .id(1L)
                .name("Balaji")
                .email("balaji@gmail.com")
                .build();

        CandidateResponse response = new CandidateResponse(
                1L,
                "Balaji",
                "balaji@gmail.com"
        );

        when(candidateRepository.save(candidate)).thenReturn(saved);
        when(mapper.toCandidateResponse(saved)).thenReturn(response);

        // WHEN
        CandidateResponse result = candidateService.createCandidate(candidateRequest);

        // THEN
        assertNotNull(result);
        verify(candidateRepository).save(candidate);
        verify(mapper).toCandidateResponse(saved);
    }


    /**
     * Should return candidate when:
     * - Candidate exists in repository
     * - Mapper converts entity to response DTO
     */
    @Test
    void shouldReturnCandidateById() {

        // GIVEN
        Candidate candidate = Candidate.builder()
                .id(1L)
                .name("Balaji")
                .email("balaji@gmail.com")
                .build();

        CandidateResponse response = new CandidateResponse(
                1L,
                "Balaji",
                "balaji@gmail.com"
        );

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        when(mapper.toCandidateResponse(candidate))
                .thenReturn(response);

        // WHEN
        CandidateResponse result = candidateService.getCandidateById(1L);

        // THEN
        assertNotNull(result);
        verify(candidateRepository).findById(1L);
        verify(mapper).toCandidateResponse(candidate);
    }

    /**
     * Should throw ResourceNotFoundException when:
     * - Candidate does not exist in repository
     */
    @Test
    void shouldThrowWhenCandidateNotFound() {

        // GIVEN
        when(candidateRepository.findById(1L))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(ResourceNotFoundException.class,
                () -> candidateService.getCandidateById(1L));
    }
}