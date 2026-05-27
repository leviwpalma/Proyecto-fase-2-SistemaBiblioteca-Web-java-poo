<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Formulario de Usuario</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .contenedor-form { max-width: 550px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            h2 { color: #0f172a; margin-top: 0; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .form-group { margin-bottom: 18px; }
            .form-group label { display: block; font-weight: bold; margin-bottom: 6px; color: #334155; }
            .form-group input, .form-group select { width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 6px; box-sizing: border-box; }
            .btn-guardar { background-color: #0284c7; color: white; padding: 12px 20px; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; width: 100%; font-size: 15px; }
            .btn-guardar:hover { background-color: #0369a1; }
            .btn-cancelar { display: block; text-align: center; margin-top: 15px; color: #64748b; text-decoration: none; font-size: 14px; }
            .alert { padding: 10px; border-radius: 4px; margin-bottom: 15px; font-weight: bold; }
            .alert-danger { background-color: #fee2e2; color: #991b1b; }
            .alert-success { background-color: #dcfce7; color: #166534; }
        </style>
    </head>
    <body>
        <div class="contenedor-form">
            <%
                // Detectar si es una edición leyendo si viene un ID cargado en el request
                String id = (request.getAttribute("id_usuario") != null) ? request.getAttribute("id_usuario").toString() : "";
                String carnet = (request.getAttribute("carnet_codigo") != null) ? request.getAttribute("carnet_codigo").toString() : "";
                String nombre = (request.getAttribute("nombre") != null) ? request.getAttribute("nombre").toString() : "";
                String password = (request.getAttribute("password") != null) ? request.getAttribute("password").toString() : "";
                String rol = (request.getAttribute("rol") != null) ? request.getAttribute("rol").toString() : "";
                String mora = (request.getAttribute("mora_acumulada") != null) ? request.getAttribute("mora_acumulada").toString() : "0.00";
                
                boolean esEdicion = !id.isEmpty();
            %>
            
            <h2><%= esEdicion ? "Modificar Datos de Usuario" : "Inscribir Nuevo Usuario" %></h2>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("exito") != null) { %>
                <div class="alert alert-success"><%= request.getAttribute("exito") %></div>
            <% } %>

            <form action="UsuarioServlet" method="POST">
                <input type="hidden" name="id" value="<%= id %>">

                <div class="form-group">
                    <label>Carnet / Código Institucional:</label>
                    <input type="text" name="txtCarnet" value="<%= carnet %>" placeholder="Ej: AB12345" required <%= esEdicion ? "readonly style='background-color:#f1f5f9;'" : "" %>>
                </div>

                <div class="form-group">
                    <label>Nombre Completo:</label>
                    <input type="text" name="txtNombre" value="<%= nombre %>" placeholder="Ej: Juan Pérez" required>
                </div>

                <div class="form-group">
                    <label>Contraseña de Acceso:</label>
                    <input type="password" name="txtPassword" value="<%= password %>" placeholder="Escriba la clave de seguridad" required>
                </div>

                <div class="form-group">
                    <label>Rol asignado en el Sistema:</label>
                    <select name="cmbRol" required>
                        <option value="Alumno" <%= rol.equals("Alumno") ? "selected" : "" %>>Alumno</option>
                        <option value="Profesor" <%= rol.equals("Profesor") ? "selected" : "" %>>Profesor</option>
                        <option value="Administrador" <%= rol.equals("Administrador") ? "selected" : "" %>>Administrador (Encargado)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Mora Inicial ($):</label>
                    <input type="number" step="0.01" name="txtMora" value="<%= mora %>" required>
                </div>

                <button type="submit" class="btn-guardar">
                    <%= esEdicion ? "Actualizar Registro" : "Guardar e Inscribir" %>
                </button>
            </form>

            <a href="UsuarioServlet?accion=listar" class="btn-cancelar">Cancelar y regresar al listado</a>
        </div>
    </body>
</html>