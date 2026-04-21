package interviewer.service;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerRequest;
import com.project.interviewmanagement_service.interviewer.dto.InterviewerResponse;
import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.mapper.InterviewerMapper;
import com.project.interviewmanagement_service.interviewer.repository.InterviewerRepository;
import com.project.interviewmanagement_service.interviewer.service.InterviewerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewerServiceTest {

    @InjectMocks
    private InterviewerService interviewerService;

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private InterviewerMapper interviewerMapper;


    @Test
    void shouldCreateInterviewerSuccessfully() {

        // GIVEN
        Interviewer interviewer = Interviewer.builder()
                .name("John")
                .expertise("Java")
                .build();

        InterviewerRequest request =
                new InterviewerRequest("John", "Java");

        Interviewer saved = Interviewer.builder()
                .id(1L)
                .name("John")
                .expertise("Java")
                .build();

        InterviewerResponse response =
                new InterviewerResponse(1L, "John", "Java");

        when(interviewerRepository.save(interviewer)).thenReturn(saved);
        when(interviewerMapper.toInterviewerResponse(saved)).thenReturn(response);

        // WHEN
        InterviewerResponse result =
                interviewerService.createInterviewer(request);

        // THEN
        assertNotNull(result);
        verify(interviewerRepository).save(interviewer);
        verify(interviewerMapper).toInterviewerResponse(saved);
    }

    @Test
    void shouldReturnAllInterviewers() {

        // GIVEN
        Interviewer interviewer1 = Interviewer.builder()
                .id(1L)
                .name("John")
                .expertise("Java")
                .build();

        Interviewer interviewer2 = Interviewer.builder()
                .id(2L)
                .name("Jane")
                .expertise("Spring")
                .build();

        InterviewerResponse response1 =
                new InterviewerResponse(1L, "John", "Java");

        InterviewerResponse response2 =
                new InterviewerResponse(2L, "Jane", "Spring");

        when(interviewerRepository.findAll())
                .thenReturn(List.of(interviewer1, interviewer2));

        when(interviewerMapper.toInterviewerResponse(interviewer1))
                .thenReturn(response1);

        when(interviewerMapper.toInterviewerResponse(interviewer2))
                .thenReturn(response2);

        // WHEN
        List<InterviewerResponse> result =
                interviewerService.getAllInterviewer("");

        // THEN
        assertEquals(2, result.size());
        verify(interviewerRepository).findAll();
        verify(interviewerMapper, times(2)).toInterviewerResponse(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoInterviewers() {

        when(interviewerRepository.findAll())
                .thenReturn(List.of());

        List<InterviewerResponse> result =
                interviewerService.getAllInterviewer("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
