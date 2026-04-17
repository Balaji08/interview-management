package com.project.interviewmanagement_service.interviewer.controller;


import com.project.interviewmanagement_service.interviewer.entity.Interviewer;
import com.project.interviewmanagement_service.interviewer.service.InterviewerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewer")
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;

    @PostMapping
    public ResponseEntity<Interviewer> createInterviewer(@Valid @RequestBody Interviewer interviewer)
    {
        Interviewer result = interviewerService.createInterviewer(interviewer);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public  ResponseEntity<List<Interviewer>> getAllInterviewer()
    {
        List<Interviewer> result = interviewerService.getAllInterviewer();
        return  ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
