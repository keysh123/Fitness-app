package com.fitness.activityservice.services;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entities.Activity;
import com.fitness.activityservice.repositories.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean isValidUser = userValidationService.validateUser(activityRequest.getUserID());
        if(!isValidUser){
            throw new RuntimeException("Invalid user");
        }
        Activity activity=Activity.builder()
                .userID(activityRequest.getUserID())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurnt(activityRequest.getCaloriesBurnt())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity savedActivity=activityRepository.save(activity);
//        publish to rabbitmq
        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        }
        catch (Exception e){
            log.error("Failed to publish activity to rabbitmq",e);

        }

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

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities= activityRepository.findByUserID(userId);
        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return mapToResponse(activityRepository.findById(activityId).orElseThrow(()-> new RuntimeException("Not found")));
    }
}
