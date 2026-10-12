package modelo;

import java.io.Serializable;

/**
 * Clase POJO (Plain Old Java Object) que encapsula la información
 * de una contraseña auditada y aprobada para su almacenamiento.
 * Implementa Serializable para permitir su transporte y manipulación en colecciones.
 */
public class RegistroPassword implements Serializable {
    
    // Identificador de versión para la serialización de la clase
    private static final long serialVersionUID = 1L;

    // Atributos privados requeridos por las especificaciones de la práctica
    private String clave;
    private int nivelFortaleza; // Representa el porcentaje calculado (0 a 100)
    private String fechaRegistro;

    /**
     * Constructor por defecto sin parámetros.
     */
    public RegistroPassword() {
    }

    /**
     * Constructor con todos los campos necesarios para inicializar el registro.
     * 
     * @param clave Contraseña que ha sido aprobada.
     * @param nivelFortaleza Puntuación porcentual de la robustez de la clave.
     * @param fechaRegistro Marca temporal de registro (fecha y hora).
     */
    public RegistroPassword(String clave, int nivelFortaleza, String fechaRegistro) {
        this.clave = clave;
        this.nivelFortaleza = nivelFortaleza;
        this.fechaRegistro = fechaRegistro;
    }

    // Métodos de acceso (Getters y Setters)

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getNivelFortaleza() {
        return nivelFortaleza;
    }

    public void setNivelFortaleza(int nivelFortaleza) {
        this.nivelFortaleza = nivelFortaleza;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Convierte el estado actual del objeto en una cadena con formato delimitado
     * por punto y coma (;) para persistirla de forma legible en el archivo vault.txt.
     * 
     * @return Cadena formateada como "clave;fortaleza;fecha".
     */
    public String toFileString() {
        return clave + ";" + nivelFortaleza + ";" + fechaRegistro;
    }

    /**
     * Método factoría que reconstruye un objeto RegistroPassword a partir
     * de una línea extraída del archivo vault.txt.
     * 
     * @param linea Texto leído desde el archivo físico.
     * @return Instancia reconstruida o null si el formato no es válido.
     */
    public static RegistroPassword fromFileString(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }
        
        // Se separan los campos por el delimitador estándar
        String[] partes = linea.split(";");
        if (partes.length >= 3) {
            try {
                String claveLeida = partes[0];
                int fortalezaLeida = Integer.parseInt(partes[1]);
                String fechaLeida = partes[2];
                return new RegistroPassword(claveLeida, fortalezaLeida, fechaLeida);
            } catch (NumberFormatException e) {
                // Control en caso de que el valor numérico esté corrupto
                return null;
            }
        }
        return null;
    }
}