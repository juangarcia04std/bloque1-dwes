package controlador;

import modelo.EvaluadorPassword;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet controlador que procesa la auditoría de una clave ingresada
 * o la solicitud de generación automática de una clave segura.
 */
@WebServlet("/evaluaPassword")
public class EvaluaPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Atiende peticiones HTTP POST provenientes del formulario de index.jsp.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Garantizar la correcta codificación de caracteres en la petición
        request.setCharacterEncoding("UTF-8");

        // 1. Extraer parámetros enviados desde la vista
        String password = request.getParameter("password");
        String accion = request.getParameter("accion");

        EvaluadorPassword evaluador = new EvaluadorPassword();

        // 2. Comprobar la acción solicitada por el usuario
        // Solo se autogenera si se pulsó el botón "Generar Clave Segura"
        if ("generar".equals(accion)) {
            password = evaluador.generarClaveSegura();
        } else if (password == null) {
            password = "";
        }

        // 3. Invocar la lógica del modelo para obtener el diagnóstico métrico
        EvaluadorPassword.ResultadoAuditoria resultado = evaluador.analizar(password);

        // 4. Crear la Cookie ultimaAuditoria con la fecha y hora actuales
        // Formato seguro para evitar espacios no válidos en cabeceras HTTP de cookies
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
        String valorCookie = URLEncoder.encode(fechaHoraActual, StandardCharsets.UTF_8.toString());

        Cookie cookieUltimaAuditoria = new Cookie("ultimaAuditoria", valorCookie);
        cookieUltimaAuditoria.setMaxAge(24 * 60 * 60); // Tiempo de vida: 24 horas (en segundos)
        cookieUltimaAuditoria.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(cookieUltimaAuditoria);

        // 5. Asignar los atributos requeridos en el objeto request
        request.setAttribute("passwordAuditada", password);
        request.setAttribute("resultado", resultado);

        // 6. Transferir el control y despachar la petición a resultadoAnalisis.jsp
        request.getRequestDispatcher("/resultadoAnalisis.jsp").forward(request, response);
    }
}