<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Control de Usuarios</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .navbar { background-color: #0ea5e9; padding: 15px 30px; color: white; display: flex; justify-content: space-between; align-items: center; border-radius: 4px; margin-bottom: 30px; }
            .navbar h1 { margin: 0; font-size: 20px; }
            .navbar a { color: white; text-decoration: none; font-weight: bold; }
            .contenedor { max-width: 1100px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            .header-seccion { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .header-seccion h2 { color: #0f172a; margin: 0; }
            .btn-agregar { background-color: #0284c7; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; }
            .btn-agregar:hover { background-color: #0369a1; }
            .msg-error { background-color: #fee2e2; color: #991b1b; padding: 12px; border-radius: 6px; margin-bottom: 20px; font-weight: bold; }
            table { width: 100%; border-collapse: collapse; margin-top: 10px; }
            th, td { padding: 12px; border: 1px solid #e2e8f0; text-align: left; }
            th { background-color: #0f172a; color: white; }
            tr:nth-child(even) { background-color: #f8fafc; }
            .btn-editar { background-color: #eab308; color: white; padding: 6px 10px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 12px; margin-right: 5px; }
            .btn-eliminar { background-color: #ef4444; color: white; padding: 6px 10px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 12px; }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>👥 Control de Usuarios - Mediateca UDB</h1>
            <a href="menuEncargado.jsp">← Volver al Menú</a>
        </div>

        <div class="contenedor">
            <% if (request.getAttribute("error") != null) { %>
                <div class="msg-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <div class="header-seccion">
                <h2>Cuentas y Miembros Registrados</h2>
                <a href="registroUsuario.jsp" class="btn-agregar">+ Registrar Nuevo Usuario</a>
            </div>

            <% 
                if (request.getAttribute("tablaUsuariosHtml") == null) {
                    response.sendRedirect("UsuarioServlet?accion=listar");
                } else {
                    out.println(request.getAttribute("tablaUsuariosHtml"));
                }
            %>
        </div>
    </body>
</html>