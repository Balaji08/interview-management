package com.project.interviewmanagement_service.feedback.controller;
import com.project.interviewmanagement_service.common.dto.ApiResponse;
import com.project.interviewmanagement_service.feedback.dto.FeedBackRequest;
import com.project.interviewmanagement_service.feedback.dto.FeedBackResponse;
import com.project.interviewmanagement_service.feedback.service.FeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
@Tag(name = "Feedback-service", description = "Feedback management APIs")
public class FeedBackController {

    private final FeedBackService feedBackService;

    /**
     * Submits feedback for a specific interview by an interviewer.
     *
     * @param interviewId ID of the interview
     * @param request     feedback details (rating, comments, interviewerId)
     * @return created feedback response
     */
    @Operation(summary = "Create new Feedback")
    @PostMapping("/{interviewId}/feedback")
    public ResponseEntity<ApiResponse<FeedBackResponse>> createFeedBack(@PathVariable Long interviewId, @Valid @RequestBody FeedBackRequest request)
    {
        FeedBackResponse result = feedBackService.createFeedBack(interviewId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Feedback created successfully", result));
    }
}
