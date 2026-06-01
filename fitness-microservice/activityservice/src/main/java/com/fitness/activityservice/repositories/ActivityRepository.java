package com.fitness.activityservice.repositories;

import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entities.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity,String> {
    List<Activity> findByUserID(String userId);
}
