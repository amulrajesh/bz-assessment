package com.ar.bzassesment.dao.repo;

import com.ar.bzassesment.dao.entity.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Integer>, JpaSpecificationExecutor<RecipeIngredients> {
    void deleteByRecipeId(Integer id);
}
