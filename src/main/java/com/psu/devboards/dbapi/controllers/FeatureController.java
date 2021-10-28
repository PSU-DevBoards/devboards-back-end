package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Feature;
import com.psu.devboards.dbapi.services.FeatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("features")
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) { this.featureService = featureService; }

    @GetMapping
    public List<Feature> listFeatures(Principal principal) { return featureService.getAllFeatures(); }
}
