package com.ar.bzassesment.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "INGREDIENTS")
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INGREDIENTS_SEQ_GENERATOR")
    @SequenceGenerator(name = "INGREDIENTS_SEQ_GENERATOR", sequenceName = "INGREDIENTS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CRAT_TSP", nullable = false)
    private Timestamp cratTsp;

    @Column(name = "CRAT_ID", nullable = false)
    private String cratUserId;

    @Column(name = "UPD_TSP", nullable = false)
    private Timestamp updTsp;

    @Column(name = "UPD_ID", nullable = false)
    private String updUserId;

    @OneToMany(mappedBy = "ingredients", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecipeIngredients> recipeIngredients;

    public Ingredients() {
    }

    public Ingredients(Integer id, String name, Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.id = id;
        this.name = name;
        this.cratTsp = cratTsp;
        this.cratUserId = cratUserId;
        this.updTsp = updTsp;
        this.updUserId = updUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCratTsp() {
        return cratTsp;
    }

    public void setCratTsp(Timestamp cratTsp) {
        this.cratTsp = cratTsp;
    }

    public String getCratUserId() {
        return cratUserId;
    }

    public void setCratUserId(String cratUserId) {
        this.cratUserId = cratUserId;
    }

    public Timestamp getUpdTsp() {
        return updTsp;
    }

    public void setUpdTsp(Timestamp updTsp) {
        this.updTsp = updTsp;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public List<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
