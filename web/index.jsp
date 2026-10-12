<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="java.net.URLDecoder" %>
<%
    // Inicialización del nombre de usuario en la sesión si no existe previamente
    if (session.getAttribute("nombreUsuario") == null) {
        session.setAttribute("nombreUsuario", "Empleado_Seguridad");
    }
    String usuario = (String) session.getAttribute("nombreUsuario");

    // Lectura del contador de claves auditadas en la sesión activa
    Integer contador = (Integer) session.getAttribute("contadorClaves");
    int totalClaves = (contador != null) ? contador : 0;

    // Recuperación y decodificación de la Cookie "ultimaAuditoria"
    String ultimaAuditoria = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("ultimaAuditoria".equals(c.getName())) {
                ultimaAuditoria = URLDecoder.decode(c.getValue(), "UTF-8").replace("_", " ");
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>KeyVault - Auditoria de Contrasenas</title>
</head>
<body>

    <!-- Tabla para centrar y estructurar el formulario sin utilizar CSS -->
    <table align="center" width="550" cellpadding="0" cellspacing="0">
        <tr>
            <td>

                <fieldset>
                    <legend><strong>Panel de Auditoria - KeyVault</strong></legend>

                    <!-- Información de sesión y auditoría previa -->
                    <table width="100%" cellpadding="4" cellspacing="0">
                        <tr>
                            <td><strong>Usuario activo:</strong></td>
                            <td><%= usuario %></td>
                        </tr>
                        <tr>
                            <td><strong>Claves registradas en sesion:</strong></td>
                            <td><%= totalClaves %></td>
                        </tr>
                        <tr>
                            <td><strong>Ultima auditoria realizada:</strong></td>
                            <td>
                                <% if (ultimaAuditoria != null) { %>
                                    <%= ultimaAuditoria %>
                                <% } else { %>
                                    <em>Sin registros previos en este equipo</em>
                                <% } %>
                            </td>
                        </tr>
                    </table>

                    <hr>

                    <!-- Formulario para ingresar clave o solicitar autogeneración -->
                    <form action="evaluaPassword" method="POST">
                        <table width="100%" cellpadding="6" cellspacing="0">
                            <tr>
                                <td colspan="2">
                                    <label for="password"><strong>Ingrese contrasena a auditar:</strong></label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <input type="text" id="password" name="password" size="55" placeholder="Escriba una clave o genere una automatica">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <!-- Dos botones diferenciados por el valor del atributo accion -->
                                    <button type="submit" name="accion" value="auditar">Auditar Clave</button>
                                    <button type="submit" name="accion" value="generar">Generar Clave Segura</button>
                                </td>
                            </tr>
                        </table>
                    </form>

                    <hr>

                    <!-- Enlace directo para consultar el archivo seguro en el servidor -->
                    <table width="100%" cellpadding="4" cellspacing="0">
                        <tr>
                            <td align="center">
                                <a href="consultaVault">[ Consultar Historico de Claves en Servidor ]</a>
                            </td>
                        </tr>
                    </table>

                </fieldset>

            </td>
        </tr>
    </table>

</body>
</html>