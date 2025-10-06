package com.fitness.aiservices.service;

import com.fitness.aiservices.model.Recommendation;
import com.fitness.aiservices.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {

      return   recommendationRepository.findByUserId(userId);

    }

    public List<Recommendation> getActivityRecommendation(String activityId) {

    return recommendationRepository.findByActivityId(activityId).orElseThrow(()-> new RuntimeException("No recommendation not found"));
    }
}
