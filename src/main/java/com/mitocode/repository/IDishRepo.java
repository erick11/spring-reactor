package com.mitocode.repository;

import com.mitocode.model.Dish;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IDishRepo extends ReactiveMongoRepository<Dish, String> {

}