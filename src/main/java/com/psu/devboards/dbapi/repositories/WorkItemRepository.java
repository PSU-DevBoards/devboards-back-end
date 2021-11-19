package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WorkItemRepository extends JpaRepository<WorkItem, Integer>, JpaSpecificationExecutor<WorkItem> {
    @Override
    List<WorkItem> findAll();
}
