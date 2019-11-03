package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.db.Ingredient;

public interface IngredientRepository {


    Iterable<Ingredient> findAll();

    Ingredient findOne(String id);

    Ingredient save(Ingredient ingredient);

}
