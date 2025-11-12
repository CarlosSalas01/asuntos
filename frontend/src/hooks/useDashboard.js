/**
 * Hook personalizado para el Dashboard - Equivalente a inicio.js
 * Maneja la lógica del dashboard y estadísticas
 */

import { useState, useEffect, useCallback, useRef } from "react";
import axios from "axios";
import { dashboardCache } from "../utils/dashboardCache";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";

export const useDashboard = (user) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [initialized, setInitialized] = useState(false);
  const timeoutRef = useRef(null);

  // Estados del dashboard
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

  // Suscribirse a cambios del caché global
  useEffect(() => {
    const unsubscribe = dashboardCache.subscribe((data) => {
      setDashboardData(data);
      setLoading(false);
    });

    // Cargar datos del caché si existen
    if (dashboardCache.hasValidCache()) {
      const cachedData = dashboardCache.getCache();
      if (cachedData) {
        setDashboardData(cachedData);
      }
    }

    return unsubscribe;
  }, []);

  /**
   * Obtiene el área adjunta basada en el nivel del usuario
   */
  const obtenerAreaAdjunta = useCallback(async (idarea, nivel) => {
    try {
      if (parseInt(nivel) <= 2) {
        return idarea;
      }

      const response = await axios.get(`${API_BASE_URL}/area-superior`, {
        params: { idarea, nivel },
        // Temporalmente sin headers de autenticación
        // headers: {
        //   Authorization: `Bearer ${localStorage.getItem("token")}`,
        // },
      });

      return response.data;
    } catch (error) {
      console.error("Error obteniendo área adjunta:", error);
      return idarea;
    }
  }, []);

  /**
   * Carga el resumen del dashboard - Con caché global
   */
  const cargarResumen = useCallback(
    async (anio = null, idAdjunta = null, nivel = null) => {
      // Verificar si se puede hacer una nueva petición
      if (!dashboardCache.canMakeRequest()) {
        // console.log("Petición bloqueada por throttling o caché");
        return;
      }

      // Si hay datos válidos en caché, usarlos
      if (dashboardCache.hasValidCache()) {
        // console.log("Usando datos del caché global");
        const cachedData = dashboardCache.getCache();
        if (cachedData) {
          setDashboardData(cachedData);
          return;
        }
      }

      dashboardCache.startRequest();
      setLoading(true);
      setError(null);

      try {
        // Determinar área adjunta si no se proporciona
        let areaFinal = idAdjunta;
        if (!areaFinal && user) {
          const userArea = user.areaActual || user.idArea || "1";
          const userNivel = user.nivel || nivel || "1";
          areaFinal = await obtenerAreaAdjunta(userArea, userNivel);
        }

        // console.log("Cargando resumen con parámetros:", {
        //   tipo: "0",
        //   otroAnio: anio,
        //   idAdjunta: areaFinal || "1",
        // });

        const response = await axios.get(`${API_BASE_URL}/resumen-inicio`, {
          params: {
            tipo: "0",
            otroAnio: anio,
            idAdjunta: areaFinal || "1",
          },
          timeout: 10000, // 10 segundos timeout
          // Temporalmente sin headers de autenticación
          // headers: {
          //   Authorization: `Bearer ${localStorage.getItem("token")}`,
          // },
        });

        if (response.data && response.data.length > 0) {
          const processedData = procesarDatosResumen(
            response.data,
            user?.nivel || nivel
          );

          // Guardar en caché global
          dashboardCache.setCache(processedData);

          // console.log("Datos del dashboard cargados correctamente");
        }
      } catch (error) {
        console.error("Error cargando resumen:", error);

        if (error.code === "ECONNABORTED") {
          setError("Tiempo de espera agotado. Intenta nuevamente.");
        } else if (error.response?.status === 401) {
          setError("Sesión expirada. Redirigiendo al login...");
          localStorage.removeItem("token");
          setTimeout(() => (window.location.href = "/login"), 2000);
        } else {
          setError("Error cargando los datos del dashboard");
        }
      } finally {
        dashboardCache.endRequest();
        setLoading(false);
      }
    },
    [user, obtenerAreaAdjunta]
  );

  /**
   * Procesa los datos del resumen recibidos del backend
   */
  const procesarDatosResumen = useCallback((data, nivel) => {
    const [totalesData, ...areasData] = data;

    // Procesar totales generales - Usar datos del backend SQL (método correcto del servlet Java)
    const totalAtendidos = totalesData.atendidosTodos || 0;
    const totalPendientes = totalesData.pendientesTodos || 0;
    const totalGral = totalesData.totalGral || totalAtendidos + totalPendientes;
    // Usar el valor real del backend (consulta SQL corregida)
    const totalReuniones = totalesData.reunionesSA || 0;

    // Log temporal para diagnóstico
    console.log("=== DEBUG Frontend - Datos recibidos ===");
    console.log("reunionesSA del backend:", totalesData.reunionesSA);
    console.log("totalReuniones usado:", totalReuniones);
    console.log("========================================");

    // Procesar datos por área
    const areas = areasData.map((area, index) => {
      // Usar directamente el totalPend del backend (equivalente a numPe en cuadroActual)
      const totalAreaPendientes = area.pendientes || 0;

      // Calcular porcentajes basados en vencidos + porvencer + sinvencer para la barra de progreso
      const sumaParciales =
        (area.vencidos || 0) + (area.porvencer || 0) + (area.sinvencer || 0);
      const pctVencidos =
        sumaParciales > 0 ? (area.vencidos * 100) / sumaParciales : 0;
      const pctPorVencer =
        sumaParciales > 0 ? (area.porvencer * 100) / sumaParciales : 0;
      const pctSinVencer =
        sumaParciales > 0 ? (area.sinvencer * 100) / sumaParciales : 0;

      return {
        id: area.idarea,
        siglas: area.siglas,
        atendidos: parseInt(area.atendidosArea) || 0,
        pendientes: totalAreaPendientes,
        vencidos: area.vencidos || 0,
        porvencer: area.porvencer || 0,
        sinvencer: area.sinvencer || 0,
        porcentajes: {
          vencidos: pctVencidos,
          porvencer: pctPorVencer,
          sinvencer: pctSinVencer,
        },
      };
    });

    const processedData = {
      fechaHora: totalesData.fechaHora || "",
      totales: {
        totalGral,
        totalAtendidos,
        totalPendientes,
        totalReuniones,
      },
      areas,
    };

    // Actualizar estado
    setDashboardData(processedData);

    // Retornar datos procesados para caché
    return processedData;
  }, []);

  /**
   * Consulta pendientes específicos de un área
   */
  const consultarPendientes = useCallback(async (idarea) => {
    try {
      setLoading(true);

      const response = await axios.get(`${API_BASE_URL}/resumen-inicio`, {
        params: {
          tipo: "1",
          idarea: idarea,
        },
        // Temporalmente sin headers de autenticación
        // headers: {
        //   Authorization: `Bearer ${localStorage.getItem("token")}`,
        // },
      });

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
   * Genera años disponibles para filtrado
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
   * Inicialización automática - Con control global de caché
   */
  useEffect(() => {
    if (user && !initialized) {
      // Limpiar timeout anterior si existe
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }

      // Debounce de 1 segundo para estabilidad
      timeoutRef.current = setTimeout(() => {
        setInitialized(true);
        const userArea = user.areaActual || user.idArea || "1";
        const userNivel = user.nivel || "1";
        cargarResumen(null, userArea, userNivel);
      }, 1000);
    }

    // Cleanup del timeout
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, [user, initialized, cargarResumen]);

  return {
    // Estados
    loading,
    error,
    dashboardData,

    // Métodos
    cargarResumen,
    consultarPendientes,
    formatearNumero,
    generarAnios,

    // Utilidades
    setError,
    setLoading,
  };
};
