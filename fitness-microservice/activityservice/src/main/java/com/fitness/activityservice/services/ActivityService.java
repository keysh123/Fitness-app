package com.fitness.activityservice.services;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entities.Activity;
import com.fitness.activityservice.repositories.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity=Activity.builder()
                .userID(activityRequest.getUserID())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurnt(activityRequest.getCaloriesBurnt())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity savedActivity=activityRepository.save(activity);


        return mapToResponse(savedActivity);
    }
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setCaloriesBurnt(activity.getCaloriesBurnt());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        activityResponse.setUserID(activity.getUserID());

        return activityResponse;
    }
}
