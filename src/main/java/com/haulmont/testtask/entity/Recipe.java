package com.haulmont.testtask.entity;

import java.sql.Date;

//класс сущности "Рецепт"
public class Recipe {

    private long id;
    private String description;
    private long patientID;
    private long doctorID;
    private Date dataOfCreation;
    private int validity;
    private Priority priority;
    private enum Priority {Нормальный, Срочный, Немедленный}

    public Recipe(String description, long recipeID, long doctorID, Date dataOfCreation, int validity, String priority) {
        this.description = description;
        this.patientID = recipeID;
        this.doctorID = doctorID;
        this.dataOfCreation = dataOfCreation;
        this.validity = validity;
        this.priority = Priority.valueOf(priority);
    }

    public Recipe() {
    }

    public Recipe(Recipe recipe) {
        try {
            this.id = recipe.getId();
            this.description = recipe.getDescription();
            this.patientID = recipe.getPatientID();
            this.doctorID = recipe.getDoctorID();
            this.dataOfCreation = recipe.getDataOfCreation();
            this.validity = recipe.getValidity();
            this.priority = Priority.valueOf(recipe.getPriority());
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: Некорректный приоритет рецепта!");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public long getPatientID() {
        return this.patientID;
    }

    public void setPatientID(long newClientID) {
        this.patientID = newClientID;
    }

    public long getDoctorID() { return doctorID; }

    public void setDoctorID(long doctorID) { this.doctorID = doctorID; }

    public Date getDataOfCreation() {
        return this.dataOfCreation;
    }

    public void setDataOfCreation(Date newDataOfCreation) {
        this.dataOfCreation = newDataOfCreation;
    }

    public int getValidity() {
        return this.validity;
    }

    public void setValidity(int newValidity) {
        this.validity = newValidity;
    }

    public String getPriority() { return this.priority.toString(); }

    public void setPriority(String priority) { this.priority = Priority.valueOf(priority); }
}

