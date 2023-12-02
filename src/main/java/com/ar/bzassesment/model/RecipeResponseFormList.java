package com.ar.bzassesment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseFormList {
    private List<RecipeResponseForm> recipeResponseForms = new ArrayList<>();
    private String message;
}
