<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.RegistroPassword" %>
<%
    // Obtención de la lista de registros cargada por ConsultaVaultServlet
    @SuppressWarnings("unchecked")
    ArrayList<RegistroPassword> lista = (ArrayList<RegistroPassword>) request.getAttribute("listaHistorico");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>KeyVault - Historico</title>
</head>
<body>

    <!-- Estructura de visualización sin estilos CSS -->
    <table align="center" width="650" cellpadding="0" cellspacing="0">
        <tr>
            <td>

                <fieldset>
                    <legend><strong>Registro Historico del Servidor (/WEB-INF/vault.txt)</strong></legend>

                    <br>

                    <% if (lista == null || lista.isEmpty()) { %>
                        <p align="center"><em>No existen contrasenas almacenadas en el archivo del servidor.</em></p>
                    <% } else { %>
                        <!-- Tabla clásica con atributos HTML estándar (border, cellpadding, cellspacing) -->
                        <table border="1" cellpadding="6" cellspacing="0" width="100%">
                            <thead>
                                <tr>
                                    <th width="35%">Fecha y Hora</th>
                                    <th>Contrasena</th>
                                    <th width="20%">Fortaleza</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (RegistroPassword reg : lista) { %>
                                    <tr>
                                        <td align="center"><%= reg.getFechaRegistro() %></td>
                                        <td><code><%= reg.getClave() %></code></td>
                                        <td align="center"><strong><%= reg.getNivelFortaleza() %>%</strong></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } %>

                    <hr>

                    <!-- Enlace de retorno -->
                    <table width="100%" cellpadding="4" cellspacing="0">
                        <tr>
                            <td align="center">
                                <a href="index.jsp">[ Volver a la Auditoria ]</a>
                            </td>
                        </tr>
                    </table>

                </fieldset>

            </td>
        </tr>
    </table>

</body>
</html>