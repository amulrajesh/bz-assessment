package com.ar.bzassesment;

import com.ar.bzassesment.dao.entity.Ingredients;
import com.ar.bzassesment.dao.entity.Instructions;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.dao.entity.RecipeIngredients;
import com.ar.bzassesment.dao.repo.IngredientsRepository;
import com.ar.bzassesment.dao.repo.InstructionsRepository;
import com.ar.bzassesment.dao.repo.RecipeIngredientsRepository;
import com.ar.bzassesment.dao.repo.RecipeRepository;
import com.ar.bzassesment.model.RecipeForm;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.model.RecipeResponseFormList;
import com.ar.bzassesment.service.RecipeSearchService;
import com.ar.bzassesment.service.RecipeService;
import com.ar.bzassesment.utils.AppConstant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class BzAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BzAssessmentApplication.class, args);
    }

    /**
    @Bean
    CommandLineRunner run(RecipeRepository recipeRepository,
                          IngredientsRepository ingredientsRepository,
                          RecipeIngredientsRepository recipeIngredientsRepository,
                          InstructionsRepository instructionsRepository,
                          RecipeService recipeService,
                          RecipeSearchService recipeSearchService) {
        //
        return args -> {
            List<String> vegIngredientsStr = this.getVegIngredientList();
            List<Ingredients> vegIngredients = this.getVegIngredients();
            ingredientsRepository.saveAll(vegIngredients);
            Recipe pastaRecipe = this.getPastaRecipe();
            recipeRepository.save(pastaRecipe);
            recipeIngredientsRepository.saveAll(
                    vegIngredients.stream().filter(i -> vegIngredientsStr.contains(i.getName()))
                            .map(i -> new RecipeIngredients(null, pastaRecipe, i))
                            .collect(Collectors.toList())
            );
            System.out.println("-----------------------------------------------------");
            instructionsRepository.findAll().stream().forEach(ins ->
                    System.out.println(ins.getId() + " \t\t"  +ins.getInsSteps() + "\t " + ins.getRecipe().getId()));
            System.out.println("-----------------------------------------------------");
            Recipe chickenKebabTallrikRecipe = this.getChickenKebabTallrikRecipe();
            recipeRepository.save(chickenKebabTallrikRecipe);
            List<Ingredients> nonVegIngredients = this.getNonVegIngredients();
            ingredientsRepository.saveAll(nonVegIngredients);
            List<String> nonVegIngredientsStr = this.getNonVegIngredientList();
            recipeIngredientsRepository.saveAll(
                    nonVegIngredients.stream().filter(i -> nonVegIngredientsStr.contains(i.getName()))
                            .map(i -> new RecipeIngredients(null, chickenKebabTallrikRecipe, i))
                            .collect(Collectors.toList())
            );
            recipeService.createRecipe(this.getRecipeForm());
            recipeService.createRecipe(this.getRecipeForm_1());
            recipeService.createRecipe(this.getRecipeForm_2());
            System.out.println("--------------------------------------");
            System.out.println("Pasta recipe id " + pastaRecipe.getId());
            RecipeResponseForm recipeResponseForm = recipeService.get(pastaRecipe.getId());
            System.out.println(recipeResponseForm.toString());
            System.out.println("--------------------------------------");
            //recipeService.delete(pastaRecipe.getId());
            RecipeResponseFormList recipeResponseForms = recipeService.load();
            recipeResponseForms.getRecipeResponseForms().stream().forEach(System.out::println);
            System.out.println("--------------------------------------");
            recipeSearchService.search("veg", null, null, null, null, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, null, null, 1, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, Arrays.asList("Green Peas"), null, 1, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, Arrays.asList("Chicken"), null, 2, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, Arrays.asList("Chicken"), null, 1, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, Arrays.asList("Chicken"), Arrays.asList("Beef", "Chicken"), 1, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", null, null, Arrays.asList("Beef", "Chicken"), 1, null);
            System.out.println("--------------------------------------");
            recipeSearchService.search("non-veg", Arrays.asList("Deep Fry", "Large onion rings", "Add more pepper and salt"), Arrays.asList("Chicken"), Arrays.asList("Beef", "Chicken"), 1, null);
        };
    }

    private RecipeForm getRecipeForm() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Burger");
        recipeForm.setInstructions(Arrays.asList("Add potato layer, Add tomato sauce"));
        recipeForm.setNoOfServings(1);
        recipeForm.setType("veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Bun", "Tomato Sauce", "Onion Rings"));
        return recipeForm;
    }

    private RecipeForm getRecipeForm_1() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Chicken Rings");
        recipeForm.setInstructions(Arrays.asList("Deep Fry", "Large onion rings", "Add more pepper and salt"));
        recipeForm.setNoOfServings(2);
        recipeForm.setType("non-veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Chicken", "Onion"));
        return recipeForm;
    }

    private RecipeForm getRecipeForm_2() {
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setRecipeName("Chicken Rings Small");
        recipeForm.setInstructions(Arrays.asList("Medium Fry", "Boneless"));
        recipeForm.setNoOfServings(1);
        recipeForm.setType("non-veg");
        recipeForm.setUserId("user");
        recipeForm.setIngredients(Arrays.asList("Chicken", "Tomato"));
        return recipeForm;
    }

    private Recipe getPastaRecipe() {
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

    private List<Ingredients> getVegIngredients() {
        return this.getVegIngredientList().stream()
                .map(str -> new Ingredients(null, str, new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin"))
                .collect(Collectors.toList());
    }

    private List<Ingredients> getNonVegIngredients() {
        return this.getNonVegIngredientList().stream()
                .map(str -> {
                    return new Ingredients(null, str, new Timestamp((new Date()).getTime()), "admin", new Timestamp((new Date()).getTime()), "admin");
                })
                .collect(Collectors.toList());
    }

    private List<String> getVegIngredientList() {
        return Arrays.asList("Tomato", "Cream", "Cheese", "Milk", "Bread", "Olive", "Chilli", "Onion", "Capsicum", "Egg", "Green Peas", "Shredded Cheese", "Pepper", "Salt");
    }

    private List<String> getNonVegIngredientList() {
        return Arrays.asList("Chicken", "Beef", "Pork", "Salmon", "Sea Gul");
    }

    private Recipe getChickenKebabTallrikRecipe() {
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
    **/
}
