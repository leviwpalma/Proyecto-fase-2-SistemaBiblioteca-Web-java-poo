<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca - Panel de Encargados</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .navbar { background-color: #1e293b; padding: 15px 30px; color: white; display: flex; justify-content: space-between; align-items: center; border-radius: 4px; margin-bottom: 30px; }
            .navbar h1 { margin: 0; font-size: 20px; }
            /* Estilo para el botón de regresar en la barra */
            .btn-regresar { background-color: #475569; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; transition: background-color 0.2s; }
            .btn-regresar:hover { background-color: #334155; }
            
            .contenedor { max-width: 1100px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: 0 auto; }
            .header-seccion { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .header-seccion h2 { color: #0f172a; margin: 0; }
            .btn-agregar { background-color: #10b981; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; }
            .btn-agregar:hover { background-color: #059669; }
            table { width: 100%; border-collapse: collapse; margin-top: 10px; }
            th, td { padding: 12px; border: 1px solid #e2e8f0; text-align: left; }
            th { background-color: #0f172a; color: white; }
            tr:nth-child(even) { background-color: #f8fafc; }
            .btn-accion { padding: 6px 10px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 12px; margin-right: 5px; color: white; }
            .btn-modificar { background-color: #3b82f6; }
            .btn-modificar:hover { background-color: #2563eb; }
            .btn-eliminar { background-color: #ef4444; }
            .btn-eliminar:hover { background-color: #dc2626; }
            .alert-success { background-color: #d1fae5; color: #065f46; padding: 12px; border-radius: 4px; margin-bottom: 20px; font-weight: bold; }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>📚 Sistema Mediateca UDB - Módulo Administrativo</h1>
            <a href="menuEncargado.jsp" class="btn-regresar">← Volver al Menú</a>
        </div>

        <div class="contenedor">
            <div class="header-seccion">
                <h2>Gestión del Inventario de Libros</h2>
                <a href="registroLibro.jsp" class="btn-agregar">+ Registrar Nuevo Libro</a>
            </div>

            <% 
                if (request.getAttribute("tablaHtml") == null) {
                    response.sendRedirect("EncargadoServlet?accion=listar");
                } else {
                    out.println(request.getAttribute("tablaHtml"));
                }
            %>
        </div>
    </body>
</html>