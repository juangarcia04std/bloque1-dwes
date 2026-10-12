package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) que centraliza las operaciones de lectura y persistencia
 * sobre el archivo físico de registros ubicado en /WEB-INF/vault.txt.
 */
public class VaultDAO {

    /**
     * Añade un nuevo registro de contraseña al final del archivo físico.
     * El método es sincronizado para evitar condiciones de carrera entre hilos del servidor.
     * 
     * @param registro Objeto RegistroPassword a persistir.
     * @param rutaFisica Ruta absoluta del sistema de archivos obtenida por ServletContext.
     * @return true si se guardó con éxito; false en caso de error de E/S.
     */
    public synchronized boolean guardar(RegistroPassword registro, String rutaFisica) {
        File archivo = new File(rutaFisica);

        // Crear la carpeta contenedora (WEB-INF) si el entorno no la hubiese creado
        File carpetaPadre = archivo.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }

        // Escritura en modo append (true) para no sobreescribir las claves anteriores
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(registro.toFileString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera todos los registros almacenados en el archivo físico y los devuelve
     * en una colección ArrayList.
     * 
     * @param rutaFisica Ruta absoluta del archivo vault.txt.
     * @return Lista de objetos RegistroPassword leídos del archivo.
     */
    public synchronized ArrayList<RegistroPassword> obtenerTodos(String rutaFisica) {
        ArrayList<RegistroPassword> lista = new ArrayList<>();
        File archivo = new File(rutaFisica);

        // Si el archivo no existe todavía, devuelve la lista vacía sin fallar
        if (!archivo.exists()) {
            return lista;
        }

        // Lectura secuencial línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    RegistroPassword registro = RegistroPassword.fromFileString(linea);
                    if (registro != null) {
                        lista.add(registro);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }
}