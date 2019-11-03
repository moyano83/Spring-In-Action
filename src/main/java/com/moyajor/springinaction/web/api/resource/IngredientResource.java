package com.moyajor.springinaction.web.api.resource;

import com.moyajor.springinaction.model.db.Ingredient;
import org.springframework.hateoas.ResourceSupport;
import lombok.Getter;
public class IngredientResource extends ResourceSupport {
    @Getter
    private String name;
    @Getter
    private Ingredient.Type type;

    public IngredientResource(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
