package com.ar.bzassesment.dto;

import com.ar.bzassesment.dao.entity.Instructions;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.model.RecipeForm;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.utils.AppConstant;
import com.ar.bzassesment.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RecipeMapper {

    public Recipe formToRecipe(RecipeForm recipeForm, Optional<Recipe> recipeOptional) {

        Recipe recipe = recipeOptional.isPresent() ? recipeOptional.get() : new Recipe();

        return this.getRecipe(recipeForm, recipe);
    }

    public RecipeResponseForm entityToRecipeForm(Recipe recipe) {
        RecipeResponseForm recipeResponseForm = new RecipeResponseForm();
        recipeResponseForm.setRecipeName(recipe.getName());
        //recipeResponseForm.setInstructions(AppUtils.getInstructionsAsString(recipe.getInstructions()));
        recipeResponseForm.setType((recipe.getVeg() == AppConstant.CHAR_Y) ? "Veg" : "Non-Veg");
        recipeResponseForm.setNoOfServings(recipe.getNoOfServings());
        return recipeResponseForm;
    }

    private Recipe getRecipe(RecipeForm recipeForm, Recipe recipe) {
        if (recipeForm.getId() != null) {
            recipe.setId(recipeForm.getId());
        } else {
            recipe.setCratUserId(recipeForm.getUserId());
            recipe.setCratTsp(new Timestamp((new Date()).getTime()));
        }
        recipe.setName(recipeForm.getRecipeName());
        recipe.setVeg((recipeForm.getType().equalsIgnoreCase("veg")) ? AppConstant.CHAR_Y : AppConstant.CHAR_N);
        recipe.setNoOfServings(recipeForm.getNoOfServings());
        recipe.setInstructions(this.getInstructions(recipeForm, recipe));
        recipe.setUpdUserId(recipeForm.getUserId());
        recipe.setUpdTsp(new Timestamp((new Date()).getTime()));
        return recipe;
    }

    private List<Instructions> getInstructions(RecipeForm recipeForm, Recipe recipe) {
        return recipeForm.getInstructions()
                .stream()
                .map(str -> new Instructions(null, recipe, str, new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"))
                .collect(Collectors.toList());
    }
}
