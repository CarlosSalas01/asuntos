import React from "react";
import { useTheme } from "../../context/ThemeContext";

/**
 * Componente TablaResultados
 * Equivalente a la tabla renderizada en consultaAsuntosGeneral.jsp
 * Muestra los resultados de la búsqueda general por tipo de asunto
 */
const TablaResultados = ({
  resultados = [],
  loading = false,
  onClickTipoAsunto,
  className = "",
}) => {
  const { isDarkMode } = useTheme();

  if (loading) {
    return (
      <div className={`${className} animate-pulse`}>
        <div
          className={`rounded-lg border ${
            isDarkMode
              ? "bg-gray-800 border-gray-700"
              : "bg-white border-gray-200"
          } overflow-hidden`}
        >
          <div
            className={`px-6 py-4 border-b ${
              isDarkMode
                ? "border-gray-700 bg-gray-750"
                : "border-gray-200 bg-gray-50"
            }`}
          >
            <div className="h-5 bg-gray-300 rounded w-24"></div>
          </div>
          <div className="p-4 space-y-3">
            {[...Array(5)].map((_, i) => (
              <div key={i} className="flex justify-between items-center py-2">
                <div className="h-4 bg-gray-300 rounded w-32"></div>
                <div className="h-4 bg-gray-300 rounded w-16"></div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (!resultados || resultados.length === 0) {
    return (
      <div className={`${className} text-center py-8`}>
        <div
          className={`text-sm ${
            isDarkMode ? "text-gray-400" : "text-gray-500"
          }`}
        >
          No hay resultados para mostrar. Realiza una búsqueda para ver los
          datos.
        </div>
      </div>
    );
  }

  const handleClick = (resultado) => {
    if (onClickTipoAsunto) {
      onClickTipoAsunto(resultado.tipoAsunto, resultado);
    }
  };

  return (
    <div className={className}>
      <div
        className={`rounded-lg border shadow-sm ${
          isDarkMode
            ? "bg-gray-800 border-gray-700"
            : "bg-white border-gray-200"
        } overflow-hidden`}
      >
        {/* Header de la tabla */}
        <div
          className={`px-6 py-4 border-b ${
            isDarkMode
              ? "border-gray-700 bg-gray-750"
              : "border-gray-200 bg-gray-50"
          }`}
        >
          <h3
            className={`text-lg font-semibold ${
              isDarkMode ? "text-white" : "text-gray-900"
            }`}
          >
            📊 Resultados
          </h3>
        </div>

        {/* Tabla */}
        <div className="overflow-hidden">
          <table className="w-full">
            <thead>
              <tr
                className={`border-b ${
                  isDarkMode
                    ? "border-gray-700 bg-gray-775"
                    : "border-gray-200 bg-gray-25"
                }`}
              >
                <th
                  className={`px-6 py-3 text-left text-sm font-medium ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  } uppercase tracking-wider`}
                >
                  Tipo Asunto
                </th>
                <th
                  className={`px-6 py-3 text-center text-sm font-medium ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  } uppercase tracking-wider`}
                >
                  Registros
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
              {resultados.map((resultado, index) => (
                <tr
                  key={resultado.tipoAsunto || index}
                  className={`transition-colors hover:${
                    isDarkMode ? "bg-gray-700" : "bg-gray-50"
                  }`}
                >
                  {/* Columna Tipo Asunto */}
                  <td
                    className={`px-6 py-4 whitespace-nowrap text-sm ${
                      isDarkMode ? "text-white" : "text-gray-900"
                    }`}
                  >
                    <div className="flex items-center">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mr-3 ${getTipoAsuntoColor(
                          resultado.tipoAsunto,
                          isDarkMode
                        )}`}
                      >
                        {resultado.tipoAsunto}
                      </span>
                      {resultado.descripcion}
                    </div>
                  </td>

                  {/* Columna Registros - Clickeable */}
                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <button
                      onClick={() => handleClick(resultado)}
                      className={`text-lg font-semibold transition-colors hover:underline focus:outline-none focus:underline ${
                        resultado.cantidad > 0
                          ? isDarkMode
                            ? "text-blue-400 hover:text-blue-300"
                            : "text-blue-600 hover:text-blue-800"
                          : isDarkMode
                          ? "text-gray-500"
                          : "text-gray-400"
                      }`}
                      disabled={resultado.cantidad === 0}
                      title={`Ver detalles de ${resultado.descripcion}`}
                    >
                      {resultado.cantidad.toLocaleString()}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Footer con totales */}
        <div
          className={`px-6 py-3 border-t ${
            isDarkMode
              ? "border-gray-700 bg-gray-750"
              : "border-gray-200 bg-gray-50"
          }`}
        >
          <div className="flex justify-between items-center text-sm">
            <span className={isDarkMode ? "text-gray-400" : "text-gray-600"}>
              Total de tipos: {resultados.length}
            </span>
            <span
              className={`font-medium ${
                isDarkMode ? "text-gray-300" : "text-gray-700"
              }`}
            >
              Total registros:{" "}
              {resultados
                .reduce((sum, r) => sum + (r.cantidad || 0), 0)
                .toLocaleString()}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

/**
 * Obtiene el color de badge según el tipo de asunto
 */
const getTipoAsuntoColor = (tipoAsunto, isDarkMode) => {
  const colors = {
    K: isDarkMode ? "bg-blue-900 text-blue-200" : "bg-blue-100 text-blue-800",
    C: isDarkMode
      ? "bg-green-900 text-green-200"
      : "bg-green-100 text-green-800",
    M: isDarkMode
      ? "bg-purple-900 text-purple-200"
      : "bg-purple-100 text-purple-800",
    R: isDarkMode
      ? "bg-orange-900 text-orange-200"
      : "bg-orange-100 text-orange-800",
    A: isDarkMode ? "bg-red-900 text-red-200" : "bg-red-100 text-red-800",
  };

  return (
    colors[tipoAsunto] ||
    (isDarkMode ? "bg-gray-900 text-gray-200" : "bg-gray-100 text-gray-800")
  );
};

export default TablaResultados;
