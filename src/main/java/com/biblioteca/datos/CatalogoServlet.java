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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "CatalogoServlet", urlPatterns = {"/CatalogoServlet"})
public class CatalogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        // Capturar si viene una solicitud de detalle específico
        String idDetalle = request.getParameter("id_detalle");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Sistema de Mediateca - UDB</title>");
            out.println("<style>");
            out.println("body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; }");
            out.println(".contenedor { max-width: 950px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }");
            out.println("h2, h3 { color: #0056b3; text-align: center; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; background: #fff; }");
            out.println("th, td { padding: 12px; border: 1px solid #dee2e6; text-align: left; }");
            out.println("th { background-color: #0056b3; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f8f9fa; }");
            out.println(".btn { display: inline-block; padding: 10px 15px; background: #0056b3; color: white; text-decoration: none; border-radius: 4px; margin-top: 20px; font-weight: bold; }");
            out.println(".btn-secundario { display: inline-block; padding: 8px 12px; background: #6c757d; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 13px; }");
            out.println(".btn:hover { background: #004085; }");
            out.println(".btn-secundario:hover { background: #5a6268; }");
            out.println(".ficha-tecnica { border-left: 5px solid #0056b3; padding-left: 20px; margin: 20px 0; background: #fdfdfd; padding-top: 10px; padding-bottom: 10px; }");
            out.println(".ficha-tecnica p { margin: 8px 0; font-size: 16px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='contenedor'>");

            // ==========================================
            //  DETALLE DE UN EJEMPLAR ESPECÍFICO
            // ==========================================
            if (idDetalle != null && !idDetalle.trim().isEmpty()) {
                
                String sqlDetalle = "SELECT d.codigo_ejemplar, d.tipo_documento, l.isbn, l.titulo, l.autor, l.editorial, l.anio_publicacion, l.ejemplares_disponibles " +
                                    "FROM libros l " +
                                    "INNER JOIN documentos d ON l.id_documento = d.id_documento " +
                                    "WHERE l.id_documento = ?";
                
                try (Connection cn = Conexion.getConexion();
                     PreparedStatement ps = cn.prepareStatement(sqlDetalle)) {
                    
                    ps.setInt(1, Integer.parseInt(idDetalle));
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            out.println("<h2>Ficha Técnica del Ejemplar</h2>");
                            out.println("<div class='ficha-tecnica'>");
                            out.println("<p><strong>Título:</strong> " + rs.getString("titulo") + "</p>");
                            out.println("<p><strong>Autor:</strong> " + rs.getString("autor") + "</p>");
                            out.println("<p><strong>Código de Ejemplar:</strong> <span style='color:#0056b3; font-weight:bold;'>" + rs.getString("codigo_ejemplar") + "</span></p>");
                            out.println("<p><strong>Tipo de Documento:</strong> " + rs.getString("tipo_documento") + "</p>");
                            out.println("<p><strong>ISBN / Identificador:</strong> " + rs.getString("isbn") + "</p>");
                            out.println("<p><strong>Editorial:</strong> " + rs.getString("editorial") + "</p>");
                            out.println("<p><strong>Año de Publicación:</strong> " + rs.getInt("anio_publicacion") + "</p>");
                            out.println("<p><strong>Unidades Disponibles en Sala:</strong> " + rs.getInt("ejemplares_disponibles") + "</p>");
                            out.println("</div>");
                        } else {
                            out.println("<p style='color:red;'>El ejemplar seleccionado no existe.</p>");
                        }
                    }
                } catch (SQLException e) {
                    out.println("<p style='color:red;'>Error al cargar los detalles: " + e.getMessage() + "</p>");
                }
                
                // Botón para retroceder a los resultados de búsqueda sin perder la sesión
                out.println("<a href='javascript:history.back()' class='btn'>← Volver a los Resultados</a>");

            // ==========================================
            //  TABLA GENERAL DE RESULTADOS
            // ==========================================
            } else {
                
                String titulo = request.getParameter("txtTitulo");
                String autor = request.getParameter("txtAutor");
                String tipo = request.getParameter("cmbTipo");

                out.println("<h2>Resultados del Catálogo de Libros</h2>");
                
                String sql = "SELECT l.id_documento, d.codigo_ejemplar, d.tipo_documento, l.isbn, l.titulo, l.autor, l.editorial, l.anio_publicacion, l.ejemplares_disponibles " +
                             "FROM libros l " +
                             "INNER JOIN documentos d ON l.id_documento = d.id_documento " +
                             "WHERE 1=1";
                
                if (titulo != null && !titulo.trim().isEmpty()) {
                    sql += " AND l.titulo LIKE ?";
                }
                if (autor != null && !autor.trim().isEmpty()) {
                    sql += " AND l.autor LIKE ?";
                }
                if (tipo != null && !tipo.equals("todos")) {
                    sql += " AND d.tipo_documento = ?";
                }

                try (Connection cn = Conexion.getConexion();
                     PreparedStatement ps = cn.prepareStatement(sql)) {
                    
                    int paramIndex = 1;
                    if (titulo != null && !titulo.trim().isEmpty()) {
                        ps.setString(paramIndex++, "%" + titulo.trim() + "%");
                    }
                    if (autor != null && !autor.trim().isEmpty()) {
                        ps.setString(paramIndex++, "%" + autor.trim() + "%");
                    }
                    if (tipo != null && !tipo.equals("todos")) {
                        String nombreTipo = "";
                        if (tipo.equals("1")) nombreTipo = "Libro";
                        if (tipo.equals("2")) nombreTipo = "Revista";
                        if (tipo.equals("3")) nombreTipo = "CD/DVD";
                        ps.setString(paramIndex++, nombreTipo);
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        out.println("<table>");
                        out.println("<tr>" +
                                    "<th>Código</th>" +
                                    "<th>Título</th>" +
                                    "<th>Autor</th>" +
                                    "<th>Editorial</th>" +
                                    "<th>Disponibles</th>" +
                                    "<th>Acción</th>" +
                                    "</tr>");
                        
                        boolean hayResultados = false;
                        while (rs.next()) {
                            hayResultados = true;
                            out.println("<tr>");
                            out.println("<td>" + rs.getString("codigo_ejemplar") + "</td>");
                            out.println("<td><strong>" + rs.getString("titulo") + "</strong></td>");
                            out.println("<td>" + rs.getString("autor") + "</td>");
                            out.println("<td>" + rs.getString("editorial") + "</td>");
                            out.println("<td>" + rs.getInt("ejemplares_disponibles") + "</td>");
                            // Botón dinámico que envía el ID de la base de datos a la URL para ver el detalle
                            out.println("<td><a href='CatalogoServlet?id_detalle=" + rs.getInt("id_documento") + "' class='btn-secundario'>Ver Detalle</a></td>");
                            out.println("</tr>");
                        }
                        
                        if (!hayResultados) {
                            out.println("<tr><td colspan='6' style='text-align:center; color:#888;'>No se encontraron ejemplares con los criterios ingresados.</td></tr>");
                        }
                        out.println("</table>");
                    }
                    
                } catch (SQLException e) {
                    out.println("<p style='color:red;'>Error al consultar la base de datos: " + e.getMessage() + "</p>");
                }

                out.println("<a href='index.jsp' class='btn'>Nueva Búsqueda</a>");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}