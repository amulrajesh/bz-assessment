package com.ar.bzassesment.dto;

import com.ar.bzassesment.dao.entity.Ingredients;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.dao.entity.RecipeIngredients;
import com.ar.bzassesment.model.RecipeForm;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngredientMapper {

    public List<Ingredients> formToIngredients(RecipeForm recipeForm) {
        return recipeForm.getIngredients()
                .stream()
                .map(i -> this.getIngredients(i, recipeForm.getUserId()))
                .collect(Collectors.toList());
    }

    public Ingredients getIngredients(String name, String userId) {
        Ingredients ingredients = new Ingredients();
        ingredients.setName(name);
        ingredients.setCratUserId(userId);
        ingredients.setCratTsp(new Timestamp((new Date()).getTime()));
        ingredients.setUpdUserId(userId);
        ingredients.setUpdTsp(new Timestamp((new Date()).getTime()));
        return ingredients;
    }
}
