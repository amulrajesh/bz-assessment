package com.ar.bzassesment.controller;

import com.ar.bzassesment.exception.ExecutionException;
import com.ar.bzassesment.model.RecipeForm;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.model.RecipeResponseFormList;
import com.ar.bzassesment.service.RecipeSearchService;
import com.ar.bzassesment.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@Api(value = "Recipe API", tags = "Recipe Controller", description = "Create, update, delete, list, search recipes")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class RecipeController extends BaseController {

    private final RecipeService recipeService;

    private final RecipeSearchService recipeSearchService;
    private static final String RECIPE_URL = BASE_URL + "/recipe";

    @GetMapping(RECIPE_URL)
    @Operation(summary = "Get recipes", description = "Get the complete list of recipes")
    public RecipeResponseFormList listRecipes(Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} initiated load all recipe", userName);
        return recipeService.load();
    }

    @GetMapping(RECIPE_URL + "/{id}")
    @Operation(summary = "Get a recipe by ID",
            description = "Get the recipe details by a given ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The found recipe ",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeResponseForm.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Recipe not found")
            })
    public ResponseEntity<RecipeResponseForm> getRecipe(@PathVariable("id") Integer recipeId, Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} initiated search request for recipe id {}", userName, recipeId);
        final RecipeResponseForm recipeResponseForm = recipeService.get(recipeId);
        if (recipeResponseForm == null) {
            log.info("User {} completed search request for recipe id {} and data not found", userName, recipeId);
            return ResponseEntity.notFound().build();
        }
        log.info("User {} completed search request for recipe id {} and data found", userName, recipeId);
        return ResponseEntity.ok(recipeResponseForm);
    }

    // Save operation
    @PostMapping(value = RECIPE_URL)
    @ApiKeyAuthDefinition(key = "Authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER, name = "Bearer")
    @Operation(summary = "Create a new recipe",
            description = "Creates a new recipe and returns the created recipe",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The created recipe ",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeForm.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request when invalid recipe data ",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeForm.class))
                    )
            })
    public ResponseEntity<String> saveRecipe(@ApiParam(value = "Properties of the recipe", required = true)
                                             @Valid @RequestBody RecipeForm recipeForm, Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} creating new recipe", userName);
        recipeForm.setUserId(userName);
        try {
            recipeService.createRecipe(recipeForm);
        log.info("User {} create new recipe", userName);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Recipe created successfully.");
        } catch (ExecutionException ex) {
        log.info("User {} exception occured while creating new recipe", userName);
            return ResponseEntity.ok().body(ex.getMessage());
        }
    }

    @PutMapping(RECIPE_URL + "/{id}")
    @ApiKeyAuthDefinition(key = "Authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER, name = "Bearer")
    @Operation(summary = "Update an recipe",
            description = "Updates an existing recipe with the given recipe details",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The found recipe ",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid recipe data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Recipe not found")
            })
    public ResponseEntity<String> updateRecipe(@PathVariable("id") Integer recipeId, @RequestBody RecipeForm recipeForm, Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} updating the recipe id {}", userName, recipeId);
        recipeForm.setUserId(userName);
        RecipeResponseForm recipeResponseForm = recipeService.get(recipeId);
        if (recipeResponseForm == null) {
            return ResponseEntity.notFound().build();
        }
        //Set id in recipeForm
        recipeForm.setId(recipeId);
        try {
            recipeService.update(recipeForm);
        } catch (ExecutionException e) {
            log.error("Exception occurred while updating recipe, please try again later, error message {}", e.getMessage(), e);
        log.info("User {} excepiton occurred while updating the recipe id {}", userName, recipeId);
            return ResponseEntity.badRequest().body("Invalid recipe details");
        }
        log.info("User {} updated the recipe id {}", userName, recipeId);
        return ResponseEntity.ok("Recipe updated successfully");
    }


    // Delete operation
    @DeleteMapping(RECIPE_URL + "/{id}")
    @ApiKeyAuthDefinition(key = "Authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER, name = "Bearer")
    @Operation(summary = "Delete an recipe by recipe id",
            description = "Deletes an recipe by the given recipe id",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "204", description = "When the given recipe is deleted"),
                    @ApiResponse(responseCode = "404", description = "Recipe not found")
            })
    public ResponseEntity<?> deleteRecipeById(@PathVariable("id") Integer recipeId, Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} delting the recipe id {}", userName, recipeId);
        final RecipeResponseForm recipeResponseForm = recipeService.get(recipeId);
        if (recipeResponseForm == null) {
            return ResponseEntity.notFound().build();
        }
        recipeService.deleteRecipeById(recipeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(RECIPE_URL + "/search")
    @Operation(summary = "Search recipes",
            description = "Filter available recipes based on one or more of the following criteria:\n" +
                    "1. Whether or not the dish is vegetarian\n" +
                    "2. The number of servings\n" +
                    "3. Specific ingredients (either include or exclude)\n" +
                    "4. Text search within the instructions.\n",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of recipes based on the given criteria")
            })
    public ResponseEntity<RecipeResponseFormList> searchRecipes(@Parameter(description = "String for vegetarian dishes") @RequestParam(name = "type", required = false) String type,
                                                                @Parameter(description = "minimum number of servings") @RequestParam(name = "minServings", required = false) Integer minServings,
                                                                @Parameter(description = "maximum number of servings") @RequestParam(name = "maxServings", required = false) Integer maxServings,
                                                                @Parameter(description = "comma seperated list of ingredients to be included in the dish") @DefaultValue("") @RequestParam(name = "includedIngredients", required = false) String includedIngredients,
                                                                @Parameter(description = "comma seperated list of ingredients not to be excluded in the dish") @DefaultValue("") @RequestParam(name = "excludedIngredients", required = false) String excludedIngredients,
                                                                @Parameter(description = "word to be found in the instructions") @RequestParam(name = "queryInstructions", required = false) String queryInstructions, Principal principal) {
        String userName = this.getUserName(principal);
        log.info("User {} initiated search request", userName);
        final RecipeResponseFormList recipes = recipeSearchService.search(type,
                queryInstructions != null ? this.strToList(queryInstructions) : null,
                includedIngredients != null ? this.strToList(includedIngredients) : null,
                excludedIngredients != null ? this.strToList(excludedIngredients) : null,
                minServings, maxServings);
        return ResponseEntity.ok(recipes);
    }

    private List<String> strToList(String data) {
        return Stream.of(data.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }


}
