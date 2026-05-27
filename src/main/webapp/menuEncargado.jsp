<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Panel Principal</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
            .navbar { background-color: #0f172a; padding: 15px 30px; color: white; display: flex; justify-content: space-between; align-items: center; border-radius: 4px; margin-bottom: 30px; }
            .navbar h1 { margin: 0; font-size: 20px; }
            .btn-Salir { background-color: #ef4444; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; }
            /* Cambiado a grid de 3 columnas para que los módulos queden perfectamente alineados en fila */
            .contenedor-menu { max-width: 1100px; margin: 0 auto; display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px; margin-top: 40px; }
            .tarjeta-modulo { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: center; border-top: 4px solid #3b82f6; display: flex; flex-direction: column; justify-content: space-between; }
            .tarjeta-modulo h3 { color: #1e293b; margin-top: 0; font-size: 20px; }
            .tarjeta-modulo p { color: #64748b; font-size: 14px; margin-bottom: 20px; flex-grow: 1; }
            .btn-enlace { display: inline-block; background-color: #3b82f6; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; margin-bottom: 8px; font-size: 14px; }
            .btn-enlace:hover { background-color: #2563eb; }
            
            /* Colores del Módulo de Préstamos */
            .modulo-prestamos { border-top-color: #6366f1; }
            .modulo-prestamos .btn-enlace { background-color: #6366f1; }
            .modulo-prestamos .btn-enlace:hover { background-color: #4f46e5; }
            
            /* Colores nuevos del Módulo de Usuarios */
            .modulo-usuarios { border-top-color: #0ea5e9; }
            .modulo-usuarios .btn-enlace { background-color: #0ea5e9; }
            .modulo-usuarios .btn-enlace:hover { background-color: #0284c7; }
            .modulo-usuarios .btn-registro-rapido { background-color: #0f172a; }
            .modulo-usuarios .btn-registro-rapido:hover { background-color: #1e293b; }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>📚 Panel de Control - Biblioteca Don Bosco</h1>
            <a href="login.jsp" class="btn-Salir">Cerrar Sesión</a>
        </div>

        <h2 style="text-align: center; color: #1e293b;">Bienvenido al Sistema de Circulación Web</h2>
        <p style="text-align: center; color: #64748b;">Seleccione el módulo administrativo que desea operar en esta sesión.</p>

        <div class="contenedor-menu">
            <div class="tarjeta-modulo">
                <h3>Módulo de Encargados</h3>
                <p>Permite registrar nuevos libros en el catálogo, modificar fichas técnicas existentes y dar de baja ejemplares dañados o retirados.</p>
                <a href="EncargadoServlet?accion=listar" class="btn-enlace">Gestionar Inventario</a>
            </div>

            <div class="tarjeta-modulo modulo-prestamos">
                <h3>Módulo de Préstamos</h3>
                <p>Procesa solicitudes de préstamo controlando restricciones de usuarios en mora y verificando topes máximos por políticas institucionales.</p>
                <a href="PrestamoServlet?accion=listar" class="btn-enlace">Control de Préstamos</a>
            </div>

            <div class="tarjeta-modulo modulo-usuarios">
                <h3>Módulo de Usuarios</h3>
                <p>Administra las cuentas de alumnos, profesores y encargados. Permite gestionar contraseñas, perfiles de acceso y estados de mora.</p>
                <div>
                    <a href="UsuarioServlet?accion=listar" class="btn-enlace">📋 Control de Usuarios</a>
                    <a href="registroUsuario.jsp" class="btn-enlace btn-registro-rapido">➕ Registrar Nuevo</a>
                </div>
            </div>
        </div>
    </body>
</html>