<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mediateca Don Bosco - Catálogo en Línea</title>
        <style>
            body { 
                font-family: 'Segoe UI', Arial, sans-serif; 
                background-color: #f4f6f9; 
                padding: 40px; 
                margin: 0; 
            }
            .navbar { 
                background-color: #0056b3; 
                padding: 15px 30px; 
                color: white; 
                display: flex; 
                justify-content: space-between; 
                align-items: center; 
                border-radius: 4px; 
                margin-bottom: 30px; 
            }
            .navbar h1 { margin: 0; font-size: 20px; }
            
            /* BOTÓN DE REGRESO AL LOGIN */
            .btn-volver { 
                background-color: rgba(255, 255, 255, 0.2); 
                color: white; 
                padding: 8px 15px; 
                text-decoration: none; 
                border-radius: 4px; 
                font-weight: bold; 
                font-size: 14px;
                transition: background 0.3s;
                border: 1px solid rgba(255, 255, 255, 0.4);
            }
            .btn-volver:hover { 
                background-color: rgba(255, 255, 255, 0.4); 
            }
            
            .contenedor-busqueda { 
                max-width: 650px; 
                background: white; 
                padding: 30px; 
                border-radius: 8px; 
                box-shadow: 0 4px 12px rgba(0,0,0,0.1); 
                margin: 0 auto; 
            }
            h2 { color: #334155; margin-top: 0; margin-bottom: 20px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; font-weight: bold; margin-bottom: 8px; color: #475569; }
            .form-group input, .form-group select { 
                width: 100%; 
                padding: 10px; 
                border: 1px solid #cbd5e1; 
                border-radius: 6px; 
                box-sizing: border-box; 
                font-size: 14px; 
            }
            .form-group select { background-color: white; }
            .btn-buscar { 
                background-color: #0056b3; 
                color: white; 
                padding: 12px 20px; 
                border: none; 
                border-radius: 6px; 
                font-weight: bold; 
                cursor: pointer; 
                font-size: 16px; 
                width: 100%; 
                margin-top: 10px; 
            }
            .btn-buscar:hover { background-color: #004085; }
            .info-invitado {
                text-align: center;
                color: #64748b;
                font-size: 13px;
                margin-top: 15px;
            }
        </style>
    </head>
    <body>

        <div class="navbar">
            <h1>🔍 Catálogo Público - Mediateca Don Bosco</h1>
            <a href="login.jsp" class="btn-volver">← Volver al Login</a>
        </div>

        <div class="contenedor-busqueda">
            <h2>Buscador de Ejemplares</h2>
            
            <form action="CatalogoServlet" method="GET">
                <div class="form-group">
                    <label for="txtTitulo">Título del Documento:</label>
                    <input type="text" id="txtTitulo" name="txtTitulo" placeholder="Ej: Introducción a base de datos...">
                </div>

                <div class="form-group">
                    <label for="txtAutor">Autor:</label>
                    <input type="text" id="txtAutor" name="txtAutor" placeholder="Ej: Deitel & Deitel">
                </div>

                <div class="form-group">
                    <label for="cmbTipo">Tipo de Material Técnico:</label>
                    <select id="cmbTipo" name="cmbTipo">
                        <option value="todos">-- Mostrar Todos --</option>
                        <option value="1">Libros</option>
                        <option value="2">Revistas Científicas</option>
                        <option value="3">CD / DVD de Recursos</option>
                    </select>
                </div>

                <button type="submit" class="btn-buscar">Procesar Búsqueda Inteligente</button>
            </form>
            
            <div class="info-invitado">
                Estás consultando el inventario en modo de <strong>Acceso Público Invitado</strong>.
            </div>
        </div>

    </body>
</html>