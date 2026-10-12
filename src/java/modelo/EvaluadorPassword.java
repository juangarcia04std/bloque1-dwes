package modelo;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que encapsula la lógica de negocio para la auditoría y análisis métrico
 * de contraseñas, así como la generación aleatoria de claves seguras.
 */
public class EvaluadorPassword {

    /**
     * Estructura inmutable para agrupar las métricas devueltas por el análisis.
     */
    public static class ResultadoAuditoria {
        private final int porcentaje;
        private final String tiempoRotura;
        private final List<String> recomendaciones;

        public ResultadoAuditoria(int porcentaje, String tiempoRotura, List<String> recomendaciones) {
            this.porcentaje = porcentaje;
            this.tiempoRotura = tiempoRotura;
            this.recomendaciones = recomendaciones;
        }

        public int getPorcentaje() { 
            return porcentaje; 
        }
        
        public String getTiempoRotura() { 
            return tiempoRotura; 
        }
        
        public List<String> getRecomendaciones() { 
            return recomendaciones; 
        }
    }

    /**
     * Analiza una contraseña evaluando longitud, presencia de minúsculas, mayúsculas,
     * dígitos y caracteres especiales, devolviendo un diagnóstico completo.
     * 
     * @param password Contraseña candidata enviada por el usuario.
     * @return Objeto ResultadoAuditoria con porcentaje, tiempo estimado y recomendaciones.
     */
    public ResultadoAuditoria analizar(String password) {
        // Control de contraseñas nulas, vacías o compuestas exclusivamente de espacios
        if (password == null || password.trim().isEmpty()) {
            List<String> recs = new ArrayList<>();
            recs.add("La contrasena no puede estar vacia.");
            return new ResultadoAuditoria(0, "Inmediato", recs);
        }

        int score = 0;
        List<String> recomendaciones = new ArrayList<>();

        // 1. Criterio de longitud (máximo 30 puntos)
        int longitud = password.length();
        if (longitud >= 14) {
            score += 30;
        } else if (longitud >= 8) {
            score += 20;
        } else {
            score += 5;
            recomendaciones.add("Incrementar la longitud a un minimo de 12 a 14 caracteres.");
        }

        // 2. Criterio de variedad de caracteres (70 puntos distribuidos)
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneNumero = password.matches(".*[0-9].*");
        boolean tieneSimbolo = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        if (tieneMinuscula) {
            score += 15;
        } else {
            recomendaciones.add("Incluir al menos una letra minuscula.");
        }

        if (tieneMayuscula) {
            score += 15;
        } else {
            recomendaciones.add("Incluir al menos una letra mayuscula.");
        }

        if (tieneNumero) {
            score += 20;
        } else {
            recomendaciones.add("Incorporar numeros distribuidos en la clave.");
        }

        if (tieneSimbolo) {
            score += 20;
        } else {
            recomendaciones.add("Incorporar simbolos o caracteres especiales (ej: #, $, %, &).");
        }

        // 3. Estimación del tiempo teórico de descifrado por fuerza bruta
        String tiempoRotura;
        if (score < 40) {
            tiempoRotura = "Pocos segundos o minutos";
        } else if (score < 70) {
            tiempoRotura = "De 2 a 3 semanas";
        } else if (score < 90) {
            tiempoRotura = "Varios meses o anos";
        } else {
            tiempoRotura = "Siglos (practicamente invulnerable por fuerza bruta)";
        }

        return new ResultadoAuditoria(score, tiempoRotura, recomendaciones);
    }

    /**
     * Genera una contraseña aleatoria de 16 caracteres de alta entropía
     * utilizando un generador criptográficamente seguro.
     * 
     * @return Cadena con la contraseña generada.
     */
    public String generarClaveSegura() {
        String universo = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom generadorSeguro = new SecureRandom();
        StringBuilder resultado = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int indice = generadorSeguro.nextInt(universo.length());
            resultado.append(universo.charAt(indice));
        }

        return resultado.toString();
    }
}