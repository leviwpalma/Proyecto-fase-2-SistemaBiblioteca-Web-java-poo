/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //  nombre exacto de la base de datos y contraseña
    private static final String DATABASE = "biblioteca_db"; 
    private static final String PASSWORD = "Admin123"; 
    
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConexion() {
        Connection cn = null;
        try {
            Class.forName(DRIVER);
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa con MySQL Workbench!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el Driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return cn;
    }
}