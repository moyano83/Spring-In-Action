package com.moyajor.springinaction.data.cassandra;

import com.moyajor.springinaction.model.cassandra.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TacoRepository extends ReactiveCrudRepository<Taco, UUID> {}