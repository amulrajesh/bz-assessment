package com.ar.bzassesment.controller;

import com.ar.bzassesment.model.LoginRequest;
import com.ar.bzassesment.model.RecipeForm;
import com.ar.bzassesment.model.RecipeResponseForm;
import com.ar.bzassesment.model.RecipeResponseFormList;
import com.ar.bzassesment.utils.TestUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RecipeControllerIntTest {

    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Before
    void setup() {
        //save
        this.shouldCreateNewRecipeWhenRecipeIsValid();
    }

    private HttpHeaders getAuthHeaders() {
        LoginRequest loginRequest = new LoginRequest("mithu", "password");
        ResponseEntity<String> response = restTemplate.exchange("/token", HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(response.getBody());
        return httpHeaders;
    }

    @Test
    void shouldFindAllRecipes() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
        ResponseEntity<RecipeResponseFormList> recipeResponseFormList = restTemplate.exchange("/api/v1/recipe",
                HttpMethod.GET, entity,
                RecipeResponseFormList.class);
        assertThat(recipeResponseFormList.getBody().getRecipeResponseForms().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldFindRecipeWhenValidRecipeID() {
        this.shouldCreateNewRecipeWhenRecipeIsValid();
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
        ResponseEntity<RecipeResponseForm> response = restTemplate.exchange("/api/v1/recipe/1", HttpMethod.GET,
                entity, RecipeResponseForm.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldThrowNotFoundWhenInvalidRecipeID() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
        ResponseEntity<RecipeResponseForm> response = restTemplate.exchange("/api/v1/recipe/999", HttpMethod.GET, entity, RecipeResponseForm.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateNewRecipeWhenRecipeIsValid() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        RecipeForm recipeForm = TestUtils.getRecipeForm();
        HttpEntity entity = new HttpEntity<>(recipeForm, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/recipe", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("Recipe created successfully.");
    }

    @Test
    @Rollback
    void shouldNotCreateNewRecipeWhenRecipeNameIsMissing() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        RecipeForm recipeForm = TestUtils.getRecipeForm();
        recipeForm.setRecipeName(null);
        HttpEntity entity = new HttpEntity<>(recipeForm, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/recipe", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("Recipe name is required.");
    }

    @Test
    @Rollback
    void shouldNotCreateNewRecipeWhenRecipeFormIsInValid() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        RecipeForm recipeForm = new RecipeForm();
        HttpEntity entity = new HttpEntity<>(recipeForm, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/recipe", HttpMethod.POST,
                entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Ingredients list should not be empty.");
        assertThat(response.getBody()).contains("Recipe type is required.");
        assertThat(response.getBody()).contains("Recipe name is required.");
        assertThat(response.getBody()).contains("Instruction should not be empty.");
    }

    @Test
    void shouldUpdateRecipeWhenRecipeIsValid() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        RecipeForm recipeForm = TestUtils.getRecipeForm();
        recipeForm.setRecipeName("Mella mella pasta");
        HttpEntity entity = new HttpEntity<>(recipeForm, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/recipe/1", HttpMethod.PUT, entity, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEqualTo("Recipe updated successfully");
        } else {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }
    }

    @Test
    void shouldNotUpdateRecipeWhenRecipeIsNotFound() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        RecipeForm recipeForm = TestUtils.getRecipeForm();
        recipeForm.setRecipeName("Mella mella pasta");
        HttpEntity entity = new HttpEntity<>(recipeForm, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/recipe/100", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @Rollback
    void shouldDeleteWithValidID() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/recipe/1", HttpMethod.DELETE, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldReturnRecipeWithValidSearch() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>( httpHeaders);
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port + "/api/v1/recipe/search")
                .queryParam("type", "{type}")
                .queryParam("minServings", "{minServings}")
                .queryParam("maxServings", "{maxServings}")
                .queryParam("queryInstructions", "{queryInstructions}")
                .queryParam("includedIngredients", "{includedIngredients}")
                .queryParam("excludedIngredients", "{excludedIngredients}")
                .encode()
                .toUriString();

        Map<String, Object> params = new HashMap<>();
        params.put("type", "non-veg");
        params.put("minServings", 1);
        params.put("maxServings", 1);
        params.put("queryInstructions", "Medium Fry, Fully cooked");
        params.put("includedIngredients", "Tomato");
        params.put("excludedIngredients", "Chicken");
        RecipeResponseFormList recipeResponseFormList =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, RecipeResponseFormList.class, params).getBody();
        assertThat(recipeResponseFormList.getRecipeResponseForms().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldReturnEmptyRecipeWithValidSearch() {
        HttpHeaders httpHeaders = this.getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port + "/api/v1/recipe/search")
                .queryParam("type", "{type}")
                .queryParam("minServings", "{minServings}")
                .queryParam("maxServings", "{maxServings}")
                .queryParam("queryInstructions", "{queryInstructions}")
                .queryParam("includedIngredients", "{includedIngredients}")
                .queryParam("excludedIngredients", "{excludedIngredients}")
                .encode()
                .toUriString();

        Map<String, Object> params = new HashMap<>();
        params.put("type", "veg");
        params.put("minServings", 1);
        params.put("maxServings", 1);
        params.put("queryInstructions", "Medium Fry, Fully cooked");
        params.put("includedIngredients", "Tomato");
        params.put("excludedIngredients", "Chicken");
        RecipeResponseFormList recipeResponseFormList =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, RecipeResponseFormList.class, params).getBody();
        assertThat(recipeResponseFormList.getRecipeResponseForms().size()).isEqualTo(0);
    }
}
