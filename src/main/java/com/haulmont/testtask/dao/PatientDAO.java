package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Patient;

import java.sql.*;
import java.util.*;

//класс DAO для сущности "Пациент"
public class PatientDAO {

    private static PreparedStatement ps;

    //добавление пациента в БД
    public static void addPatient(Patient patient) {
        try {
            String query = "INSERT INTO patient (surname, firstName, patronymic, number) VALUES (?,?,?,?)";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, patient.getSurname());
            ps.setString(2, patient.getFirstName());
            ps.setString(3, patient.getPatronymic());
            ps.setString(4, patient.getNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление пациента из БД
    public static int deletePatient(long id) {
        try {
            String query = "DELETE FROM patient WHERE id = ?";
            ps = Database.connection.prepareStatement(query);
            ps.setLong(1,id);
            ps.executeUpdate();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }

    //внесение изменений в данные пациента
    public static void updatePatient(Patient patient) {
        try {
            String query = "UPDATE patient SET SURNAME = ?, FIRSTNAME = ?, PATRONYMIC = ?, NUMBER = ? WHERE id = ?";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, patient.getSurname());
            ps.setString(2, patient.getFirstName());
            ps.setString(3, patient.getPatronymic());
            ps.setString(4, patient.getNumber());
            ps.setLong(5, patient.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //метод возвращеает список всех пациентов
    public static List<Patient> getAllPatient() {
        try {
            String query = "SELECT * FROM patient";
            ResultSet resultSet = Database.statement.executeQuery(query);
            List<Patient> patients = new ArrayList<>();
            Patient patient = new Patient();
            while (resultSet.next()) {
                patient.setId(resultSet.getLong(1));
                patient.setSurname(resultSet.getString(2));
                patient.setFirstName(resultSet.getString(3));
                patient.setPatronymic( resultSet.getString(4));
                patient.setNumber(resultSet.getString(5));
                patients.add(new Patient(patient));
            }
            return patients;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("В БД нет пациентов");
            List<Patient> patients = null;
            return patients;
        }
    }

    //метод находит пациента по его ключу (id) и возвращает данные пациента в виде строки
   public static  String findPatientById(long id) {
        List<Patient> patients = PatientDAO.getAllPatient();
        Iterator<Patient> iterator = patients.iterator();
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            if (patient.getId() == id) {
                return patient.toString();
            }
        }
        return null;
    }

}
