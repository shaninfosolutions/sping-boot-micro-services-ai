package com.fitness.aiservices.repository;

import com.fitness.aiservices.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation,String> {
    List<Recommendation> findByUserId(String userId);

    Optional< List<Recommendation> >findByActivityId(String activityId);
}
