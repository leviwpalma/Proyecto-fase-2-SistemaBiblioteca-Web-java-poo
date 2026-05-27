/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.biblioteca.datos;

import com.biblioteca.utils.Conexion;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Método unificado por si se requiere procesamiento extra
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirección de seguridad al login si intentan entrar por GET directo
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String usuarioForm = request.getParameter("txtUsuario").trim();
        String claveForm = request.getParameter("txtClave").trim();
        
        // CONSULTA SINCRONIZADA: Mapea directamente con carnet_codigo y password de  MySQL
        String sql = "SELECT nombre, rol FROM usuarios WHERE carnet_codigo = ? AND password = ?";
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, usuarioForm);
            ps.setString(2, claveForm);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String rol = rs.getString("rol");
                    
                    // Creación del espacio de sesión para almacenar las credenciales en memoria web
                    HttpSession sesion = request.getSession();
                    sesion.setAttribute("usuarioLogueado", nombre);
                    sesion.setAttribute("rolUsuario", rol);
                    
                    // VALIDACIÓN DE ROL: Conectado con tu ENUM de base de datos
                    if (rol.equalsIgnoreCase("Administrador")) {
                        response.sendRedirect("menuEncargado.jsp");
                    } else {
                        // Si es Alumno o Profesor, los enviamos a la consulta general
                        response.sendRedirect("index.jsp");
                    }
                    
                } else {
                    // Mensaje de advertencia si fallan los datos de acceso
                    request.setAttribute("error", "Credenciales incorrectas. Verifique usuario y clave.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
            
        } catch (SQLException e) {
            // Captura de errores de infraestructura de base de datos
            request.setAttribute("error", "Error de conexión con la base de datos: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Controlador de Autenticación por Roles - Mediateca UDB";
    }
}