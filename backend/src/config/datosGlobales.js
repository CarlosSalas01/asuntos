/**
 * Configuración global del sistema - Equivalente a DatosGlobales.java
 * Centraliza todas las configuraciones del sistema, incluyendo base de datos,
 * rutas, versiones, token y autenticación.
 */

// Configuración del entorno
// const ESTADO_PRODUCCION = process.env.NODE_ENV === "production" || false;
// const AUTENTICACION_LDAP = process.env.AUTENTICACION_LDAP === "true" || false;
const ESTADO_PRODUCCION = process.env.NODE_ENV === "production" || false;
const AUTENTICACION_LDAP = process.env.AUTENTICACION_LDAP === "true" || false;

// Versiones del sistema para trazabilidad completa
const VERSION_ORIGINAL = "2018.12.17"; // Última versión estable del sistema Java/JSP
const VERSION_MIGRACION = "2025.11.12"; // Versión de migración a Node.js/React
const VERSION = `${VERSION_MIGRACION} (migrado desde ${VERSION_ORIGINAL})`;

/**
 * Configuración de base de datos basada en DatosGlobales.java
 */
const getDatabaseConfig = () => {
  if (ESTADO_PRODUCCION) {
    // Configuración de producción
    return {
      host: process.env.DB_HOST || "10.153.3.21",
      port: process.env.DB_PORT || 5433,
      database: process.env.DB_NAME || "asuntosdggma_2014",
      user: process.env.DB_USER || "dggma",
      password: process.env.DB_PASSWORD || "asuntos1218",
      ssl: false,
      // Configuración del pool de conexiones (equivalente a BasicDataSource)
      max: 10,
      min: 1,
      idle: 5000,
      acquire: 5000,
      evict: 5000,
    };
  } else {
    // Configuración de desarrollo/prueba
    return {
      host: process.env.DB_HOST || "10.153.3.26",
      port: process.env.DB_PORT || 5438,
      database: process.env.DB_NAME || "asuntosdggma_2014",
      user: process.env.DB_USER || "dggma",
      password: process.env.DB_PASSWORD || "asuntos1218",
      ssl: false,
      // Configuración del pool de conexiones
      max: 10,
      min: 1,
      idle: 5000,
      acquire: 5000,
      evict: 5000,
    };
  }
};

/**
 * Obtiene la ruta del sistema basada en el entorno
 */
const getRutaSistema = () => {
  if (ESTADO_PRODUCCION) {
    return (
      process.env.SISTEMA_URL || "http://10.153.3.31:8082/SistemaSeguimiento/"
    );
  } else {
    return (
      process.env.SISTEMA_URL || "http://10.153.3.31:8082/SistemaSeguimiento/"
    );
  }
};

/**
 * Obtiene la ruta de archivos del sistema
 */
const getRuta = () => {
  if (ESTADO_PRODUCCION) {
    return process.env.FILES_PATH || "/data/asuntosdggma/";
  } else {
    return process.env.FILES_PATH || "d:\\temporal\\asuntosdggma\\";
  }
};

/**
 * Configuración JWT
 */
const getJWTConfig = () => {
  return {
    secret: process.env.JWT_SECRET || "asuntos_ugma_secret_key_2024",
    expiresIn: process.env.JWT_EXPIRES_IN || "8h",
  };
};

/**
 * Año base del sistema
 */
const getAnioBase = () => {
  return "01/01/2010";
};

export default {
  ESTADO_PRODUCCION,
  AUTENTICACION_LDAP,
  VERSION,
  VERSION_ORIGINAL,
  VERSION_MIGRACION,
  getDatabaseConfig,
  getRutaSistema,
  getRuta,
  getJWTConfig,
  getAnioBase,
};
