package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkItemRepository extends JpaRepository<WorkItem, Integer> {
    @Override
    List<WorkItem> findAll();
}
