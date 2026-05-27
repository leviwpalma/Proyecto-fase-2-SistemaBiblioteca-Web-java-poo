<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Registrar Préstamo</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .navbar { background-color: #1e293b; padding: 15px 30px; color: white; display: flex; justify-content: space-between; align-items: center; border-radius: 4px; margin-bottom: 30px; }
            .navbar h1 { margin: 0; font-size: 20px; }
            .contenedor-form { max-width: 600px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            h2 { color: #0f172a; margin-top: 0; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .form-group { margin-bottom: 15px; }
            .form-group label { display: block; font-weight: bold; margin-bottom: 5px; color: #334155; }
            .form-group input { width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
            .btn-prestar { background-color: #6366f1; color: white; padding: 12px 20px; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 15px; width: 100%; margin-top: 10px; }
            .btn-prestar:hover { background-color: #4f46e5; }
            .btn-regresar { display: block; text-align: center; color: #64748b; text-decoration: none; margin-top: 15px; font-size: 14px; font-weight: bold; }
            .btn-regresar:hover { color: #0f172a; }
            
            /* Estilos para mensajes de error o éxito que mande el Servlet */
            .mensaje-error { background-color: #fee2e2; color: #991b1b; padding: 12px; border-radius: 4px; margin-bottom: 20px; font-weight: bold; border-left: 5px solid #dc2626; }
            .mensaje-exito { background-color: #d1fae5; color: #065f46; padding: 12px; border-radius: 4px; margin-bottom: 20px; font-weight: bold; border-left: 5px solid #10b981; }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>📚 Sistema Mediateca UDB - Módulo de Transacciones</h1>
            <span>Circulación y Préstamos</span>
        </div>

        <div class="contenedor-form">
            <h2>Registrar Préstamo de Ejemplar</h2>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="mensaje-error"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("exito") != null) { %>
                <div class="mensaje-exito"><%= request.getAttribute("exito") %></div>
            <% } %>

            <form action="PrestamoServlet" method="POST">
                
                <div class="form-group">
                    <label>Carnet / Identificador del Usuario:</label>
                    <input type="text" name="txtCarnet" placeholder="Ej: AB102322 o DOC4412" required>
                </div>
                
                <div class="form-group">
                    <label>Código del Ejemplar a Prestar:</label>
                    <input type="text" name="txtCodigoEjemplar" placeholder="Ej: LIB0001" required>
                </div>
                
                <div class="form-group">
                    <label>Días de Préstamo Sugeridos:</label>
                    <input type="number" name="txtDias" min="1" max="30" value="3" required>
                </div>
                
                <button type="submit" class="btn-prestar">Validar y Procesar Préstamo</button>
               <a href="PrestamoServlet?accion=listar" class="btn-regresar">Cancelar y Volver al Listado</a>
            </form>
        </div>
    </body>
</html>