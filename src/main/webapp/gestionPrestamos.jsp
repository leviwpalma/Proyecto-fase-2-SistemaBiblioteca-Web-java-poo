<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Control de Préstamos</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .navbar { background-color: #4f46e5; padding: 15px 30px; color: white; display: flex; justify-content: space-between; align-items: center; border-radius: 4px; margin-bottom: 30px; }
            .navbar h1 { margin: 0; font-size: 20px; }
            .navbar a { color: white; text-decoration: none; font-weight: bold; }
            .contenedor { max-width: 1100px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            .header-seccion { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .header-seccion h2 { color: #0f172a; margin: 0; }
            .btn-agregar { background-color: #6366f1; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; }
            .btn-agregar:hover { background-color: #4f46e5; }
            table { width: 100%; border-collapse: collapse; margin-top: 10px; }
            th, td { padding: 12px; border: 1px solid #e2e8f0; text-align: left; }
            th { background-color: #0f172a; color: white; }
            tr:nth-child(even) { background-color: #f8fafc; }
            .btn-devolver { background-color: #ef4444; color: white; padding: 6px 10px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 12px; }
            .btn-devolver:hover { background-color: #dc2626; }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>📋 Registro de Circulación - Mediateca UDB</h1>
            <a href="menuEncargado.jsp">← Volver al Menú</a>
        </div>

        <div class="contenedor">
            <div class="header-seccion">
                <h2>Préstamos Activos en Sala y Domicilio</h2>
                <a href="registrarPrestamo.jsp" class="btn-agregar">+ Registrar Nuevo Préstamo</a>
            </div>

            <% 
                if (request.getAttribute("tablaPrestamosHtml") == null) {
                    response.sendRedirect("PrestamoServlet?accion=listar");
                } else {
                    out.println(request.getAttribute("tablaPrestamosHtml"));
                }
            %>
        </div>
    </body>
</html>