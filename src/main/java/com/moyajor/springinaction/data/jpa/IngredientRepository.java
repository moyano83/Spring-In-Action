package com.moyajor.springinaction.data.jpa;

import com.moyajor.springinaction.model.Ingredient;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
