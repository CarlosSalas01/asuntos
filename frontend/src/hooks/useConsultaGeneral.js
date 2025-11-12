import { useState, useCallback } from "react";
import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:9003/api";

/**
 * Hook personalizado para manejar la consulta general de asuntos
 * Equivalente al procesamiento en BusquedaGeneral.java y DelegadoNegocio
 */
const useConsultaGeneral = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [resultadosBusqueda, setResultadosBusqueda] = useState([]);
  const [areas, setAreas] = useState([]);

  /**
   * Realiza la búsqueda general con los filtros especificados
   * Corregido para usar POST /api/busqueda-general según MAPEO_ARCHIVOS_MIGRACION.md
   */
  const realizarBusqueda = useCallback(async (filtros) => {
    setLoading(true);
    setError(null);

    try {
      console.log("🔍 POST /api/busqueda-general con filtros:", filtros);

      // Usar POST con body según especificaciones exactas
      const response = await axios.post(
        `${API_BASE_URL}/busqueda-general`,
        {
          fechas: filtros.fechas || "fechaingreso",
          fecha1: filtros.fecha1 || "",
          fecha2: filtros.fecha2 || "",
          areaFiltro: filtros.areaFiltro || "0",
          texto: filtros.texto || "",
        },
        {
          timeout: 15000,
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      // La respuesta es directamente el array según consultaGeneralController
      const resultados = response.data;

      if (Array.isArray(resultados)) {
        setResultadosBusqueda(resultados);
        console.log("✅ Búsqueda completada:", resultados);
      } else {
        throw new Error("Respuesta del servidor no válida");
      }
    } catch (error) {
      console.error("❌ Error en búsqueda general:", error);

      if (error.code === "ECONNABORTED") {
        setError("Tiempo de espera agotado. Intenta nuevamente.");
      } else if (error.response?.status === 401) {
        setError("Sesión expirada. Redirigiendo al login...");
      } else {
        setError(
          error.response?.data?.message || "Error realizando la búsqueda"
        );
      }
      setResultadosBusqueda([]);
    } finally {
      setLoading(false);
    }
  }, []);

  /**
   * Carga las áreas disponibles para el filtro
   */
  const cargarAreas = useCallback(async () => {
    try {
      const response = await axios.get(
        `${API_BASE_URL}/busqueda-general/areas`,
        {
          timeout: 10000,
        }
      );

      if (response.data.success) {
        setAreas(response.data.areas || []);
      }
    } catch (error) {
      console.error("❌ Error cargando áreas:", error);
      // No establecer error aquí para no interferir con la búsqueda principal
    }
  }, []);

  /**
   * Resetea el estado del componente
   */
  const resetear = useCallback(() => {
    setResultadosBusqueda([]);
    setError(null);
    setLoading(false);
  }, []);

  /**
   * Navegar a consulta específica según el tipo de asunto
   * Equivalente a RuteaConsultaAsuntos.java - RUTAS CORREGIDAS
   */
  const navegarAConsultaEspecifica = useCallback((tipoAsunto) => {
    const rutas = {
      K: "/consulta-sia", // SIA
      C: "/consulta-correos", // CORREOS
      M: "/consulta-comisiones", // COMISIONES
      R: "/consulta-reuniones", // REUNIONES
      A: "/consulta-acuerdos", // ACUERDOS
    };

    const ruta = rutas[tipoAsunto];
    if (ruta) {
      console.log(
        `🚀 Navegando a consulta específica: ${tipoAsunto} -> ${ruta}`
      );
      return ruta;
    } else {
      console.warn(`⚠️ Tipo de asunto no reconocido: ${tipoAsunto}`);
      return null;
    }
  }, []);

  return {
    // Estado
    loading,
    error,
    resultadosBusqueda,
    areas,

    // Acciones
    realizarBusqueda,
    cargarAreas,
    resetear,
    navegarAConsultaEspecifica,
  };
};

export default useConsultaGeneral;
