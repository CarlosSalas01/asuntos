import React from "react";
import { useTheme } from "../context/ThemeContext";
import { useNavigate } from "react-router-dom";

/**
 * Componente TablaResultados - Tabla fija de 5 tipos de asunto
 * Basado en consultaAsuntosGeneral.jsp líneas 96-102
 *
 * DATOS FIJOS SIEMPRE (según MAPEO_ARCHIVOS_MIGRACION.md):
 * 1. SIA (código K) → /consulta-sia
 * 2. CORREOS (código C) → /consulta-correos
 * 3. COMISIONES (código M) → /consulta-comisiones
 * 4. REUNIONES (código R) → /consulta-reuniones
 * 5. ACUERDOS (código A) → /consulta-acuerdos
 */
const TablaResultados = ({ datos = [], onClickTipo }) => {
  const { isDarkMode } = useTheme();
  const navigate = useNavigate();

  // Estructura fija de 5 tipos - NUNCA CAMBIAR según instrucciones
  const tiposFijos = [
    { tipoAsunto: "K", descripcion: "SIA", ruta: "/consulta-sia" },
    { tipoAsunto: "C", descripcion: "CORREOS", ruta: "/consulta-correos" },
    {
      tipoAsunto: "M",
      descripcion: "COMISIONES",
      ruta: "/consulta-comisiones",
    },
    { tipoAsunto: "R", descripcion: "REUNIONES", ruta: "/consulta-reuniones" },
    { tipoAsunto: "A", descripcion: "ACUERDOS", ruta: "/consulta-acuerdos" },
  ];

  /**
   * Busca la cantidad para un tipo específico desde los datos del API
   * @param {string} tipo - Código del tipo (K, C, M, R, A)
   * @returns {number} Cantidad encontrada o 0
   */
  const obtenerCantidad = (tipo) => {
    const item = datos.find((d) => d.tipoAsunto === tipo);
    return item ? item.cantidad : 0;
  };

  /**
   * Maneja el click en un número de la tabla
   * @param {Object} tipoInfo - Información del tipo clickeado
   */
  const handleClickNumero = (tipoInfo) => {
    const cantidad = obtenerCantidad(tipoInfo.tipoAsunto);

    console.log(
      `🔍 Click en ${tipoInfo.descripcion} (${tipoInfo.tipoAsunto}): ${cantidad} registros`
    );

    // Llamar al callback del componente padre si existe
    if (onClickTipo) {
      onClickTipo(tipoInfo.tipoAsunto);
    }

    // Navegar a la ruta específica
    navigate(tipoInfo.ruta, {
      state: {
        tipoAsunto: tipoInfo.tipoAsunto,
        descripcion: tipoInfo.descripcion,
        cantidad: cantidad,
      },
    });
  };

  return (
    <div className="overflow-x-auto">
      <table
        className={`min-w-full border-collapse border ${
          isDarkMode ? "border-gray-600" : "border-gray-300"
        }`}
      >
        {/* Header de la tabla */}
        <thead>
          <tr className={isDarkMode ? "bg-gray-700" : "bg-gray-100"}>
            <th
              className={`border px-4 py-3 text-left font-semibold ${
                isDarkMode
                  ? "border-gray-600 text-gray-200"
                  : "border-gray-300 text-gray-700"
              }`}
            >
              Tipo Asunto
            </th>
            <th
              className={`border px-4 py-3 text-center font-semibold ${
                isDarkMode
                  ? "border-gray-600 text-gray-200"
                  : "border-gray-300 text-gray-700"
              }`}
            >
              Registros
            </th>
          </tr>
        </thead>

        {/* Cuerpo de la tabla - SIEMPRE 5 filas fijas */}
        <tbody>
          {tiposFijos.map((tipo, index) => {
            const cantidad = obtenerCantidad(tipo.tipoAsunto);

            return (
              <tr
                key={tipo.tipoAsunto}
                className={`${
                  index % 2 === 0
                    ? isDarkMode
                      ? "bg-gray-800"
                      : "bg-white"
                    : isDarkMode
                    ? "bg-gray-750"
                    : "bg-gray-50"
                } hover:${
                  isDarkMode ? "bg-gray-700" : "bg-blue-50"
                } transition-colors duration-150`}
              >
                {/* Columna: Tipo Asunto */}
                <td
                  className={`border px-4 py-3 ${
                    isDarkMode ? "border-gray-600" : "border-gray-300"
                  }`}
                >
                  <div className="flex items-center">
                    <span className="bg-blue-500 text-white text-xs font-bold px-2 py-1 rounded mr-3">
                      {tipo.tipoAsunto}
                    </span>
                    <span className="font-medium">{tipo.descripcion}</span>
                  </div>
                </td>

                {/* Columna: Registros (números clickeables) */}
                <td
                  className={`border px-4 py-3 text-center ${
                    isDarkMode ? "border-gray-600" : "border-gray-300"
                  }`}
                >
                  <button
                    onClick={() => handleClickNumero(tipo)}
                    className={`text-2xl font-bold px-4 py-2 rounded transition-all duration-200 ${
                      cantidad > 0
                        ? "text-blue-600 hover:text-blue-800 hover:bg-blue-100 dark:text-blue-400 dark:hover:text-blue-300 dark:hover:bg-blue-900 cursor-pointer transform hover:scale-105"
                        : "text-gray-400 dark:text-gray-500 cursor-default"
                    }`}
                    disabled={cantidad === 0}
                    title={
                      cantidad > 0
                        ? `Ver detalle de ${tipo.descripcion}`
                        : "Sin registros"
                    }
                  >
                    {cantidad.toLocaleString()}
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>

      {/* Información adicional */}
      <div className="mt-4 text-sm text-gray-500 dark:text-gray-400">
        <p className="flex items-center">
          <span className="inline-block w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
          Los números son clickeables para ver el detalle específico
        </p>
        {datos.length === 0 && (
          <p className="mt-2 text-yellow-600 dark:text-yellow-400">
            ⚠️ Realiza una búsqueda para ver las cantidades actualizadas
          </p>
        )}
      </div>
    </div>
  );
};

export default TablaResultados;
