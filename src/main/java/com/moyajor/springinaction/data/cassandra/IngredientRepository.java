package com.moyajor.springinaction.data.cassandra;

import com.moyajor.springinaction.model.cassandra.Ingredient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {}

