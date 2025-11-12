import React from "react";
import { useTheme } from "../context/ThemeContext";

/**
 * Componente FormularioFiltros - Formulario de filtros para búsqueda general
 * Basado en consultaAsuntosGeneral.jsp líneas 41-83
 *
 * CAMPOS REQUERIDOS según especificaciones:
 * - fechas (select): fechaingreso, fechaatencion
 * - fecha1 (input date): Fecha inicio
 * - fecha2 (input date): Fecha fin
 * - areaFiltro (select): ID área (0 = todas)
 * - texto (input text): Texto de búsqueda
 */
const FormularioFiltros = ({
  filtros,
  areas,
  onFiltroChange,
  onBuscar,
  onLimpiar,
  loading,
}) => {
  const { isDarkMode } = useTheme();

  /**
   * Maneja cambios en los campos del formulario
   */
  const handleInputChange = (campo, valor) => {
    onFiltroChange(campo, valor);
  };

  /**
   * Maneja el envío del formulario
   */
  const handleSubmit = (e) => {
    e.preventDefault();
    onBuscar(e);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Fila 1: Tipo de fecha y fechas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* Select: Tipo de fecha */}
        <div>
          <label
            className={`block text-sm font-medium mb-2 ${
              isDarkMode ? "text-gray-300" : "text-gray-700"
            }`}
          >
            Tipo de Fecha
          </label>
          <select
            value={filtros.fechas || "fechaingreso"}
            onChange={(e) => handleInputChange("fechas", e.target.value)}
            className={`w-full border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              isDarkMode
                ? "bg-gray-700 border-gray-600 text-white"
                : "bg-white border-gray-300 text-gray-900"
            }`}
          >
            <option value="fechaingreso">Fecha ingreso</option>
            <option value="fechaatencion">Fecha atención</option>
            <option value="fechaenvio">Fecha envío</option>
          </select>
        </div>

        {/* Input: Fecha inicio */}
        <div>
          <label
            className={`block text-sm font-medium mb-2 ${
              isDarkMode ? "text-gray-300" : "text-gray-700"
            }`}
          >
            Fecha Inicio
          </label>
          <input
            type="date"
            value={filtros.fecha1 || ""}
            onChange={(e) => handleInputChange("fecha1", e.target.value)}
            className={`w-full border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              isDarkMode
                ? "bg-gray-700 border-gray-600 text-white"
                : "bg-white border-gray-300 text-gray-900"
            }`}
          />
        </div>

        {/* Input: Fecha fin */}
        <div>
          <label
            className={`block text-sm font-medium mb-2 ${
              isDarkMode ? "text-gray-300" : "text-gray-700"
            }`}
          >
            Fecha Fin
          </label>
          <input
            type="date"
            value={filtros.fecha2 || ""}
            onChange={(e) => handleInputChange("fecha2", e.target.value)}
            className={`w-full border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              isDarkMode
                ? "bg-gray-700 border-gray-600 text-white"
                : "bg-white border-gray-300 text-gray-900"
            }`}
          />
        </div>
      </div>

      {/* Fila 2: Área y texto de búsqueda */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Select: Área */}
        <div>
          <label
            className={`block text-sm font-medium mb-2 ${
              isDarkMode ? "text-gray-300" : "text-gray-700"
            }`}
          >
            Área
          </label>
          <select
            value={filtros.areaFiltro || "0"}
            onChange={(e) => handleInputChange("areaFiltro", e.target.value)}
            className={`w-full border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              isDarkMode
                ? "bg-gray-700 border-gray-600 text-white"
                : "bg-white border-gray-300 text-gray-900"
            }`}
          >
            <option value="0">Todas las áreas</option>
            {areas.map((area) => (
              <option key={area.idarea} value={area.idarea}>
                {area.nombre}
              </option>
            ))}
          </select>
        </div>

        {/* Input: Texto de búsqueda */}
        <div>
          <label
            className={`block text-sm font-medium mb-2 ${
              isDarkMode ? "text-gray-300" : "text-gray-700"
            }`}
          >
            Texto de Búsqueda
          </label>
          <input
            type="text"
            placeholder="Buscar en descripción de asuntos..."
            value={filtros.texto || ""}
            onChange={(e) => handleInputChange("texto", e.target.value)}
            className={`w-full border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              isDarkMode
                ? "bg-gray-700 border-gray-600 text-white placeholder-gray-400"
                : "bg-white border-gray-300 text-gray-900 placeholder-gray-500"
            }`}
          />
        </div>
      </div>

      {/* Botones de acción */}
      <div className="flex flex-col sm:flex-row gap-3 pt-4">
        {/* Botón Buscar */}
        <button
          type="submit"
          disabled={loading}
          className={`flex-1 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-medium py-2 px-6 rounded-md transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
            loading ? "cursor-not-allowed" : "cursor-pointer"
          }`}
        >
          {loading ? (
            <div className="flex items-center justify-center">
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
              Buscando...
            </div>
          ) : (
            <div className="flex items-center justify-center">
              <svg
                className="w-5 h-5 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
              Buscar
            </div>
          )}
        </button>

        {/* Botón Limpiar */}
        <button
          type="button"
          onClick={onLimpiar}
          disabled={loading}
          className={`flex-1 sm:flex-none bg-gray-500 hover:bg-gray-600 disabled:bg-gray-400 text-white font-medium py-2 px-6 rounded-md transition-colors focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 ${
            loading ? "cursor-not-allowed" : "cursor-pointer"
          }`}
        >
          <div className="flex items-center justify-center">
            <svg
              className="w-5 h-5 mr-2"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
            Limpiar
          </div>
        </button>
      </div>

      {/* Información adicional */}
      <div className="text-sm text-gray-500 dark:text-gray-400 pt-2">
        <p className="flex items-center">
          <span className="inline-block w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
          Complete al menos las fechas para realizar una búsqueda efectiva
        </p>
      </div>
    </form>
  );
};

export default FormularioFiltros;
