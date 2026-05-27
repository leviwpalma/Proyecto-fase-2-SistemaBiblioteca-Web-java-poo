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
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EncargadoServlet", urlPatterns = {"/EncargadoServlet"})
public class EncargadoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if (accion.equals("listar")) {
                listarLibros(request, response);
            } else if (accion.equals("insertar")) {
                insertarLibro(request, response);
            } else if (accion.equals("cargar")) {
                cargarLibroParaEditar(request, response);
            } else if (accion.equals("actualizar")) {
                actualizarLibro(request, response);
            } else if (accion.equals("eliminar")) {
                eliminarLibro(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    //  LEER CATALOGO
    private void listarLibros(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        StringBuilder html = new StringBuilder();
        
        String sql = "SELECT l.id_documento, d.codigo_ejemplar, l.isbn, l.titulo, l.autor, l.editorial, l.anio_publicacion, l.ejemplares_disponibles " +
                     "FROM libros l " +
                     "INNER JOIN documentos d ON l.id_documento = d.id_documento " +
                     "ORDER BY d.codigo_ejemplar DESC";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            html.append("<table>");
            html.append("<tr>")
                .append("<th>Código</th>")
                .append("<th>Título</th>")
                .append("<th>Autor</th>")
                .append("<th>ISBN</th>")
                .append("<th>Editorial</th>")
                .append("<th>Año</th>")
                .append("<th>Stock</th>")
                .append("<th>Acciones</th>")
                .append("</tr>");
            
            while (rs.next()) {
                html.append("<tr>");
                html.append("<td>").append(rs.getString("codigo_ejemplar")).append("</td>");
                html.append("<td><strong>").append(rs.getString("titulo")).append("</strong></td>");
                html.append("<td>").append(rs.getString("autor")).append("</td>");
                html.append("<td>").append(rs.getString("isbn")).append("</td>");
                html.append("<td>").append(rs.getString("editorial")).append("</td>");
                html.append("<td>").append(rs.getInt("anio_publicacion")).append("</td>");
                html.append("<td>").append(rs.getInt("ejemplares_disponibles")).append("</td>");
                html.append("<td>");
                // BOTÓN DE MODIFICAR AGREGADO
                html.append("<a href='EncargadoServlet?accion=cargar&id=").append(rs.getInt("id_documento")).append("' class='btn-accion btn-modificar'>Modificar</a>");
                html.append("<a href='EncargadoServlet?accion=eliminar&id=").append(rs.getInt("id_documento"))
                    .append("' class='btn-accion btn-eliminar' onclick='return confirm(\"¿Está seguro de eliminar este ejemplar?\")'>Eliminar</a>");
                html.append("</td>");
                html.append("</tr>");
            }
            html.append("</table>");
        }
        
        request.setAttribute("tablaHtml", html.toString());
        request.getRequestDispatcher("gestionLibros.jsp").forward(request, response);
    }

    // 2. BUSCAR UN LIBRO POR ID Y MANDARLO AL FORMULARIO DE EDICIÓN
    private void cargarLibroParaEditar(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        int idDocumento = Integer.parseInt(request.getParameter("id"));
        
        String sql = "SELECT l.id_documento, d.codigo_ejemplar, l.isbn, l.titulo, l.autor, l.editorial, l.anio_publicacion, l.ejemplares_disponibles " +
                     "FROM libros l " +
                     "INNER JOIN documentos d ON l.id_documento = d.id_documento " +
                     "WHERE l.id_documento = ?";
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, idDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Guardamos los datos en un Mapa temporal para que el JSP los lea fácil con Expression Language (${libro.atributo})
                    Map<String, Object> libro = new HashMap<>();
                    libro.put("id_documento", rs.getInt("id_documento"));
                    libro.put("codigo_ejemplar", rs.getString("codigo_ejemplar"));
                    libro.put("isbn", rs.getString("isbn"));
                    libro.put("titulo", rs.getString("titulo"));
                    libro.put("autor", rs.getString("autor"));
                    libro.put("editorial", rs.getString("editorial"));
                    libro.put("anio_publicacion", rs.getInt("anio_publicacion"));
                    libro.put("ejemplares_disponibles", rs.getInt("ejemplares_disponibles"));
                    
                    request.setAttribute("libro", libro);
                }
            }
        }
        request.getRequestDispatcher("editarLibro.jsp").forward(request, response);
    }

    //  GUARDAR LOS CAMBIOS ACTUALIZADOS (UPDATE)
    private void actualizarLibro(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        int idDocumento = Integer.parseInt(request.getParameter("txtIdDocumento"));
        String titulo = request.getParameter("txtTitulo");
        String autor = request.getParameter("txtAutor");
        String isbn = request.getParameter("txtIsbn");
        String editorial = request.getParameter("txtEditorial");
        int anio = Integer.parseInt(request.getParameter("txtAnio"));
        int cantidad = Integer.parseInt(request.getParameter("txtCantidad"));
        
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, editorial = ?, anio_publicacion = ?, ejemplares_disponibles = ? WHERE id_documento = ?";
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, isbn);
            ps.setString(2, titulo);
            ps.setString(3, autor);
            ps.setString(4, editorial);
            ps.setInt(5, anio);
            ps.setInt(6, cantidad);
            ps.setInt(7, idDocumento);
            
            ps.executeUpdate();
        }
        
        response.sendRedirect("EncargadoServlet?accion=listar");
    }

    // CREAR (INSERT)
    private void insertarLibro(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String codigo = request.getParameter("txtCodigo");
        String titulo = request.getParameter("txtTitulo");
        String autor = request.getParameter("txtAutor");
        String isbn = request.getParameter("txtIsbn");
        String editorial = request.getParameter("txtEditorial");
        int anio = Integer.parseInt(request.getParameter("txtAnio"));
        int cantidad = Integer.parseInt(request.getParameter("txtCantidad"));

        Connection cn = null;
        PreparedStatement psDoc = null;
        PreparedStatement psLib = null;
        
        try {
            cn = Conexion.getConexion();
            cn.setAutoCommit(false);
            
            String sqlDoc = "INSERT INTO documentos (codigo_ejemplar, tipo_documento) VALUES (?, 'Libro')";
            psDoc = cn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS);
            psDoc.setString(1, codigo);
            psDoc.executeUpdate();
            
            int idDocumentoGenerado = 0;
            try (ResultSet generatedKeys = psDoc.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idDocumentoGenerado = generatedKeys.getInt(1);
                }
            }
            
            String sqlLib = "INSERT INTO libros (id_documento, isbn, titulo, autor, editorial, anio_publicacion, ejemplares_disponibles) VALUES (?, ?, ?, ?, ?, ?, ?)";
            psLib = cn.prepareStatement(sqlLib);
            psLib.setInt(1, idDocumentoGenerado);
            psLib.setString(2, isbn);
            psLib.setString(3, titulo);
            psLib.setString(4, autor);
            psLib.setString(5, editorial);
            psLib.setInt(6, anio);
            psLib.setInt(7, cantidad);
            psLib.executeUpdate();
            
            cn.commit();
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            throw e;
        } finally {
            if (psDoc != null) psDoc.close();
            if (psLib != null) psLib.close();
            if (cn != null) cn.close();
        }
        response.sendRedirect("EncargadoServlet?accion=listar");
    }

    //  ELIMINAR
    private void eliminarLibro(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int idDocumento = Integer.parseInt(request.getParameter("id"));
        String sqlLib = "DELETE FROM libros WHERE id_documento = ?";
        String sqlDoc = "DELETE FROM documentos WHERE id_documento = ?";
        
        try (Connection cn = Conexion.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psLib = cn.prepareStatement(sqlLib);
                 PreparedStatement psDoc = cn.prepareStatement(sqlDoc)) {
                psLib.setInt(1, idDocumento);
                psLib.executeUpdate();
                psDoc.setInt(1, idDocumento);
                psDoc.executeUpdate();
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
        response.sendRedirect("EncargadoServlet?accion=listar");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}