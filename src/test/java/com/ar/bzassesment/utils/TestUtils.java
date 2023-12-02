package com.ar.bzassesment.utils;

import com.ar.bzassesment.dao.entity.Ingredients;
import com.ar.bzassesment.dao.entity.Instructions;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.model.RecipeForm;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static RecipeForm getRecipeForm() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Burger");
        recipeForm.setInstructions(Arrays.asList("Add potato layer, Add tomato sauce"));
        recipeForm.setNoOfServings(1);
        recipeForm.setType("veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Bun", "Tomato Sauce", "Onion Rings"));
        return recipeForm;
    }

    public static RecipeForm getRecipeForm_1() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Chicken Rings");
        recipeForm.setInstructions(Arrays.asList("Deep Fry", "Large onion rings", "Add more pepper and salt"));
        recipeForm.setNoOfServings(2);
        recipeForm.setType("non-veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Chicken", "Onion"));
        return recipeForm;
    }

    public static RecipeForm getRecipeForm_2() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Chicken Rings Small");
        recipeForm.setInstructions(Arrays.asList("Medium Fry", "Boneless"));
        recipeForm.setNoOfServings(1);
        recipeForm.setType("non-veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Chicken", "Tomato"));
        return recipeForm;
    }

    public static Recipe getPastaRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        List<Instructions> instructionsList = Arrays.asList(
                new Instructions(null, recipe, "Cooked in Oven", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe, "Add shredded cease on the top", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe, "Handburst bun", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"));
        recipe.setInstructions(instructionsList);
        recipe.setVeg(AppConstant.CHAR_Y);
        recipe.setNoOfServings(2);
        recipe.setCratTsp(new Timestamp((new Date()).getTime()));
        recipe.setCratUserId("admin");
        recipe.setUpdTsp(new Timestamp((new Date()).getTime()));
        recipe.setUpdUserId("admin");
        return recipe;
    }

    public static List<Ingredients> getVegIngredients() {
        return getVegIngredientList().stream()
                .map(str -> new Ingredients(null, str, new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"))
                .collect(Collectors.toList());
    }

    public static List<Ingredients> getNonVegIngredients() {
        return getNonVegIngredientList().stream()
                .map(str -> {
                    return new Ingredients(null, str, new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin");
                })
                .collect(Collectors.toList());
    }

    public static List<String> getVegIngredientList() {
        return Arrays.asList("Tomato", "Cream", "Cheese", "Milk", "Bread", "Olive", "Chilli", "Onion", "Capsicum", "Egg", "Green Peas", "Shredded Cheese", "Pepper", "Salt");
    }

    public static List<String> getNonVegIngredientList() {
        return Arrays.asList("Chicken", "Beef", "Pork", "Salmon", "Sea Gul");
    }

    public static Recipe getChickenKebabTallrikRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("Chicken Kebab Tarik");
        List<Instructions> instructionsList = Arrays.asList(
                new Instructions(null, recipe, "Fully cooked", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe,"Without sauce", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe, "Add all ingredients", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe,"No eggs", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"),
                new Instructions(null, recipe,"Less pepper", new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"));
        recipe.setInstructions(instructionsList);
        recipe.setVeg(AppConstant.CHAR_N);
        recipe.setNoOfServings(1);
        recipe.setCratTsp(new Timestamp((new Date()).getTime()));
        recipe.setCratUserId("admin");
        recipe.setUpdTsp(new Timestamp((new Date()).getTime()));
        recipe.setUpdUserId("admin");
        return recipe;
    }
}
