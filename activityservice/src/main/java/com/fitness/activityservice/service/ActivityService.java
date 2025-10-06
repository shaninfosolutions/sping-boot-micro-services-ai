package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser=userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw  new RuntimeException("Invalid User:" +request.getUserId());
        }

        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .addiontalMetries(request.getAddiontalMetries()).build();

        Activity saveActivity=activityRepository.save(activity);

        return mapToResponse(saveActivity);

    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response=new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        response.setStartTime(activity.getStartTime());
        response.setAddiontalMetries(activity.getAddiontalMetries());

        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {

        List<Activity> activities= activityRepository.findByUserId(userId);

        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

    public ActivityResponse getActivityById(String activityId) {

        return activityRepository.findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(()->new RuntimeException("Activity nos found by id:"+activityId) );
    }
}
