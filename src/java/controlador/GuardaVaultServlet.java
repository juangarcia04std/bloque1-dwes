package controlador;

import modelo.RegistroPassword;
import modelo.VaultDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet controlador que gestiona el guardado seguro de contraseñas en el servidor
 * y actualiza las métricas en la sesión activa del usuario.
 */
@WebServlet("/guardaVault")
public class GuardaVaultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Procesa la solicitud POST para guardar una clave aprobada.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. Extraer los datos enviados desde resultadoAnalisis.jsp
        String clave = request.getParameter("clave");
        String fortalezaStr = request.getParameter("fortaleza");

        // Control defensivo: si la clave es nula o vacía, no se guarda y se vuelve al inicio
        if (clave == null || clave.trim().isEmpty() || fortalezaStr == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int fortaleza = Integer.parseInt(fortalezaStr);
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 2. Crear la entidad de registro
        RegistroPassword nuevoRegistro = new RegistroPassword(clave, fortaleza, fechaActual);

        // 3. Obtener la ruta física del archivo /WEB-INF/vault.txt en el servidor
        String rutaArchivo = getServletContext().getRealPath("/WEB-INF/vault.txt");

        // 4. Persistir mediante el DAO
        VaultDAO dao = new VaultDAO();
        dao.guardar(nuevoRegistro, rutaArchivo);

        // 5. Actualizar el contador de claves auditadas en la sesión activa (HttpSession)
        HttpSession session = request.getSession();
        Integer contador = (Integer) session.getAttribute("contadorClaves");
        if (contador == null) {
            contador = 0;
        }
        session.setAttribute("contadorClaves", contador + 1);

        // 6. Redirigir al servlet de consulta para mostrar el listado actualizado
        response.sendRedirect(request.getContextPath() + "/consultaVault");
    }
}