package com.ar.bzassesment.service;

import com.ar.bzassesment.dao.entity.Instructions;
import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.dao.repo.InstructionsRepository;
import com.ar.bzassesment.dao.repo.RecipeRepository;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.model.RecipeResponseFormList;
import com.ar.bzassesment.utils.AppConstant;
import com.ar.bzassesment.utils.AppUtils;
import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ar.bzassesment.utils.AppConstant.*;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class RecipeSearchService {

    private final RecipeRepository recipeRepository;

    private final InstructionsRepository instructionsRepository;

    /**
     * Method to run search based on the input.
     * Using JPA Specification to create dynamic join query at runtime.
     * @param type - Veg Or Non-Veg
     * @param instructions - String contains cooking instructions, compared with like condition and lower case
     * @param includeIngredients - Ingredients should be present in the recipe
     * @param excludeIngredients - Ingredients should be not in recipe
     * @param minNoOfServings - Recipe should be served to minimum of person
     * @param maxNoOfServings - Recipe should be service maximum of persons
     * @return query result contains Recipe and Ingredients data
     */
    public RecipeResponseFormList search(String type, List<String> instructions, List<String> includeIngredients,
                                         List<String> excludeIngredients, Integer minNoOfServings,
                                         Integer maxNoOfServings) {
        Specification<Recipe> spec = Specification.where(null);

        //Query condition for recipe type
        if (type != null && !type.isEmpty()) {
            char vegType = type.equalsIgnoreCase("veg") ? AppConstant.CHAR_Y : AppConstant.CHAR_N;
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("veg"), vegType));
        }

        //Query condition for instruction
        if (instructions != null && !instructions.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.join("instructions")
                            .get("insSteps")
                            .in(instructions));
        }

        //If both Min and Max no of servings exists then use between condition
        //Else if only min use greater than logic
        //Else if only max use less than logic
        if (minNoOfServings != null && minNoOfServings > 0 && maxNoOfServings != null && maxNoOfServings > 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("noOfServings"), minNoOfServings, maxNoOfServings));
        } else if (minNoOfServings != null && minNoOfServings > 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("noOfServings"), minNoOfServings));
        } else if (maxNoOfServings != null && maxNoOfServings > 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("noOfServings"), maxNoOfServings));
        }

        //Using inner join for include ingredients
        if (includeIngredients != null && !includeIngredients.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.join("recipeIngredients")
                            .join("ingredients")
                            .get("name")
                            .in(includeIngredients));
        }

        //Using inner join with not condition
        if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.not(
                            root.join("recipeIngredients", JoinType.LEFT)
                                    .join("ingredients", JoinType.INNER)
                                    .get("name")
                                    .in(excludeIngredients)));
        }

        //Execute query
        List<Recipe> recipes = this.recipeRepository.findAll(spec);

        //Convert Recipe object to Form objects
        List<RecipeResponseForm> recipeResponseForms = recipes.stream()
                .map(recipe -> this.setRecipeResponseForm(recipe) )
                .collect(Collectors.toList());
        String message = (recipeResponseForms.isEmpty()) ? SEARCH_RESULT_NOT_FOUND : SEARCH_RESULT_FOUND;
        System.out.println("Output ----- " + recipeResponseForms);
        return new RecipeResponseFormList(recipeResponseForms, message);
    }

    private RecipeResponseForm setRecipeResponseForm(Recipe recipe) {
        RecipeResponseForm recipeResponseForm = new RecipeResponseForm();
        recipeResponseForm.setRecipeName(recipe.getName());
        recipeResponseForm.setType((recipe.getVeg() == CHAR_Y) ? "Veg" : "Non-Veg");
        recipeResponseForm.setNoOfServings(recipe.getNoOfServings());
        recipeResponseForm.setIngredients(this.getIngredientsList(recipe));
        recipeResponseForm.setInstructions(this.getInstructions(recipe.getId()));
        return  recipeResponseForm;
    }

    private String getInstructions(Integer recipeId) {
        List<Instructions> instructionsList = this.instructionsRepository.findByRecipeId(recipeId);
        return AppUtils.getInstructionsAsString(instructionsList);
    }

    /**
     * Method to fetch ingredient data and assign it in the response.
     * @param recipe - Recipe entity returned by the search query
     * @return list of ingredients for the recipe
     */
    private List<String> getIngredientsList(Recipe recipe) {
        return recipe.getRecipeIngredients().stream().map(ri ->ri.getIngredients().getName()).collect(Collectors.toList());
    }
}
