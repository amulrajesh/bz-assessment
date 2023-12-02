package com.ar.bzassesment.dao.repo;

import com.ar.bzassesment.dao.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredientsRepository extends JpaRepository<Ingredients, Integer>, JpaSpecificationExecutor<Ingredients> {
    Optional<Ingredients> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT ingredients.NAME FROM Ingredients ingredients " +
            " inner join RECIPE_INGREDIENTS recipe_ingredients ON ingredients.ID=recipe_ingredients.INGREDIENTS_ID " +
            " WHERE recipe_ingredients.RECIPE_ID = :recipeId")
    List<String> findByRecipeId(Integer recipeId);
}
