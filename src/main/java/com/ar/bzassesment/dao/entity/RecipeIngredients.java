package com.ar.bzassesment.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "RECIPE_INGREDIENTS")
public class RecipeIngredients {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPE_INGREDIENTS_SEQ_GENERATOR")
    @SequenceGenerator(name = "RECIPE_INGREDIENTS_SEQ_GENERATOR", sequenceName = "RECIPE_INGREDIENTS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "INGREDIENTS_ID")
    Ingredients ingredients;

    public RecipeIngredients() {
    }

    public RecipeIngredients(Integer id, Recipe recipe, Ingredients ingredients) {
        this.id = id;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }
}
