package controlador;

import modelo.RegistroPassword;
import modelo.VaultDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet controlador que recupera los registros almacenados en el archivo
 * del servidor y delega la visualización a vaultHistorico.jsp.
 */
@WebServlet("/consultaVault")
public class ConsultaVaultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Atiende peticiones HTTP GET para consultar el histórico.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la ruta física del archivo seguro
        String rutaArchivo = getServletContext().getRealPath("/WEB-INF/vault.txt");

        // 2. Recuperar la colección de registros desde el fichero mediante el DAO
        VaultDAO dao = new VaultDAO();
        ArrayList<RegistroPassword> listaClaves = dao.obtenerTodos(rutaArchivo);

        // 3. Asignar la colección ArrayList como atributo de la petición
        request.setAttribute("listaHistorico", listaClaves);

        // 4. Reenviar el flujo de datos hacia la vista vaultHistorico.jsp
        request.getRequestDispatcher("/vaultHistorico.jsp").forward(request, response);
    }
}