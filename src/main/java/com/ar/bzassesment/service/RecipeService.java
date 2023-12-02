package com.ar.bzassesment.service;

import com.ar.bzassesment.dao.entity.Ingredients;
import com.ar.bzassesment.dao.entity.Instructions;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.dao.entity.RecipeIngredients;
import com.ar.bzassesment.dao.repo.IngredientsRepository;
import com.ar.bzassesment.dao.repo.InstructionsRepository;
import com.ar.bzassesment.dao.repo.RecipeIngredientsRepository;
import com.ar.bzassesment.dao.repo.RecipeRepository;
import com.ar.bzassesment.dto.IngredientMapper;
import com.ar.bzassesment.dto.RecipeIngredientsMapper;
import com.ar.bzassesment.dto.RecipeMapper;
import com.ar.bzassesment.exception.ExecutionException;
import com.ar.bzassesment.model.RecipeForm;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.model.RecipeResponseFormList;
import com.ar.bzassesment.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ar.bzassesment.utils.AppConstant.SEARCH_RESULT_FOUND;
import static com.ar.bzassesment.utils.AppConstant.SEARCH_RESULT_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientsRepository ingredientsRepository;

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final InstructionsRepository instructionsRepository;

    private final RecipeMapper recipeMapper;

    private final IngredientMapper ingredientMapper;

    private final RecipeIngredientsMapper recipeIngredientsMapper;

    //Method to create new recipe
    public void createRecipe(RecipeForm recipeForm) throws ExecutionException {
        try {
            //Convert form object to entity
            Recipe recipe = recipeMapper.formToRecipe(recipeForm, Optional.empty());
            //Convert form to ingredients entity
            List<Ingredients> ingredients = this.constructIngredients(recipeForm);
            //Convert form to ReceipeIngredient entity
            List<RecipeIngredients> recipeIngredients = recipeIngredientsMapper.getRecipeIngredients(recipe, ingredients);
            recipeRepository.save(recipe);
            ingredientsRepository.saveAll(ingredients);
            recipeIngredientsRepository.saveAll(recipeIngredients);
        } catch (Exception ex) {
            log.error("Exception occurred while creating recipe, exception message {}", ex.getMessage(), ex);
            throw new ExecutionException("Exception occurred while creating recipe, please try again later");
        }
    }

    //Method to update recipe
    public void update(RecipeForm recipeForm) throws ExecutionException {
        try {
            Recipe recipe = recipeMapper.formToRecipe(recipeForm, this.getRecipe(recipeForm.getId()));
            List<Ingredients> ingredients = this.constructIngredients(recipeForm);
            List<RecipeIngredients> recipeIngredients = recipeIngredientsMapper.getRecipeIngredients(recipe, ingredients);
            recipeRepository.save(recipe);
            ingredientsRepository.saveAll(ingredients);
            recipeIngredientsRepository.saveAll(recipeIngredients);
        } catch (Exception ex) {
            log.error("Exception occurred while creating recipe, exception message {}", ex.getMessage(), ex);
            throw new ExecutionException("Exception occurred while creating recipe, please try again later");
        }
    }

    //Method to load all recipes
    public RecipeResponseFormList load() {
        List<Recipe> recipes = this.recipeRepository.findAll();

        List<RecipeResponseForm> recipeResponseForms = new ArrayList<>();
        recipes.stream().forEach(recipe -> {
            RecipeResponseForm recipeResponseForm = this.recipeMapper.entityToRecipeForm(recipe);
            //set recipe instructions
            this.setInstructions(recipe.getId(), recipeResponseForm);
            //set ingredients
            this.getIngredientsForRecipe(recipe.getId(), recipeResponseForm);
            recipeResponseForms.add(recipeResponseForm);
        });
        String message = (recipeResponseForms.isEmpty()) ? SEARCH_RESULT_NOT_FOUND : SEARCH_RESULT_FOUND;
        return new RecipeResponseFormList(recipeResponseForms, message);
    }

    //Method to fetch recipe using id
    public RecipeResponseForm get(Integer recipeId) {
        Optional<Recipe> recipeOptional = this.getRecipe(recipeId);
        if (!recipeOptional.isPresent()) {
            return null;
        }
        RecipeResponseForm recipeResponseForm = this.recipeMapper.entityToRecipeForm(recipeOptional.get());
        //set instructions
        this.setInstructions(recipeId, recipeResponseForm);
        //set ingredients
        this.getIngredientsForRecipe(recipeId, recipeResponseForm);
        return recipeResponseForm;
    }

    private void setInstructions(Integer recipeId, RecipeResponseForm recipeResponseForm) {
        List<Instructions> instructionsList = this.instructionsRepository.findByRecipeId(recipeId);
        recipeResponseForm.setInstructions(AppUtils.getInstructionsAsString(instructionsList));
    }

    //Method to fetch ingredients using recipe id
    private void getIngredientsForRecipe(Integer recipeId, RecipeResponseForm recipeResponseForm) {
        List<String> ingredientsList = this.ingredientsRepository.findByRecipeId(recipeId);
        recipeResponseForm.getIngredients().addAll(ingredientsList);
    }

    //Method to delete recipe by id
    public void deleteRecipeById(Integer recipeId) {
        //Delete data from RecipeIngredients table
        this.recipeIngredientsRepository.deleteByRecipeId(recipeId);
        //Delete instructions
        this.instructionsRepository.deleteByRecipeId(recipeId);
        //Delete from Recipe table
        this.recipeRepository.deleteById(recipeId);
    }

    public void validateRecipeForm(RecipeForm recipeForm, Boolean isNewFlag) throws ExecutionException {
        //Validate recipe id exists or not
        if (!isNewFlag && !this.getRecipe(recipeForm.getId()).isPresent()) {
            throw new ExecutionException("Recipe not found");
        }
        //Validate recipe name
    }

    public void validateRecipe(Integer id) throws ExecutionException {
        if (!this.getRecipe(id).isPresent()) {
            throw new ExecutionException("Recipe not found");
        }
    }

    //Run find by id query
    private Optional<Recipe> getRecipe(Integer recipeId) {
        return this.recipeRepository.findById(recipeId);
    }

    //Converting receipeform data to Ingrdients object
    private List<Ingredients> constructIngredients(RecipeForm recipeForm) {
        return recipeForm.getIngredients().stream()
                .map(ingredient -> this.checkIngredientExistsOrNot(ingredient, recipeForm.getUserId()))
                .collect(Collectors.toList());

    }

    private Ingredients checkIngredientExistsOrNot(String name, String userId) {
        Optional<Ingredients> ingredientsOptional = this.ingredientsRepository.findByName(name);
        return (ingredientsOptional.isPresent()) ?
                ingredientsOptional.get() : this.ingredientMapper.getIngredients(name, userId);
    }
}
