package com.ar.bzassesment.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeForm {
    private Integer id;

    @NotBlank(message = "Recipe name is required.")
    @Size(max = 100, message = "The Recipe name should not exceed more than 100 characters")
    @ApiModelProperty(notes = "The name of the ingredient", example = "Potato")
    private String recipeName;

    @Min(value = 1, message = "The minimum no no of servings for recipe is 1.")
    private Integer noOfServings;

    @NotEmpty(message = "Instruction should not be empty.")
    private List<String> instructions = new ArrayList<>();

    private String userId;

    @NotBlank(message = "Recipe type is required.")
    private String type;

    @NotEmpty(message = "Ingredients list should not be empty.")
    private List<String> ingredients = new ArrayList<>();
}
