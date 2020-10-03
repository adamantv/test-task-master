package com.haulmont.testtask.entity;

//класс сущности "Пациент"
public class Patient {

    private String surname;
    private String firstName;
    private String patronymic;
    private String number;
    private long   id;

    public Patient(String surname, String firstName, String patronymic, String number) {
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.number = number;
    }

    public Patient() {
    }

    public Patient(Patient patient) {
        this.surname = patient.getSurname();
        this.firstName = patient.getFirstName();
        this.patronymic = patient.getPatronymic();
        this.number = patient.getNumber();
        this.id = patient.getId();
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

    public String getNumber(){
        return this.number;
    }

    public void setNumber(String newNumber){
        this.number = newNumber;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long newId){
        this.id = newId;
    }

    //переопределение метода toString() для корректного выведения инфомации о пациентах в таблицу "Рецепты"
    @Override
    public String toString() {
        return surname + ' ' + firstName.charAt(0) + '.' + patronymic.charAt(0) + '.';
    }
}
