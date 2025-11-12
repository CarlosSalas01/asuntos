import React, { useState, useEffect } from "react";
import { useTheme } from "../context/ThemeContext";
import FormularioFiltros from "../components/FormularioFiltros";
import TablaResultados from "../components/TablaResultados";
import useConsultaGeneral from "../hooks/useConsultaGeneral";

/**
 * Componente principal ConsultaAsuntosGeneral
 * Equivalente a consultaAsuntosGeneral.jsp líneas 41-102
 * Combina formulario de filtros + tabla de resultados
 *
 * BASADO EN: consultaAsuntosGeneral.jsp (líneas 41-83) - Formulario de filtros
 *           consultaAsuntosGeneral.jsp (líneas 96-102) - Tabla de resultados
 */
const ConsultaAsuntosGeneral = () => {
  const { isDarkMode } = useTheme();

  // Hook para manejar la lógica de consulta
  const {
    loading,
    error,
    resultadosBusqueda,
    areas,
    realizarBusqueda,
    cargarAreas,
    navegarAConsultaEspecifica,
    resetear,
  } = useConsultaGeneral();

  // Estado inicial de filtros - equivalente a FiltroAsunto del sistema original
  const [filtros, setFiltros] = useState({
    fechas: "fechaingreso", // Equivalente a tipoFecha en FiltroAsunto
    fecha1: "", // Fecha inicio
    fecha2: "", // Fecha fin
    areaFiltro: "0", // ID área (0 = todas las áreas)
    texto: "", // Texto de búsqueda
  });

  // Cargar áreas al montar el componente
  useEffect(() => {
    cargarAreas();
  }, [cargarAreas]);

  /**
   * Maneja el envío del formulario
   * Equivalente a la acción busquedaGeneral.do del JSP original
   */
  const handleBuscar = (e) => {
    e.preventDefault();
    console.log("🔍 Ejecutando búsqueda con filtros:", filtros);
    realizarBusqueda(filtros);
  };

  /**
   * Maneja la actualización de filtros
   */
  const handleFiltroChange = (campo, valor) => {
    setFiltros((prev) => ({
      ...prev,
      [campo]: valor,
    }));
  };

  /**
   * Maneja el click en los números de la tabla
   * Navega a la consulta específica según el tipo de asunto
   */
  const handleClickTipo = (tipoAsunto) => {
    console.log(`🚀 Click en tipo ${tipoAsunto}`);
    const ruta = navegarAConsultaEspecifica(tipoAsunto);

    if (ruta) {
      // Aquí se puede usar react-router navigate
      // Por ahora solo registramos la acción
      console.log(`🎯 Navegando a: ${ruta}`);
      // navigate(ruta, { state: { filtros } }); // Pasar filtros a la página específica
    }
  };

  /**
   * Resetea el formulario y resultados
   */
  const handleLimpiar = () => {
    setFiltros({
      fechas: "fechaingreso",
      fecha1: "",
      fecha2: "",
      areaFiltro: "0",
      texto: "",
    });
    resetear();
  };

  return (
    <div
      className={`min-h-screen p-6 ${
        isDarkMode ? "bg-gray-900 text-white" : "bg-gray-50 text-gray-900"
      }`}
    >
      <div className="max-w-7xl mx-auto">
        {/* Título de la página */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-center mb-2">
            Consulta General de Asuntos
          </h1>
          <p className="text-center text-gray-600 dark:text-gray-400">
            Sistema de búsqueda por tipo de asunto
          </p>
        </div>

        {/* Formulario de Filtros */}
        <div
          className={`mb-8 p-6 rounded-lg shadow-md ${
            isDarkMode ? "bg-gray-800" : "bg-white"
          }`}
        >
          <h2 className="text-xl font-semibold mb-4 flex items-center">
            <span className="bg-blue-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">
              1
            </span>
            Filtros de Búsqueda
          </h2>

          <FormularioFiltros
            filtros={filtros}
            areas={areas}
            onFiltroChange={handleFiltroChange}
            onBuscar={handleBuscar}
            onLimpiar={handleLimpiar}
            loading={loading}
          />
        </div>

        {/* Resultados */}
        <div
          className={`p-6 rounded-lg shadow-md ${
            isDarkMode ? "bg-gray-800" : "bg-white"
          }`}
        >
          <h2 className="text-xl font-semibold mb-4 flex items-center">
            <span className="bg-green-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">
              2
            </span>
            Resultados por Tipo
          </h2>

          {/* Estados de carga y error */}
          {loading && (
            <div className="text-center py-8">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"></div>
              <p className="text-gray-600 dark:text-gray-400">
                Consultando base de datos...
              </p>
            </div>
          )}

          {error && (
            <div className="bg-red-100 dark:bg-red-900 border border-red-400 text-red-700 dark:text-red-200 px-4 py-3 rounded mb-4">
              <strong>Error:</strong> {error}
            </div>
          )}

          {/* Tabla de Resultados */}
          {!loading && !error && (
            <TablaResultados
              datos={resultadosBusqueda}
              onClickTipo={handleClickTipo}
            />
          )}

          {/* Mensaje cuando no hay resultados */}
          {!loading && !error && resultadosBusqueda.length === 0 && (
            <div className="text-center py-8">
              <p className="text-gray-600 dark:text-gray-400">
                Realiza una búsqueda para ver los resultados
              </p>
            </div>
          )}
        </div>

        {/* Información adicional */}
        <div className="mt-6 text-center text-sm text-gray-500 dark:text-gray-400">
          <p>
            Los números en la tabla son clickeables para ver el detalle
            específico de cada tipo
          </p>
        </div>
      </div>
    </div>
  );
};

export default ConsultaAsuntosGeneral;
