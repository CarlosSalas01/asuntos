import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useTheme } from "../context/ThemeContext";

/**
 * Componente ConsultaSIA - Consulta específica para SIA (código K)
 * Equivalente a ConsultaTurnoKEET.java del sistema original
 */
const ConsultaSIA = () => {
  const { isDarkMode } = useTheme();
  const location = useLocation();
  const navigate = useNavigate();

  // Obtener datos pasados desde TablaResultados
  const { tipoAsunto, descripcion, cantidad } = location.state || {};

  const handleVolver = () => {
    navigate("/consulta-asuntos-general");
  };

  return (
    <div
      className={`min-h-screen p-6 ${
        isDarkMode ? "bg-gray-900 text-white" : "bg-gray-50 text-gray-900"
      }`}
    >
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-3xl font-bold">Consulta SIA</h1>
            <p className="text-gray-600 dark:text-gray-400">
              Detalle específico de asuntos SIA
            </p>
          </div>
          <button
            onClick={handleVolver}
            className="bg-gray-500 hover:bg-gray-700 text-white px-4 py-2 rounded transition-colors"
          >
            ← Volver a Consulta General
          </button>
        </div>

        {/* Información del filtro */}
        {tipoAsunto && (
          <div
            className={`mb-6 p-4 rounded-lg ${
              isDarkMode ? "bg-blue-900" : "bg-blue-100"
            }`}
          >
            <div className="flex items-center">
              <span className="bg-blue-500 text-white px-3 py-1 rounded-full text-sm font-bold mr-3">
                {tipoAsunto}
              </span>
              <span className="font-semibold text-lg">{descripcion}</span>
              <span className="ml-auto text-2xl font-bold text-blue-600">
                {cantidad?.toLocaleString()} registros
              </span>
            </div>
          </div>
        )}

        {/* Contenido específico */}
        <div
          className={`p-6 rounded-lg shadow-md ${
            isDarkMode ? "bg-gray-800" : "bg-white"
          }`}
        >
          <h2 className="text-xl font-semibold mb-4">Listado Detallado</h2>

          <div className="text-center py-8">
            <div className="text-6xl mb-4">🔍</div>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              Consulta específica de SIA en desarrollo
            </p>
            <p className="text-sm text-gray-500 dark:text-gray-500 mt-2">
              Esta sección mostrará el detalle completo de los asuntos SIA
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConsultaSIA;
