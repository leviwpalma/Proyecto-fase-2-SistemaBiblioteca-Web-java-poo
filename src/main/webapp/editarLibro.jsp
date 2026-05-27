<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Editar Libro</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .contenedor-form { max-width: 600px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            h2 { color: #0f172a; margin-top: 0; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .form-group { margin-bottom: 15px; }
            .form-group label { display: block; font-weight: bold; margin-bottom: 5px; color: #334155; }
            .form-group input { width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
            .form-group input[readonly] { background-color: #f1f5f9; color: #64748b; cursor: not-allowed; }
            .btn-guardar { background-color: #3b82f6; color: white; padding: 12px 20px; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 15px; width: 100%; margin-top: 10px; }
            .btn-guardar:hover { background-color: #2563eb; }
            .btn-cancelar { display: block; text-align: center; color: #64748b; text-decoration: none; margin-top: 15px; font-size: 14px; font-weight: bold; }
            .btn-cancelar:hover { color: #0f172a; }
        </style>
    </head>
    <body>
        <div class="contenedor-form">
            <h2>Modificar Registro de Libro</h2>
            
            <form action="EncargadoServlet?accion=actualizar" method="POST">
                <input type="hidden" name="txtIdDocumento" value="${libro.id_documento}">
                
                <div class="form-group">
                    <label>Código del Ejemplar (No modificable):</label>
                    <input type="text" name="txtCodigo" value="${libro.codigo_ejemplar}" readonly>
                </div>
                
                <div class="form-group">
                    <label>Título del Libro:</label>
                    <input type="text" name="txtTitulo" value="${libro.titulo}" required>
                </div>
                
                <div class="form-group">
                    <label>Autor:</label>
                    <input type="text" name="txtAutor" value="${libro.autor}" required>
                </div>
                
                <div class="form-group">
                    <label>ISBN / Identificador:</label>
                    <input type="text" name="txtIsbn" value="${libro.isbn}" required>
                </div>
                
                <div class="form-group">
                    <label>Editorial:</label>
                    <input type="text" name="txtEditorial" value="${libro.editorial}" required>
                </div>
                
                <div class="form-group">
                    <label>Año de Publicación:</label>
                    <input type="number" name="txtAnio" min="1000" max="2026" value="${libro.anio_publicacion}" required>
                </div>
                
                <div class="form-group">
                    <label>Cantidad de Ejemplares Disponibles:</label>
                    <input type="number" name="txtCantidad" min="0" value="${libro.ejemplares_disponibles}" required>
                </div>
                
                <button type="submit" class="btn-guardar">Actualizar Cambios</button>
                <a href="EncargadoServlet?accion=listar" class="btn-cancelar">Cancelar y Volver</a>
            </form>
        </div>
    </body>
</html>