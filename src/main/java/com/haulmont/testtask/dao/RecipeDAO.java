package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Recipe;

import java.sql.*;
import java.util.*;

//класс DAO для сущности "Рецепт"
public class RecipeDAO {

    private static PreparedStatement ps;

    //добавление рецепта в БД
    public static void addRecipe(Recipe recipe){
        try {
            String query = "INSERT INTO recipes (description, patientID, doctorID, dataOfCreation, validity, priority) VALUES (?,?,?,?,?,?)";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, recipe.getDescription());
            ps.setLong(2, recipe.getPatientID());
            ps.setLong(3, recipe.getDoctorID());
            ps.setDate(4, recipe.getDataOfCreation());
            ps.setInt(5, recipe.getValidity());
            ps.setString(6, recipe.getPriority());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Рецепт не был добавлен в БД");
        }
    }

    //удаление рецепта из БД
    public static void deleteRecipe(long id) {
        try {
            String query = "DELETE FROM recipes WHERE id= ?";
            ps = Database.connection.prepareStatement(query);
            ps.setLong(1,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //внесение изменений в данные рецепта
    public static void updateRecipe(Recipe recipe){
        try {
            String query = "UPDATE recipes SET description = ?, patientID = ?, doctorID = ?, dataOfCreation = ?, validity = ?, priority= ? WHERE id = ?";
            ps = Database.connection.prepareStatement(query);
            ps.setString(1, recipe.getDescription());
            ps.setLong(2, recipe.getPatientID());
            ps.setLong(3, recipe.getDoctorID());
            ps.setDate(4, recipe.getDataOfCreation());
            ps.setInt(5, recipe.getValidity());
            ps.setString(6, recipe.getPriority());
            ps.setLong(7, recipe.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Рецепт не был исправлен в БД");
        }
    }

    //метод возвращеает список всех рецептов
    public static List<Recipe> getAllRecipe() {
        try {
            String query = "SELECT * FROM recipes";
            ResultSet resultSet = Database.statement.executeQuery(query);
            List<Recipe> recipes = new ArrayList<>();
            Recipe recipe = new Recipe();
            while (resultSet.next()) {
                recipe.setId(resultSet.getLong(1));
                recipe.setDescription(resultSet.getString(2));
                recipe.setPatientID(resultSet.getLong(3));
                recipe.setDoctorID(resultSet.getLong(4));
                recipe.setDataOfCreation(resultSet.getDate(5));
                recipe.setValidity(resultSet.getInt(6));
                recipe.setPriority(resultSet.getString(7));
                recipes.add(new Recipe(recipe));
            }
            return recipes;
        } catch (SQLException e) {
            System.out.println("В БД нет рецептов");
            List<Recipe> recipes = null;
            return recipes;
        }
    }
}
