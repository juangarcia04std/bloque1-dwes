<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.EvaluadorPassword.ResultadoAuditoria" %>
<%
    // Recuperación de los atributos transmitidos desde el servlet controlador
    String password = (String) request.getAttribute("passwordAuditada");
    ResultadoAuditoria resultado = (ResultadoAuditoria) request.getAttribute("resultado");

    // Redirección defensiva si se intenta acceder a la página sin pasar por el servlet
    if (resultado == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    int porcentaje = resultado.getPorcentaje();
    boolean esVacia = (password == null || password.trim().isEmpty());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>KeyVault - Resultado del Analisis</title>
</head>
<body>

    <!-- Estructura centrada con HTML puro -->
    <table align="center" width="580" cellpadding="0" cellspacing="0">
        <tr>
            <td>

                <fieldset>
                    <legend><strong>Diagnostico de Seguridad</strong></legend>

                    <!-- Tabla de visualización de métricas de la contraseña -->
                    <table width="100%" cellpadding="6" cellspacing="0">
                        <tr>
                            <td width="35%"><strong>Contrasena evaluada:</strong></td>
                            <td><code><%= esVacia ? "(vacia)" : password %></code></td>
                        </tr>
                        <tr>
                            <td><strong>Fortaleza calculada:</strong></td>
                            <td>
                                <!-- Barra de progreso estándar de HTML5 -->
                                <progress value="<%= porcentaje %>" max="100"></progress>
                                <strong><%= porcentaje %>%</strong>
                            </td>
                        </tr>
                        <tr>
                            <td><strong>Tiempo estimado de rotura:</strong></td>
                            <td><%= resultado.getTiempoRotura() %></td>
                        </tr>
                    </table>

                    <!-- Bloque con recomendaciones generadas por el modelo -->
                    <fieldset>
                        <legend><strong>Recomendaciones</strong></legend>
                        <% if (resultado.getRecomendaciones().isEmpty()) { %>
                            <p>La contrasena cumple satisfactoriamente con los estandares corporativos.</p>
                        <% } else { %>
                            <ul>
                                <% for (String rec : resultado.getRecomendaciones()) { %>
                                    <li><%= rec %></li>
                                <% } %>
                            </ul>
                        <% } %>
                    </fieldset>

                    <br>

                    <!-- Acciones: Guardar en servidor o Volver -->
                    <table width="100%" cellpadding="4" cellspacing="0">
                        <tr>
                            <td>
                                <% if (esVacia) { %>
                                    <!-- Aviso de restricción si la clave no tiene contenido -->
                                    <p><strong>Aviso:</strong> No se puede guardar una contrasena vacia en el Vault.</p>
                                <% } else { %>
                                    <!-- Formulario para enviar la clave aprobada a GuardaVaultServlet -->
                                    <form action="guardaVault" method="POST">
                                        <input type="hidden" name="clave" value="<%= password %>">
                                        <input type="hidden" name="fortaleza" value="<%= porcentaje %>">
                                        <input type="submit" value="Guardar en Vault">
                                    </form>
                                <% } %>
                            </td>
                            <td align="right">
                                <!-- Botón Volver al formulario de inicio -->
                                <form action="index.jsp" method="GET">
                                    <input type="submit" value="Volver al Inicio">
                                </form>
                            </td>
                        </tr>
                    </table>

                </fieldset>

            </td>
        </tr>
    </table>

</body>
</html>