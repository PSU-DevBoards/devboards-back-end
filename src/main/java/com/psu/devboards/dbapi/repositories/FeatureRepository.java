package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Feature;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface FeatureRepository extends CrudRepository<Feature, Long> {

    @Override
    public List<Feature> findAll();
}
