package com.project.interviewmanagement_service.common.utils;

public enum ErrorCode {

    CANDIDATE_NOT_FOUND("Candidate not found with id: %s"),
    INTERVIEWER_NOT_FOUND("Interviewer not found with id %s"),
    SOME_INTERVIEWER_NOT_FOUND("Some interviewers not found with id %s"),
    INTERVIEW_NOT_FOUND("Interview not found with id: %s"),
    INVALID_STATUS_TRANSITION("Invalid status transition from %s to %s"),
    SCHEDULING_CONFLICT("Interview already scheduled at %s"),
    INTERVIEW_TIME_CONFLICT("Interview time conflicts with existing schedule"),
    INVALID_DATE_RANGE("From Date cannot be after To Date"),
    STATUS_ALREADY_EXIST("Same status already exist"),
    CANNOT_CHANGE_CANCELLED("Cannot change cancelled interview"),
    SCHEDULED_CAN_BE_RESCHEDULED("Only scheduled interviews can be rescheduled"),
    CANNOT_RESCHEDULE("Cannot rescheduled to past time"),
    CANNOT_CHANGE_COMPLETED("Cannot change completed interview"),
    INTERVIEWER_NOT_ASSIGNED("Interviewer was not assigned to this interview"),

    // Validation
    VALIDATION_ERROR("Validation failed"),

    // DB
    DATA_INTEGRITY_VIOLATION("Database constraint violation"),

    INTERNAL_SERVER_ERROR("Something went wrong");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return message.formatted(args);
    }

    public String getMessage() {
        return message;
    }
}
