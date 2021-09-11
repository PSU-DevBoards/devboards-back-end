package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

    @Override
    List<WorkItem> findAll();
}
