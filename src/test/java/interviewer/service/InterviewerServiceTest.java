package interviewer.service;
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

        Interviewer saved = Interviewer.builder()
                .id(1L)
                .name("John")
                .expertise("Java")
                .build();

        InterviewerResponse response = new InterviewerResponse();

        when(interviewerRepository.save(interviewer)).thenReturn(saved);
        when(interviewerMapper.toInterviewerResponse(saved)).thenReturn(response);

        // WHEN
        InterviewerResponse result =
                interviewerService.createInterviewer(interviewer);

        // THEN
        assertNotNull(result);
        verify(interviewerRepository).save(interviewer);
        verify(interviewerMapper).toInterviewerResponse(saved);
    }

    @Test
    void shouldReturnAllInterviewers() {

        // GIVEN
        Interviewer interviewer1 = Interviewer.builder().id(1L).build();
        Interviewer interviewer2 = Interviewer.builder().id(2L).build();

        InterviewerResponse response1 = new InterviewerResponse();
        InterviewerResponse response2 = new InterviewerResponse();

        when(interviewerRepository.findAll())
                .thenReturn(List.of(interviewer1, interviewer2));

        when(interviewerMapper.toInterviewerResponse(interviewer1))
                .thenReturn(response1);

        when(interviewerMapper.toInterviewerResponse(interviewer2))
                .thenReturn(response2);

        // WHEN
        List<InterviewerResponse> result =
                interviewerService.getAllInterviewer();

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
                interviewerService.getAllInterviewer();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
