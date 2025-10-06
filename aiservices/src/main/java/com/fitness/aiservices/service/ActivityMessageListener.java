package com.fitness.aiservices.service;

import com.fitness.aiservices.model.Activity;
import com.fitness.aiservices.model.Recommendation;
import com.fitness.aiservices.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final  ActivityAIService activityAIService;

    private final RecommendationRepository recommendationRepository;


    @RabbitListener(queues = "activity.queue")
    public void processActivityMessage(Activity activity){
        log.info("Processing activity Message: {}", activity.getId());


        //log.info("Generated Recommendation: {}", activityAIService.generateRecommendation(activity));
        Recommendation recommendation= activityAIService.generateRecommendation(activity);
        log.info("Generated Recommendation To save in DB: {}", recommendation.getActivityId());
        recommendationRepository.save(recommendation);


    }
}
