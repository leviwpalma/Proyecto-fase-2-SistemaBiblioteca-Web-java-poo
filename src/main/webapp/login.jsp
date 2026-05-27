<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca Don Bosco - Login</title>
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #0f172a; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
            .login-card { background: white; padding: 40px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.3); width: 100%; max-width: 400px; box-sizing: border-box; }
            .login-card h2 { margin-top: 0; color: #1e293b; text-align: center; margin-bottom: 5px; }
            .login-card p { text-align: center; color: #64748b; font-size: 14px; margin-bottom: 30px; }
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; font-weight: bold; margin-bottom: 8px; color: #334155; font-size: 14px; }
            .form-group input { width: 100%; padding: 12px; border: 1px solid #cbd5e1; border-radius: 6px; box-sizing: border-box; font-size: 14px; }
            .btn-ingresar { background-color: #3b82f6; color: white; padding: 12px; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; font-size: 15px; width: 100%; margin-top: 10px; }
            .btn-ingresar:hover { background-color: #2563eb; }
            .divisor { display: flex; align-items: center; text-align: center; color: #cbd5e1; margin: 25px 0; font-size: 13px; }
            .divisor::before, .divisor::after { content: ''; flex: 1; border-bottom: 1px solid #e2e8f0; }
            .divisor:not(:empty)::before { margin-right: .5em; }
            .divisor:not(:empty)::after { margin-left: .5em; }
            .btn-invitado { display: block; text-align: center; background-color: #f1f5f9; color: #1e293b; padding: 12px; border-radius: 6px; text-decoration: none; font-weight: bold; font-size: 14px; border: 1px solid #cbd5e1; }
            .btn-invitado:hover { background-color: #e2e8f0; }
            .error-login { background-color: #fee2e2; color: #991b1b; padding: 10px; border-radius: 6px; text-align: center; font-weight: bold; font-size: 13px; margin-bottom: 20px; border-left: 4px solid #dc2626; }
        </style>
    </head>
    <body>
        <div class="login-card">
            <h2>Biblioteca UDB</h2>
            <p>Acceso al Sistema Bibliotecario</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error-login"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="LoginServlet" method="POST">
                <div class="form-group">
                    <label>Carnet / Usuario:</label>
                    <input type="text" name="txtUsuario" placeholder="Ingrese su carnet de encargado" required>
                </div>
                
                <div class="form-group">
                    <label>Contraseña de Acceso:</label>
                    <input type="password" name="txtClave" placeholder="••••••••" required>
                </div>
                
                <button type="submit" class="btn-ingresar">Iniciar Sesión</button>
            </form>

            <div class="divisor">O BIEN</div>

            <a href="index.jsp" class="btn-invitado">🔍 Consultar Libros como Invitado</a>
        </div>
    </body>
</html>