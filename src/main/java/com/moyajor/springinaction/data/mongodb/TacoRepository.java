package com.moyajor.springinaction.data.mongodb;

import com.moyajor.springinaction.model.mongodb.Taco;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository; import reactor.core.publisher.Flux;

public interface TacoRepository extends ReactiveMongoRepository<Taco, String> {
    Flux<Taco> findByOrderByCreatedAtDesc();
}

