package com.ar.bzassesment.service;

import com.ar.bzassesment.dao.entity.Ingredients;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientsRepository ingredientsRepository;

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Mock
    private InstructionsRepository instructionsRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private IngredientMapper ingredientMapper;

    @Mock
    private RecipeIngredientsMapper recipeIngredientsMapper;

    @InjectMocks
    private RecipeService recipeService;

    @Before
    public void setup(){
        recipeService = new RecipeService(recipeRepository, ingredientsRepository, recipeIngredientsRepository, instructionsRepository, recipeMapper, ingredientMapper, recipeIngredientsMapper);
        MockitoAnnotations.initMocks(recipeService);
    }

    //@Test
    public void testCreateRecipe() throws ExecutionException {
        // Mock data
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setUserId("user1");
        recipeForm.setIngredients(Arrays.asList("Ingredient1", "Ingredient2"));

        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1);

        Ingredients mockIngredient1 = new Ingredients();
        mockIngredient1.setId(1);
        mockIngredient1.setName("Ingredient1");

        Ingredients mockIngredient2 = new Ingredients();
        mockIngredient2.setId(2);
        mockIngredient2.setName("Ingredient2");

        when(recipeMapper.formToRecipe(any(), any())).thenReturn(mockRecipe);
        when(recipeIngredientsMapper.getRecipeIngredients(any(), any())).thenReturn(Arrays.asList(
                new RecipeIngredients(), new RecipeIngredients()
        ));
        when(ingredientsRepository.findByName(any())).thenReturn(Optional.of(mockIngredient1));

        // Perform the test
        assertDoesNotThrow(() -> recipeService.createRecipe(recipeForm));

        // Verify that the repositories were called
        Mockito.verify(recipeRepository, Mockito.times(1)).save(any());
        Mockito.verify(ingredientsRepository, Mockito.times(2)).saveAll(any());
        Mockito.verify(recipeIngredientsRepository, Mockito.times(1)).saveAll(any());
    }

    @Test
    public void testUpdate() throws ExecutionException {
        // Mock data
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setId(1);
        recipeForm.setUserId("user1");
        recipeForm.setIngredients(Arrays.asList("Ingredient1", "Ingredient2"));

        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1);

        Ingredients mockIngredient1 = new Ingredients();
        mockIngredient1.setId(1);
        mockIngredient1.setName("Ingredient1");

        Ingredients mockIngredient2 = new Ingredients();
        mockIngredient2.setId(2);
        mockIngredient2.setName("Ingredient2");

        when(recipeMapper.formToRecipe(any(), any())).thenReturn(mockRecipe);
        when(recipeIngredientsMapper.getRecipeIngredients(any(), any())).thenReturn(Arrays.asList(
                new RecipeIngredients(), new RecipeIngredients()
        ));
        when(ingredientsRepository.findByName(any())).thenReturn(Optional.of(mockIngredient1));

        // Perform the test
        assertDoesNotThrow(() -> recipeService.update(recipeForm));

        // Verify that the repositories were called
        Mockito.verify(recipeRepository, Mockito.times(1)).save(any());
        Mockito.verify(ingredientsRepository, Mockito.times(2)).saveAll(any());
        Mockito.verify(recipeIngredientsRepository, Mockito.times(1)).saveAll(any());
    }

    @Test
    public void testLoad() {
        // Mock data
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1);
        mockRecipe.setName("Mock Recipe");

        when(recipeRepository.findAll()).thenReturn(Collections.singletonList(mockRecipe));
        when(recipeMapper.entityToRecipeForm(any())).thenReturn(new RecipeResponseForm());

        // Perform the test
        RecipeResponseFormList result = recipeService.load();

        // Verify the result
        assertEquals(1, result.getRecipeResponseForms().size());
        assertEquals("Mock Recipe", result.getRecipeResponseForms().get(0).getRecipeName());
        assertEquals("Search result found", result.getMessage());
    }

    @Test
    public void testGet() {
        // Mock data
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1);
        mockRecipe.setName("Mock Recipe");

        when(recipeRepository.findById(any())).thenReturn(Optional.of(mockRecipe));
        when(recipeMapper.entityToRecipeForm(any())).thenReturn(new RecipeResponseForm());

        // Perform the test
        RecipeResponseForm result = recipeService.get(1);

        // Verify the result
        assertNotNull(result);
        assertEquals("Mock Recipe", result.getRecipeName());
    }

    @Test
    public void testDeleteRecipeById() {
        // Perform the test
        assertDoesNotThrow(() -> recipeService.deleteRecipeById(1));

        // Verify that the repositories were called
        Mockito.verify(recipeIngredientsRepository, Mockito.times(1)).deleteByRecipeId(anyInt());
        Mockito.verify(instructionsRepository, Mockito.times(1)).deleteByRecipeId(anyInt());
        Mockito.verify(recipeRepository, Mockito.times(1)).deleteById(anyInt());
    }
}
