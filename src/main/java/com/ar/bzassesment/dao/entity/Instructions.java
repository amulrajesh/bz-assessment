package com.ar.bzassesment.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Table(name = "INSTRUCTIONS")
public class Instructions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUCTIONS_SEQ_GENERATOR")
    @SequenceGenerator(name = "INSTRUCTIONS_SEQ_GENERATOR", sequenceName = "INSTRUCTIONS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    Recipe recipe;

    @Getter
    @Column(name = "INS_STEPS", nullable = false)
    private String insSteps;

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

    public Instructions() {
    }

    public Instructions(String insSteps, Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.insSteps = insSteps;
        this.cratTsp = cratTsp;
        this.cratUserId = cratUserId;
        this.updTsp = updTsp;
        this.updUserId = updUserId;
    }

    public Instructions(Integer id, String insSteps, Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.id = id;
        this.insSteps = insSteps;
        this.cratTsp = cratTsp;
        this.cratUserId = cratUserId;
        this.updTsp = updTsp;
        this.updUserId = updUserId;
    }

    public Instructions(Integer id, Recipe recipe, String insSteps, Timestamp cratTsp, String cratUserId, Timestamp updTsp, String updUserId) {
        this.id = id;
        this.recipe = recipe;
        this.insSteps = insSteps;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getInsSteps() {
        return insSteps;
    }

    public void setInsSteps(String insSteps) {
        this.insSteps = insSteps;
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
}
