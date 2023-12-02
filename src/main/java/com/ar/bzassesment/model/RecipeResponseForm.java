package com.ar.bzassesment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseForm {
    private String recipeName;
    private Integer noOfServings;
    private String instructions;
    private String type;
    private List<String> ingredients = new ArrayList<>();
}
