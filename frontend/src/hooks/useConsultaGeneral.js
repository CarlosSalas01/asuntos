//Este js contiene el hook personalizado useConsultaGeneral, que maneja la lógica
//de la consulta general de asuntos, incluyendo la búsqueda con filtros,
//la carga de áreas, y la navegación a consultas específicas según el tipo de asunto.

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

  //Usamos el useCallback para memorizar la función y evitar recrearla en cada render
  //El parámetro de filtros es un objeto con las propiedades:
  //fechas, fecha1, fecha2, areaFiltro, texto. Es asínncrono porque hace una llamada a la API
  const realizarBusqueda = useCallback(async (filtros) => {
    //Estos setters son para manejar el estado de la búsqueda,
    //el de loading indica que la búsqueda está en curso,
    //y el de error para manejar cualquier error que ocurra
    setLoading(true);
    setError(null);

    try {
      console.log("POST /api/busqueda-general con filtros:", filtros);

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
      } else {
        throw new Error("Respuesta del servidor no válida");
      }
    } catch (error) {
      console.error("Error en búsqueda general:", error);

      //Usamos el error de ENCONNABORTED para manejar timeouts
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
      K: "/consulta-sia",
      C: "/consulta-correos",
      M: "/consulta-comisiones",
      R: "/consulta-reuniones",
      A: "/consulta-acuerdos",
    };

    return rutas[tipoAsunto] || null;
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
