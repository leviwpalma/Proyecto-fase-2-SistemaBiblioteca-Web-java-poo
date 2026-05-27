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

@WebServlet(name = "PrestamoServlet", urlPatterns = {"/PrestamoServlet"})
public class PrestamoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if (accion.equals("listar")) {
                listarPrestamos(request, response);
            } else if (accion.equals("devolver")) {
                devolverPrestamo(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    //  LISTAR PRÉSTAMOS ACTIVOS EN UNA TABLA HTML
    private void listarPrestamos(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        StringBuilder html = new StringBuilder();
        
        // Consulta relacional usando los identificadores 
        String sql = "SELECT p.id_prestamo, u.carnet_codigo, u.nombre, d.codigo_ejemplar, l.titulo, p.fecha_prestamo, p.fecha_devolucion_esperada " +
                     "FROM prestamos p " +
                     "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "INNER JOIN documentos d ON p.id_documento = d.id_documento " +
                     "INNER JOIN libros l ON d.id_documento = l.id_documento " +
                     "WHERE p.estado = 'Activo' " +
                     "ORDER BY p.id_prestamo DESC";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            html.append("<table>");
            html.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>Carnet</th>")
                .append("<th>Nombre Completo</th>")
                .append("<th>Cód. Ejemplar</th>")
                .append("<th>Título del Libro</th>")
                .append("<th>Fecha Préstamo</th>")
                .append("<th>Devolución Esperada</th>")
                .append("<th>Acción</th>")
                .append("</tr>");
            
            boolean tieneDatos = false;
            while (rs.next()) {
                tieneDatos = true;
                html.append("<tr>");
                html.append("<td>").append(rs.getInt("id_prestamo")).append("</td>");
                html.append("<td><strong>").append(rs.getString("carnet_codigo")).append("</strong></td>");
                html.append("<td>").append(rs.getString("nombre")).append("</td>");
                html.append("<td>").append(rs.getString("codigo_ejemplar")).append("</td>");
                html.append("<td>").append(rs.getString("titulo")).append("</td>");
                html.append("<td>").append(rs.getDate("fecha_prestamo")).append("</td>");
                html.append("<td>").append(rs.getDate("fecha_devolucion_esperada")).append("</td>");
                html.append("<td>");
                html.append("<a href='PrestamoServlet?accion=devolver&id=").append(rs.getInt("id_prestamo"))
                    .append("' class='btn-devolver' onclick='return confirm(\"¿Confirmar devolución de este ejemplar?\")'>Devolver</a>");
                html.append("</td>");
                html.append("</tr>");
            }
            
            if (!tieneDatos) {
                html.append("<tr><td colspan='8' style='text-align:center; color:#64748b;'>No hay préstamos activos en este momento.</td></tr>");
            }
            html.append("</table>");
        }
        
        request.setAttribute("tablaPrestamosHtml", html.toString());
        request.getRequestDispatcher("gestionPrestamos.jsp").forward(request, response);
    }

    //  PROCESAR LA DEVOLUCIÓN DE UN LIBRO
    private void devolverPrestamo(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        int idPrestamo = Integer.parseInt(request.getParameter("id"));
        
        String sqlBuscarDoc = "SELECT id_documento FROM prestamos WHERE id_prestamo = ?";
        String sqlUpdatePrestamo = "UPDATE prestamos SET estado = 'Devuelto', fecha_devolucion_real = CURDATE() WHERE id_prestamo = ?";
        String sqlSumarStock = "UPDATE libros SET ejemplares_disponibles = ejemplares_disponibles + 1 WHERE id_documento = ?";
        
        try (Connection cn = Conexion.getConexion()) {
            cn.setAutoCommit(false);
            int idDocumento = 0;
            
            // Buscar qué documento está amarrado al préstamo
            try (PreparedStatement psBuscar = cn.prepareStatement(sqlBuscarDoc)) {
                psBuscar.setInt(1, idPrestamo);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) idDocumento = rs.getInt("id_documento");
                }
            }
            
            // Modificar el estado del préstamo e incrementar el inventario
            try (PreparedStatement psPrestamo = cn.prepareStatement(sqlUpdatePrestamo);
                 PreparedStatement psStock = cn.prepareStatement(sqlSumarStock)) {
                
                psPrestamo.setInt(1, idPrestamo);
                psPrestamo.executeUpdate();
                
                if (idDocumento > 0) {
                    psStock.setInt(1, idDocumento);
                    psStock.executeUpdate();
                }
                
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
        response.sendRedirect("PrestamoServlet?accion=listar");
    }

    //  REGISTRAR NUEVO PRÉSTAMO CON VALIDACIONES DE REGLAS DE NEGOCIO 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String carnetInput = request.getParameter("txtCarnet").trim();
        String codigoEjemplarInput = request.getParameter("txtCodigoEjemplar").trim();
        int diasSolicitados = Integer.parseInt(request.getParameter("txtDias"));
        
        Connection cn = null;
        
        try {
            cn = Conexion.getConexion();
            
            // REGLA 1: Validación de Datos del Usuario y Mora
            String sqlUsuario = "SELECT id_usuario, nombre, rol, mora_acumulada FROM usuarios WHERE carnet_codigo = ?";
            int idUsuario = 0;
            String nombreUsuario = "";
            String rolUsuario = "";
            double moraAcumulada = 0.0;
            
            try (PreparedStatement psUser = cn.prepareStatement(sqlUsuario)) {
                psUser.setString(1, carnetInput);
                try (ResultSet rs = psUser.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id_usuario");
                        nombreUsuario = rs.getString("nombre");
                        rolUsuario = rs.getString("rol");
                        moraAcumulada = rs.getDouble("mora_acumulada");
                    } else {
                        request.setAttribute("error", "Error: El carnet de usuario no se encuentra registrado.");
                        request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
                        return;
                    }
                }
            }
            
            if (moraAcumulada > 0.0) {
                request.setAttribute("error", "Denegado: El usuario posee una MORA activa de $" + moraAcumulada + ". Debe solventar sus deudas.");
                request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
                return;
            }
            
            // Buscar ID de documento mapeando el código del ejemplar ingresado
            String sqlDoc = "SELECT id_documento FROM documentos WHERE codigo_ejemplar = ?";
            int idDocumento = 0;
            try (PreparedStatement psDoc = cn.prepareStatement(sqlDoc)) {
                psDoc.setString(1, codigoEjemplarInput);
                try (ResultSet rs = psDoc.executeQuery()) {
                    if (rs.next()) {
                        idDocumento = rs.getInt("id_documento");
                    } else {
                        request.setAttribute("error", "Error: El código de ejemplar ingresado no existe en el catálogo.");
                        request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
                        return;
                    }
                }
            }
            
            // REGLA 2: Control de límites máximos según el Rol
            String sqlContar = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado = 'Activo'";
            int prestamosActivos = 0;
            try (PreparedStatement psContar = cn.prepareStatement(sqlContar)) {
                psContar.setInt(1, idUsuario);
                try (ResultSet rsContar = psContar.executeQuery()) {
                    if (rsContar.next()) prestamosActivos = rsContar.getInt(1);
                }
            }
            
            int maximoPermitido = rolUsuario.equalsIgnoreCase("Profesor") ? 6 : 3;
            if (prestamosActivos >= maximoPermitido) {
                request.setAttribute("error", "Denegado: El usuario de tipo [" + rolUsuario + "] ya cuenta con " + prestamosActivos + " préstamos activos. (Límite: " + maximoPermitido + ").");
                request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
                return;
            }
            
            // TRANSACCIÓN : Guardar Préstamo y descontar 1 unidad al stock físico del Libro
            cn.setAutoCommit(false);
            
            String sqlInsert = "INSERT INTO prestamos (id_usuario, id_documento, fecha_prestamo, fecha_devolucion_esperada, estado) " +
                               "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), 'Activo')";
            String sqlRestar = "UPDATE libros SET ejemplares_disponibles = ejemplares_disponibles - 1 WHERE id_documento = ? AND ejemplares_disponibles > 0";
            
            try (PreparedStatement psIns = cn.prepareStatement(sqlInsert);
                 PreparedStatement psRes = cn.prepareStatement(sqlRestar)) {
                
                psIns.setInt(1, idUsuario);
                psIns.setInt(2, idDocumento);
                psIns.setInt(3, diasSolicitados);
                psIns.executeUpdate();
                
                psRes.setInt(1, idDocumento);
                int filasAfectadas = psRes.executeUpdate();
                
                if (filasAfectadas == 0) {
                    cn.rollback();
                    request.setAttribute("error", "Error: No hay ejemplares disponibles en el inventario para realizar la transacción.");
                    request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
                    return;
                }
                
                cn.commit();
                request.setAttribute("exito", "¡Préstamo registrado exitosamente a favor de: " + nombreUsuario + "!");
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            request.setAttribute("error", "Error interno en el motor relacional: " + e.getMessage());
        } finally {
            if (cn != null) { try { cn.close(); } catch (SQLException ex) {} }
        }
        
        request.getRequestDispatcher("registrarPrestamo.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}