package com.moyajor.springinaction.data.mongodb;

import com.moyajor.springinaction.model.mongodb.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> { }
