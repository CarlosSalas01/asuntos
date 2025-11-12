/**
 * Hook actualizado para usar API externa de SOLR directamente
 */

import { useState, useEffect, useCallback } from "react";
import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:9003/api";

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
    areas: [], // Por ahora vacío, enfoque en totales
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
   * Carga totales desde API externa de SOLR
   */
  const cargarTotales = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      console.log("🌐 Cargando datos del dashboard...");

      // Cargar datos de totales generales
      const responseTotales = await axios.get(
        `${API_BASE_URL}/dashboard/totales`,
        {
          params: {
            fechaInicio: "2025-01-01",
            fechaFin: "2025-11-11",
          },
          timeout: 15000,
        }
      );

      // Cargar datos del resumen de áreas (endpoint original)
      const responseAreas = await axios.get(
        `${API_BASE_URL}/dashboard/resumen-inicio`,
        {
          params: {
            tipo: "0", // Resumen general
            otroAnio: "",
            idAdjunta: "1",
          },
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

        // Procesar datos de áreas como en el hook original
        const areasFormateadas = datosAreas.slice(1).map((area, index) => ({
          id: area.idarea || index + 1,
          siglas: area.siglas || `Área ${index + 1}`,
          nombre: area.nombre || area.siglas,
          atendidos: parseInt(area.atendidos) || 0,
          pendientes: parseInt(area.pendientes) || 0,
          vencidos: parseInt(area.vencidos) || 0,
          porvencer: parseInt(area.porvencer) || 0,
          sinvencer: parseInt(area.sinvencer) || 0,
          porcentajes: calcularPorcentajes(area),
        }));

        setDashboardData({
          fechaHora: new Date(timestamp).toLocaleString(),
          totales: {
            totalGral: totales.totalGral,
            totalAtendidos: totales.totalAtendidos,
            totalPendientes: totales.totalPendientes,
            totalReuniones: totales.totalReuniones,
          },
          areas: areasFormateadas, // Ahora incluimos las áreas
        });
      }
    } catch (error) {
      console.error("❌ Error cargando datos desde API:", error);

      if (error.code === "ECONNABORTED") {
        setError("Tiempo de espera agotado. Intenta nuevamente.");
      } else if (error.response?.status === 401) {
        setError("Sesión expirada. Redirigiendo al login...");
      } else {
        setError("Error cargando los datos del dashboard desde API externa");
      }
    } finally {
      setLoading(false);
    }
  }, [calcularPorcentajes]);

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
   * Inicialización automática
   */
  useEffect(() => {
    cargarTotales();
  }, [cargarTotales]);

  return {
    loading,
    error,
    dashboardData,
    cargarTotales,
    consultarPendientes,
    formatearNumero,
    setError,
    setLoading,
  };
};
