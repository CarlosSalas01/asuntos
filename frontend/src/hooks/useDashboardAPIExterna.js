/**
 * Hook actualizado para usar API externa de SOLR directamente
 */

import { useState, useEffect, useCallback } from "react";
import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:9003/api";

// Función para obtener headers con token de autorización
const getAuthHeaders = () => {
  const token = localStorage.getItem("authToken");
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const useDashboardAPIExterna = (user) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [dashboardData, setDashboardData] = useState({
    fechaHora: "",
    totales: {
      totalGral: 0,
      totalAtendidos: 0,
      totalPendientes: 0,
      totalReuniones: 0,
    },
    areas: [],
  });

  /**
   * Calcula porcentajes para la barra de progreso de cada área
   */
  const calcularPorcentajes = useCallback((area) => {
    const vencidos = parseInt(area.vencidos) || 0;
    const porvencer = parseInt(area.porvencer) || 0;
    const sinvencer = parseInt(area.sinvencer) || 0;
    const total = vencidos + porvencer + sinvencer;

    if (total === 0) {
      return { vencidos: 0, porvencer: 0, sinvencer: 0 };
    }

    return {
      vencidos: (vencidos / total) * 100,
      porvencer: (porvencer / total) * 100,
      sinvencer: (sinvencer / total) * 100,
    };
  }, []);

  /**
   * Carga totales desde API externa - Para uso manual/botones
   */
  const cargarTotales = useCallback(
    async (fechaInicio = null, fechaFin = null, idAdjunta = "1") => {
      if (loading) return; // Evitar múltiples cargas simultáneas

      setLoading(true);
      setError(null);

      try {
        // Generar fechas dinámicas si no se proporcionan
        const hoy = new Date();
        const primerDiaAnio = new Date(hoy.getFullYear(), 0, 1);

        const fechaInicioFinal =
          fechaInicio || primerDiaAnio.toISOString().split("T")[0];
        const fechaFinFinal = fechaFin || hoy.toISOString().split("T")[0];

        const [responseTotales, responseAreas] = await Promise.all([
          axios.get(`${API_BASE_URL}/dashboard/totales`, {
            params: { fechaInicio: fechaInicioFinal, fechaFin: fechaFinFinal },
            headers: getAuthHeaders(),
            timeout: 15000,
          }),
          axios.get(`${API_BASE_URL}/dashboard/resumen-inicio`, {
            params: { tipo: "0", otroAnio: "", idAdjunta: idAdjunta },
            headers: getAuthHeaders(),
            timeout: 15000,
          }),
        ]);

        if (responseTotales.data?.totales && responseAreas.data) {
          const { totales, timestamp } = responseTotales.data;
          const datosAreas = responseAreas.data;

          const areasFormateadas = datosAreas
            .slice(1)
            .map((area, index) => ({
              id: area.idarea || area.id || index + 1,
              siglas: area.siglas || `Área ${index + 1}`,
              nombre: area.nombre || area.siglas,
              atendidos:
                parseInt(area.atendidos) || parseInt(area.atendidosArea) || 0,
              pendientes:
                parseInt(area.pendientes) || parseInt(area.totalPend) || 0,
              vencidos: parseInt(area.vencidos) || 0,
              porvencer: parseInt(area.porvencer) || 0,
              sinvencer: parseInt(area.sinvencer) || 0,
              porcentajes: area.porcentajes || calcularPorcentajes(area),
            }))
            .sort((a, b) => a.siglas.localeCompare(b.siglas)); // Ordenar alfabéticamente

          const fechaHoraFinal =
            datosAreas[0]?.fechaHora ||
            (timestamp
              ? new Date(timestamp).toLocaleString()
              : new Date().toLocaleString());

          setDashboardData({
            fechaHora: fechaHoraFinal,
            totales: {
              totalGral: totales.totalGral,
              totalAtendidos: totales.totalAtendidos,
              totalPendientes: totales.totalPendientes,
              totalReuniones: totales.totalReuniones,
            },
            areas: areasFormateadas,
          });

          console.log("Dashboard recargado exitosamente");
        }
      } catch (error) {
        console.error("Error recargando datos:", error);
        setError("Error recargando los datos del dashboard");
      } finally {
        setLoading(false);
      }
    },
    [calcularPorcentajes, loading]
  );

  /**
   * Consulta pendientes (mantener endpoint existente para el modal)
   */
  const consultarPendientes = useCallback(async (idarea) => {
    try {
      setLoading(true);

      const response = await axios.get(
        `${API_BASE_URL}/dashboard/resumen-inicio`,
        {
          params: {
            tipo: "1", // Detalles de pendientes
            idarea: idarea,
          },
          headers: getAuthHeaders(),
          timeout: 10000,
        }
      );

      return response.data;
    } catch (error) {
      console.error("Error consultando pendientes:", error);
      setError("Error cargando detalles de pendientes");
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  /**
   * Formatea números con separadores de miles
   */
  const formatearNumero = useCallback((numero) => {
    return numero.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }, []);

  /**
   * Genera años disponibles para filtrado (funcionalidad del hook original)
   */
  const generarAnios = useCallback(() => {
    const anioActual = new Date().getFullYear();
    const anios = [];
    for (let i = anioActual; i >= 2010; i--) {
      anios.push(i);
    }
    return anios;
  }, []);

  /**
   * Obtiene área adjunta basada en el nivel del usuario (funcionalidad del hook original)
   */
  const obtenerAreaAdjunta = useCallback(async (idarea, nivel) => {
    try {
      if (parseInt(nivel) <= 2) {
        return idarea;
      }

      const response = await axios.get(
        `${API_BASE_URL}/dashboard/area-superior`,
        {
          params: { idarea, nivel },
          headers: getAuthHeaders(),
          timeout: 5000,
        }
      );

      return response.data || idarea;
    } catch (error) {
      console.warn(
        "Error obteniendo área adjunta, usando área original:",
        error
      );
      return idarea;
    }
  }, []);

  /**
   * Inicialización automática con soporte para usuario - SIN BUCLE INFINITO
   */
  useEffect(() => {
    const inicializar = async () => {
      if (loading) return; // Evitar múltiples cargas simultáneas

      setLoading(true);
      setError(null);

      try {
        let idAdjunta = "1";

        // Si hay usuario, determinar área adjunta
        if (user && user.idArea) {
          const userArea = user.idArea; // Usar solo idArea (número)
          const userNivel = user.nivel || "1";

          // Lógica directa sin useCallback para evitar bucle
          if (parseInt(userNivel) > 2) {
            try {
              const response = await axios.get(
                `${API_BASE_URL}/dashboard/area-superior`,
                {
                  params: { idarea: userArea, nivel: userNivel },
                  headers: getAuthHeaders(),
                  timeout: 5000,
                }
              );
              idAdjunta = response.data || userArea;
            } catch (error) {
              console.warn(
                "⚠️ Error obteniendo área adjunta, usando área original"
              );
              idAdjunta = userArea;
            }
          } else {
            idAdjunta = userArea;
          }
        }

        // Cargar datos directamente sin useCallback
        // Generar fechas dinámicas
        const hoy = new Date();
        const primerDiaAnio = new Date(hoy.getFullYear(), 0, 1);

        const fechaInicioFinal = primerDiaAnio.toISOString().split("T")[0]; // Ejemplo: "2025-01-01"
        const fechaFinFinal = hoy.toISOString().split("T")[0]; // Ejemplo: "2025-11-14"

        // Cargar datos de totales generales
        const responseTotales = await axios.get(
          `${API_BASE_URL}/dashboard/totales`,
          {
            params: {
              fechaInicio: fechaInicioFinal,
              fechaFin: fechaFinFinal,
            },
            headers: getAuthHeaders(),
            timeout: 15000,
          }
        );

        // Cargar datos del resumen de áreas
        const responseAreas = await axios.get(
          `${API_BASE_URL}/dashboard/resumen-inicio`,
          {
            params: {
              tipo: "0",
              otroAnio: "",
              idAdjunta: idAdjunta,
            },
            headers: getAuthHeaders(),
            timeout: 15000,
          }
        );

        if (
          responseTotales.data &&
          responseTotales.data.totales &&
          responseAreas.data
        ) {
          const { totales, timestamp } = responseTotales.data;
          const datosAreas = responseAreas.data;

          // Procesar datos de áreas
          const areasFormateadas = datosAreas
            .slice(1)
            .map((area, index) => ({
              id: area.idarea || area.id || index + 1,
              siglas: area.siglas || `Área ${index + 1}`,
              nombre: area.nombre || area.siglas,
              atendidos:
                parseInt(area.atendidos) || parseInt(area.atendidosArea) || 0,
              pendientes:
                parseInt(area.pendientes) || parseInt(area.totalPend) || 0,
              vencidos: parseInt(area.vencidos) || 0,
              porvencer: parseInt(area.porvencer) || 0,
              sinvencer: parseInt(area.sinvencer) || 0,
              porcentajes: area.porcentajes || calcularPorcentajes(area),
            }))
            .sort((a, b) => a.siglas.localeCompare(b.siglas)); // Ordenar alfabéticamente
          const fechaHoraFinal =
            datosAreas[0]?.fechaHora ||
            (timestamp
              ? new Date(timestamp).toLocaleString()
              : new Date().toLocaleString());

          setDashboardData({
            fechaHora: fechaHoraFinal,
            totales: {
              totalGral: totales.totalGral,
              totalAtendidos: totales.totalAtendidos,
              totalPendientes: totales.totalPendientes,
              totalReuniones: totales.totalReuniones,
            },
            areas: areasFormateadas,
          });
        }
      } catch (error) {
        if (error.code === "ECONNABORTED") {
          setError("Tiempo de espera agotado. Intenta nuevamente.");
        } else if (error.response?.status === 401) {
          setError("Sesión expirada. Redirigiendo al login...");
        } else if (error.response?.status === 500) {
          setError("Error interno del servidor. Intenta más tarde.");
        } else {
          setError("Error cargando los datos del dashboard");
        }
      } finally {
        setLoading(false);
      }
    };

    // Solo ejecutar una vez o cuando cambie el usuario
    inicializar();
  }, [user?.areaActual, user?.idArea, user?.nivel]); // DEPENDENCIAS ESPECÍFICAS

  return {
    // Estados
    loading,
    error,
    dashboardData,

    // Métodos principales
    cargarTotales,
    consultarPendientes,

    // Utilidades
    formatearNumero,
    generarAnios,
    obtenerAreaAdjunta,

    // Setters (para compatibilidad)
    setError,
    setLoading,
  };
};
