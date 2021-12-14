package com.psu.devboards.dbapi.models.specifications;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification for WorkItem filters.
 */
@Builder
public class WorkItemSpecification implements Specification<WorkItem> {

    private final WorkItemStatus status;
    private final WorkItemType type;
    private final Integer organizationId;
    private final Integer parentId;

    /**
     * Builds a Predicate based on the supplied filters.
     */
    @Override
    public Predicate toPredicate(Root<WorkItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (status != null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status)));
        if (type != null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), type)));
        if (organizationId != null)
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organization").get("id"),
                    organizationId)));
        if (parentId != null)
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("parent").get("id"), parentId)));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
