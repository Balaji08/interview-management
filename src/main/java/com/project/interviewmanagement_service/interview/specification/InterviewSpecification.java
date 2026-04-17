package com.project.interviewmanagement_service.interview.specification;

import com.project.interviewmanagement_service.interview.entity.Interview;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class InterviewSpecification {

    public static Specification<Interview> filter(
            String candidateName,
            String interviewerName,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        return (root, query, cb) -> {

            if (Interview.class.equals(query.getResultType())) {
                root.fetch("candidate", JoinType.LEFT);
                root.fetch("interviewers", JoinType.LEFT);
                query.distinct(true);
            }

            var predicate = cb.conjunction();

            if (candidateName != null && !candidateName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.join("candidate").get("name"),
                                "%" + candidateName + "%"));
            }

            if (interviewerName != null && !interviewerName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.join("interviewers").get("name"),
                                "%" + interviewerName + "%"));
            }



            // ✅ From Date filter
            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("scheduledAt"), fromDate));
            }

            // ✅ To Date filter
            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("scheduledAt"), toDate));
            }

            return predicate;
        };
    }
}