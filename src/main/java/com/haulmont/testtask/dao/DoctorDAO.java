package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;

import java.sql.*;
import java.util.*;

//класс DAO для сущности "Врач"
public class DoctorDAO {

    private static PreparedStatement ps;

    //добавление врача в БД
    public static void addDoctor(Doctor doctor) {
        try {
            String query = "INSERT INTO doctor (surname, firstName, patronymic, specialization) VALUES (?,?,?,?)";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, doctor.getSurname());
            ps.setString(2, doctor.getFirstName());
            ps.setString(3, doctor.getPatronymic());
            ps.setString(4, doctor.getSpecialization());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление врача из БД
    public static int deleteDoctor(long id) {
        try {
            String query = "DELETE FROM doctor WHERE id = ?";
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

    //внесение изменений в данные врача
    public static void updateDoctor(Doctor doctor) {
        try {
            String query = "UPDATE doctor SET SURNAME = ?, FIRSTNAME = ?, PATRONYMIC = ?, SPECIALIZATION = ? WHERE id = ?";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, doctor.getSurname());
            ps.setString(2, doctor.getFirstName());
            ps.setString(3, doctor.getPatronymic());
            ps.setString(4, doctor.getSpecialization());
            ps.setLong(5, doctor.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //метод возвращеает список всех врачей
    public static List<Doctor> getAllDoctor() {
        try {
            String query = "SELECT * FROM doctor";
            ResultSet resultSet = Database.statement.executeQuery(query);
            List<Doctor> doctors = new ArrayList<>();
            Doctor doctor = new Doctor();
            while (resultSet.next()) {
                doctor.setId(resultSet.getLong(1));
                doctor.setSurname(resultSet.getString(2));
                doctor.setFirstName(resultSet.getString(3));
                doctor.setPatronymic( resultSet.getString(4));
                doctor.setSpecialization(resultSet.getString(5));
                doctors.add(new Doctor(doctor));
            }
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("В БД нет врачей");
            List<Doctor> doctors = null;
            return doctors;
        }
    }

    //метод находит врача по его ключу (id) и возвращает данные врача в виде строки
    public static String findDoctorById(long id) {
        List<Doctor> doctors = DoctorDAO.getAllDoctor();
        Iterator<Doctor> iterator = doctors.iterator();
        while (iterator.hasNext()) {
            Doctor doctor = iterator.next();
            if (doctor.getId() == id) {
                return doctor.toString();
            }
        }
        return null;
    }

}



