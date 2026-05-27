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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if (accion.equals("listar")) {
                listarUsuarios(request, response);
            } else if (accion.equals("eliminar")) {
                eliminarUsuario(request, response);
            } else if (accion.equals("editar")) {
                cargarUsuarioParaEditar(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // LISTAR USUARIOS
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        StringBuilder html = new StringBuilder();
        String sql = "SELECT id_usuario, carnet_codigo, nombre, password, rol, mora_acumulada FROM usuarios ORDER BY id_usuario DESC";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            html.append("<table>")
                .append("<tr><th>ID</th><th>Carnet</th><th>Nombre Completo</th><th>Rol</th><th>Mora</th><th>Acciones</th></tr>");
            
            while (rs.next()) {
                html.append("<tr>")
                    .append("<td>").append(rs.getInt("id_usuario")).append("</td>")
                    .append("<td><strong>").append(rs.getString("carnet_codigo")).append("</strong></td>")
                    .append("<td>").append(rs.getString("nombre")).append("</td>")
                    .append("<td>").append(rs.getString("rol")).append("</td>")
                    .append("<td>$").append(rs.getDouble("mora_acumulada")).append("</td>")
                    .append("<td>")
                    .append("<a href='UsuarioServlet?accion=editar&id=").append(rs.getInt("id_usuario")).append("' class='btn-editar'>Editar</a>")
                    .append("<a href='UsuarioServlet?accion=eliminar&id=").append(rs.getInt("id_usuario")).append("' class='btn-eliminar' onclick='return confirm(\"¿Estás seguro de eliminar este usuario?\")'>Eliminar</a>")
                    .append("</td></tr>");
            }
            html.append("</table>");
        }
        request.setAttribute("tablaUsuariosHtml", html.toString());
        request.getRequestDispatcher("gestionUsuarios.jsp").forward(request, response);
    }

    // ELIMINAR CON CONTROL DE LLAVE FORÁNEA
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("id"));
        
        // Verificar primero si tiene préstamos amarrados
        String sqlVerificar = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ?";
        String sqlDelete = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection cn = Conexion.getConexion()) {
            try (PreparedStatement psVerificar = cn.prepareStatement(sqlVerificar)) {
                psVerificar.setInt(1, idUsuario);
                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        request.setAttribute("error", "No se puede eliminar: El usuario posee un historial de préstamos activo en el sistema.");
                        listarUsuarios(request, response);
                        return;
                    }
                }
            }
            // Si está libre, se borra
            try (PreparedStatement psDelete = cn.prepareStatement(sqlDelete)) {
                psDelete.setInt(1, idUsuario);
                psDelete.executeUpdate();
            }
        }
        response.sendRedirect("UsuarioServlet?accion=listar");
    }

    // CARGAR DATOS EN EL FORMULARIO PARA EDITAR
    private void cargarUsuarioParaEditar(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("id"));
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("id_usuario", rs.getInt("id_usuario"));
                    request.setAttribute("carnet_codigo", rs.getString("carnet_codigo"));
                    request.setAttribute("nombre", rs.getString("nombre"));
                    request.setAttribute("password", rs.getString("password"));
                    request.setAttribute("rol", rs.getString("rol"));
                    request.setAttribute("mora_acumulada", rs.getDouble("mora_acumulada"));
                }
            }
        }
        request.getRequestDispatcher("registroUsuario.jsp").forward(request, response);
    }

    //  PROCESAR GUARDADO Y ACTUALIZACIÓN 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String idParam = request.getParameter("id");
        String carnet = request.getParameter("txtCarnet").trim();
        String nombre = request.getParameter("txtNombre").trim();
        String password = request.getParameter("txtPassword").trim();
        String rol = request.getParameter("cmbRol");
        double mora = Double.parseDouble(request.getParameter("txtMora"));

        try (Connection cn = Conexion.getConexion()) {
            if (idParam == null || idParam.isEmpty()) {
                // ACCIÓN: INSERTAR (Validando que el carnet no exista ya)
                String sqlCheck = "SELECT COUNT(*) FROM usuarios WHERE carnet_codigo = ?";
                try (PreparedStatement psCheck = cn.prepareStatement(sqlCheck)) {
                    psCheck.setString(1, carnet);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            request.setAttribute("error", "El carnet '" + carnet + "' ya está registrado por otro usuario.");
                            request.getRequestDispatcher("registroUsuario.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                String sqlInsert = "INSERT INTO usuarios (carnet_codigo, nombre, password, rol, mora_acumulada) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psIns = cn.prepareStatement(sqlInsert)) {
                    psIns.setString(1, carnet);
                    psIns.setString(2, nombre);
                    psIns.setString(3, password);
                    psIns.setString(4, rol);
                    psIns.setDouble(5, mora);
                    psIns.executeUpdate();
                    request.setAttribute("exito", "¡Usuario guardado con éxito!");
                }
            } else {
                // ACCIÓN: ACTUALIZAR
                int idUsuario = Integer.parseInt(idParam);
                String sqlUpdate = "UPDATE usuarios SET nombre = ?, password = ?, rol = ?, mora_acumulada = ? WHERE id_usuario = ?";
                try (PreparedStatement psUp = cn.prepareStatement(sqlUpdate)) {
                    psUp.setString(1, nombre);
                    psUp.setString(2, password);
                    psUp.setString(3, rol);
                    psUp.setDouble(4, mora);
                    psUp.setInt(5, idUsuario);
                    psUp.executeUpdate();
                    request.setAttribute("exito", "¡Datos actualizados con éxito!");
                    
                    // Recargar datos actualizados en la vista
                    request.setAttribute("id_usuario", idUsuario);
                    request.setAttribute("carnet_codigo", carnet);
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("password", password);
                    request.setAttribute("rol", rol);
                    request.setAttribute("mora_acumulada", mora);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error en Base de Datos: " + e.getMessage());
        }
        request.getRequestDispatcher("registroUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}