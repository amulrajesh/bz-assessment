package com.ar.bzassesment.dao.repo;

import com.ar.bzassesment.dao.entity.Recipe;
import com.ar.bzassesment.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.save(TestUtils.getPastaRecipe());
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldReturnRecipeNamePasta() {
        Recipe recipe = recipeRepository.save(TestUtils.getPastaRecipe());
        Recipe recipe1 = recipeRepository.findById(recipe.getId()).orElseThrow();
        assertEquals("Pasta", recipe1.getName(), "Recipe name should be 'Pasta'");
    }

    @Test
    void shouldNotReturnRecipe() {
        Optional<Recipe> recipe = recipeRepository.findById(100);
        assertFalse(recipe.isPresent(), "Recipe should not be present");
    }

}
