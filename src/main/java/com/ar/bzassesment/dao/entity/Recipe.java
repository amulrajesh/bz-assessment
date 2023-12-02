package com.ar.bzassesment.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "RECIPE")
public class Recipe {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPE_SEQ_GENERATOR")
    @SequenceGenerator(name = "RECIPE_SEQ_GENERATOR", sequenceName = "RECIPE_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    Integer id;

    @Getter
    @Column(name = "NAME", nullable = false)
    private String name;

    @Getter
    @Column(name = "NO_OF_SERVINGS", nullable = false)
    private Integer noOfServings;

    @Getter
    @Column(name = "VEG", nullable = false)
    private char veg;

    @Getter
    @Column(name = "CRAT_TSP", nullable = false)
    private Timestamp cratTsp;

    @Getter
    @Column(name = "CRAT_ID", nullable = false)
    private String cratUserId;

    @Getter
    @Column(name = "UPD_TSP", nullable = false)
    private Timestamp updTsp;

    @Getter
    @Column(name = "UPD_ID", nullable = false)
    private String updUserId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Instructions> instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecipeIngredients> recipeIngredients;

    public Recipe(Integer id, String name, Integer noOfServings, List<Instructions> instructions, char veg,
                  Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.id = id;
        this.name = name;
        this.noOfServings = noOfServings;
        this.instructions = instructions;
        this.veg = veg;
        this.cratTsp = cratTsp;
        this.cratUserId = cratUserId;
        this.updTsp = updTsp;
        this.updUserId = updUserId;
    }


    public Recipe(String name, Integer noOfServings, List<Instructions> instructions, char veg,
                  Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.id = id;
        this.name = name;
        this.noOfServings = noOfServings;
        this.instructions = instructions;
        this.veg = veg;
        this.cratTsp = cratTsp;
        this.cratUserId = cratUserId;
        this.updTsp = updTsp;
        this.updUserId = updUserId;
    }

    public Recipe() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoOfServings(Integer noOfServings) {
        this.noOfServings = noOfServings;
    }

    public void setVeg(char veg) {
        this.veg = veg;
    }

    public void setCratTsp(Timestamp cratTsp) {
        this.cratTsp = cratTsp;
    }

    public void setCratUserId(String cratUserId) {
        this.cratUserId = cratUserId;
    }

    public void setUpdTsp(Timestamp updTsp) {
        this.updTsp = updTsp;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNoOfServings() {
        return noOfServings;
    }

    public char getVeg() {
        return veg;
    }

    public Timestamp getCratTsp() {
        return cratTsp;
    }

    public String getCratUserId() {
        return cratUserId;
    }

    public Timestamp getUpdTsp() {
        return updTsp;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public List<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Instructions> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instructions> instructions) {
        this.instructions = instructions;
    }
}
