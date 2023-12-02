package com.ar.bzassesment.dao.repo;

import com.ar.bzassesment.dao.entity.Instructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructionsRepository extends JpaRepository<Instructions, Integer> {
    List<Instructions> findByRecipeId(Integer recipeId);

    void deleteByRecipeId(Integer recipeId);
}
