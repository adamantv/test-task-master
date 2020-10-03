package com.haulmont.testtask.dao;

import java.sql.*;

//класс для взаимодействия с БД (подключение, отключение), осуществление доступа с помощью JDBC
public class Database {

    public static Connection connection;
    public static Statement  statement;

    static public void startDatabase(){
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Не удалось загрузить драйвер HSQLDB");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:hsqldb:file:src/main/database/hospital", "SA", "");
            statement  = connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД");
            e.printStackTrace();
            System.exit(2);
        }
    }

   static  public void closeDatabase(){
        try {
            String query = "SHUTDOWN";
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("БД не закрыта");
        }
    }

}
