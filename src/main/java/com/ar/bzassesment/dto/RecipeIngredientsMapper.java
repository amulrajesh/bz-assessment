package com.ar.bzassesment.dto;

import com.ar.bzassesment.dao.entity.Ingredients;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.dao.entity.RecipeIngredients;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeIngredientsMapper {

    public List<RecipeIngredients> getRecipeIngredients(Recipe recipe, List<Ingredients> ingredients) {
        return ingredients.stream()
                .map(ingredients1 -> new RecipeIngredients(null, recipe, ingredients1))
                .collect(Collectors.toList());
    }
}
