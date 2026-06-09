package com.fitness.aiservice.services;

import com.fitness.aiservice.entities.Activity;
import com.fitness.aiservice.entities.Recommendation;
import com.fitness.aiservice.repositories.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAIService activityAIService;
    private final RecommendationRepository recommendationRepository;
    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity) {

        try {
            log.info("Received activity for processing {}", activity.getId());

            Recommendation recommendation =
                    activityAIService.generateRecommendation(activity);

            recommendationRepository.save(recommendation);

        } catch (Exception e) {
            log.error("Failed to process activity {}", activity.getId(), e);
        }
    }
}
