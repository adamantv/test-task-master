package com.haulmont.testtask.entity;

//класс сущности "Врач"
public class Doctor {

    private String surname;
    private String firstName;
    private String patronymic;
    private String specialization;
    private long   id;

    public Doctor(String surname, String firstName, String patronymic, String number){
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.specialization = number;
    }

    public Doctor() {
    }

    public Doctor(Doctor doctor) {
        this.surname = doctor.getSurname();
        this.firstName = doctor.getFirstName();
        this.patronymic = doctor.getPatronymic();
        this.specialization = doctor.getSpecialization();
        this.id = doctor.getId();
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String newFirstName){
        this.firstName = newFirstName;
    }

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String newSurname){
        this.surname = newSurname;
    }

    public String getPatronymic(){
        return this.patronymic;
    }

    public void setPatronymic(String newPatronymic){
        this.patronymic = newPatronymic;
    }

    public String getSpecialization(){
        return this.specialization;
    }

    public void setSpecialization(String newNumber){
        this.specialization = newNumber;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long newId){
        this.id = newId;
    }

    //переопределение метода toString() для корректного выведения инфомации о врачах в таблицу "Рецепты"
    @Override
    public String toString() {
        return surname + " " + firstName.charAt(0) + "." + patronymic.charAt(0) + ". (" + specialization + ")";
    }
}
