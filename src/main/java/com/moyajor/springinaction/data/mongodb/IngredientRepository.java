package com.moyajor.springinaction.data.mongodb;

import com.moyajor.springinaction.model.mongodb.Ingredient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository; import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="*")
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {}
