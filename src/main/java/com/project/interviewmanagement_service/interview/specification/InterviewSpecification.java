package com.project.interviewmanagement_service.interview.specification;

import com.project.interviewmanagement_service.interview.entity.Interview;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Builds dynamic query filters for Interview search.
 * Supports filtering by candidate name, interviewer name, and date range.
 */
public class InterviewSpecification {


    public static Specification<Interview> filter(
            String candidateName,
            String interviewerName,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        return (root, query, cb) -> {

            // Apply fetch joins only for entity queries (not count queries)
            // Prevents N+1 problem and avoids issues with pagination count query


            if (Interview.class.equals(query.getResultType())) {
                root.fetch("candidate", JoinType.LEFT);
                root.fetch("interviewers", JoinType.LEFT);
                query.distinct(true);
            }

            var predicate = cb.conjunction();

            // Filter by candidate name (partial match)
            if (candidateName != null && !candidateName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.join("candidate").get("name"),
                                "%" + candidateName + "%"));
            }

            // Filter by interviewer name (join with interviewers)
            if (interviewerName != null && !interviewerName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.join("interviewers").get("name"),
                                "%" + interviewerName + "%"));
            }



            // Filter interviews scheduled after or equal to fromDate
            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("scheduledAt"), fromDate));
            }

            // Filter interviews scheduled before or equal to toDate
            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("scheduledAt"), toDate));
            }

            return predicate;
        };
    }
}