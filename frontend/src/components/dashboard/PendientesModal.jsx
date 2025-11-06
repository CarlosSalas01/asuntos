/**
 * Modal de detalles de pendientes
 */

import React from "react";

const PendientesModal = ({ isOpen, onClose, modalData }) => {
  if (!isOpen || !modalData) return null;

  // Orden específico de conceptos según el modal original
  const ordenConceptos = [
    "SIA",
    "COMISIONES",
    "CORREOS",
    "ACUERDOS",
    "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS",
  ];

  // Función para obtener datos ordenados según el modal original
  // Ahora procesa datos reales del backend SQL (equivalente al servlet Java original)
  const obtenerDatosOrdenados = () => {
    const resumenData = modalData[0]?.resumen || [];

    console.log("📊 === DATOS DEL MODAL (Backend SQL) ===");
    console.log("Área:", modalData[0]?.area);
    console.log("Resumen recibido:", resumenData);

    return ordenConceptos.map((concepto) => {
      // Buscar el concepto en los datos del resumen usando tipoAbreviado Y tipoasunto
      const item = resumenData.find((r) => {
        // Coincidencia por código abreviado (más preciso)
        if (concepto === "SIA" && r.tipoAbreviado === "K") return true;
        if (concepto === "COMISIONES" && r.tipoAbreviado === "M") return true;
        if (concepto === "CORREOS" && r.tipoAbreviado === "C") return true;
        if (concepto === "ACUERDOS" && r.tipoAbreviado === "A") return true;
        if (
          concepto === "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS" &&
          r.tipoAbreviado === "R"
        )
          return true;

        // Fallback: coincidencia por nombre de tipo (retrocompatibilidad)
        if (concepto === "SIA") {
          return (
            r.tipoasunto &&
            (r.tipoasunto.includes("SOLICITUDES DE INFORMACION") ||
              r.tipoasunto.includes("SIA") ||
              r.tipoasunto === "SOLICITUDES DE INFORMACIÓN")
          );
        }
        if (concepto === "COMISIONES") {
          return r.tipoasunto && r.tipoasunto.includes("COMISIONES");
        }
        if (concepto === "CORREOS") {
          return r.tipoasunto && r.tipoasunto.includes("CORREOS");
        }
        if (concepto === "ACUERDOS") {
          return (
            r.tipoasunto &&
            (r.tipoasunto.includes("ACUERDOS") ||
              r.tipoasunto.includes("ASUNTOS GENERALES"))
          );
        }
        if (concepto === "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS") {
          return r.tipoasunto && r.tipoasunto.includes("REUNIONES");
        }
        return false;
      });

      const resultado = {
        concepto,
        tipoasunto: item?.tipoasunto || concepto,
        tipoAbreviado: item?.tipoAbreviado || "",
        vencidos_d: item?.vencidos_d || 0,
        porvencer_d: item?.porvencer_d || 0,
        pendactivos_d: item?.pendactivos_d || 0,
        // Datos adicionales para enlaces navegables (funcionalidad futura)
        enlaceVencidos: item
          ? `consultarAsuntos.do?tipo=${item.tipoAbreviado}&estado=vencidos&idarea=${modalData[0]?.area?.idarea}`
          : null,
        enlacePorVencer: item
          ? `consultarAsuntos.do?tipo=${item.tipoAbreviado}&estado=porvencer&idarea=${modalData[0]?.area?.idarea}`
          : null,
        enlaceActivos: item
          ? `consultarAsuntos.do?tipo=${item.tipoAbreviado}&estado=activos&idarea=${modalData[0]?.area?.idarea}`
          : null,
      };

      console.log(`📋 ${concepto}:`, resultado);
      return resultado;
    });
  };

  const calcularTotal = (item) => {
    if (item.concepto === "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS") {
      return item.vencidos_d;
    }
    return item.vencidos_d + item.porvencer_d + item.pendactivos_d;
  };

  const calcularTotales = () => {
    const datosOrdenados = obtenerDatosOrdenados();
    let totalVencidos = 0;
    let totalPorVencer = 0;
    let totalActivos = 0;

    datosOrdenados.forEach((item) => {
      totalVencidos += item.vencidos_d;
      if (item.concepto !== "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS") {
        totalPorVencer += item.porvencer_d;
        totalActivos += item.pendactivos_d;
      }
    });

    return {
      totalVencidos,
      totalPorVencer,
      totalActivos,
      total: totalVencidos + totalPorVencer + totalActivos,
    };
  };

  const datosOrdenados = obtenerDatosOrdenados();
  const totales = calcularTotales();

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-gray-800 rounded-lg max-w-5xl w-full max-h-[90vh] overflow-hidden shadow-2xl">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-gray-200 dark:border-gray-700 bg-gradient-to-r from-blue-500 to-blue-600">
          <div className="text-white">
            <h2 className="text-2xl font-bold">{modalData[0]?.area?.siglas}</h2>
            <p className="text-blue-100 text-lg font-medium">
              ASUNTOS PENDIENTES
            </p>
          </div>
          <button
            onClick={onClose}
            className="text-white hover:text-gray-200 transition-colors duration-200 p-2 hover:bg-white/10 rounded-full"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className="p-6 overflow-y-auto max-h-[70vh]">
          {/* Información del área y datos de depuración */}
          <div className="mb-4 p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg border border-blue-200 dark:border-blue-700">
            <div className="flex justify-between items-center text-sm">
              <div className="text-blue-800 dark:text-blue-200">
                <strong>Área:</strong>{" "}
                {modalData[0]?.area?.nombre || modalData[0]?.area?.siglas}
                <span className="ml-2 text-blue-600 dark:text-blue-300">
                  (ID: {modalData[0]?.area?.idarea})
                </span>
              </div>
              <div className="text-blue-600 dark:text-blue-300 text-xs">
                🔍 Datos desde PostgreSQL (SQL directo)
              </div>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="min-w-full table-auto border-collapse">
              <thead>
                <tr className="bg-gray-50 dark:bg-gray-700">
                  <th className="px-6 py-4 text-left text-sm font-bold text-gray-700 dark:text-gray-200 uppercase tracking-wider border-b border-gray-200 dark:border-gray-600">
                    Concepto
                  </th>
                  <th className="px-4 py-4 text-center text-sm font-bold text-gray-700 dark:text-gray-200 uppercase tracking-wider border-b border-gray-200 dark:border-gray-600">
                    Vencidos
                  </th>
                  <th className="px-4 py-4 text-center text-sm font-bold text-gray-700 dark:text-gray-200 uppercase tracking-wider border-b border-gray-200 dark:border-gray-600">
                    Por Vencer
                  </th>
                  <th className="px-4 py-4 text-center text-sm font-bold text-gray-700 dark:text-gray-200 uppercase tracking-wider border-b border-gray-200 dark:border-gray-600">
                    Activos
                  </th>
                  <th className="px-4 py-4 text-center text-sm font-bold text-gray-700 dark:text-gray-200 uppercase tracking-wider border-b border-gray-200 dark:border-gray-600">
                    Total
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                {datosOrdenados.map((item, index) => {
                  const esReunion =
                    item.concepto ===
                    "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS";
                  return (
                    <tr
                      key={index}
                      className="hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors duration-200"
                    >
                      <td className="px-6 py-4 text-sm font-medium text-gray-900 dark:text-white border-b border-gray-100 dark:border-gray-700">
                        {item.concepto}
                      </td>
                      <td className="px-4 py-4 text-sm text-center border-b border-gray-100 dark:border-gray-700">
                        {esReunion ? (
                          <span
                            className="inline-block bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200 px-3 py-1 rounded-full font-semibold cursor-pointer hover:bg-red-200 dark:hover:bg-red-800 transition-colors"
                            onClick={() =>
                              item.vencidos_d > 0 &&
                              console.log(
                                `Navegar a reuniones pendientes: ${item.enlaceVencidos}`
                              )
                            }
                            title={
                              item.vencidos_d > 0
                                ? "Click para ver detalles de reuniones sin acuerdos"
                                : "No hay reuniones pendientes"
                            }
                          >
                            {item.vencidos_d}
                          </span>
                        ) : (
                          <span
                            className={`inline-block bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200 px-3 py-1 rounded-full font-semibold transition-colors ${
                              item.vencidos_d > 0
                                ? "cursor-pointer hover:bg-red-200 dark:hover:bg-red-800"
                                : "opacity-50"
                            }`}
                            onClick={() =>
                              item.vencidos_d > 0 &&
                              console.log(
                                `Navegar a vencidos: ${item.enlaceVencidos}`
                              )
                            }
                            title={
                              item.vencidos_d > 0
                                ? "Click para ver asuntos vencidos"
                                : "No hay asuntos vencidos"
                            }
                          >
                            {item.vencidos_d}
                          </span>
                        )}
                      </td>
                      <td className="px-4 py-4 text-sm text-center border-b border-gray-100 dark:border-gray-700">
                        {esReunion ? (
                          <span
                            className="text-gray-400"
                            title="Las reuniones no tienen fecha de vencimiento"
                          >
                            -
                          </span>
                        ) : (
                          <span
                            className={`inline-block bg-yellow-100 dark:bg-yellow-900 text-yellow-800 dark:text-yellow-200 px-3 py-1 rounded-full font-semibold transition-colors ${
                              item.porvencer_d > 0
                                ? "cursor-pointer hover:bg-yellow-200 dark:hover:bg-yellow-800"
                                : "opacity-50"
                            }`}
                            onClick={() =>
                              item.porvencer_d > 0 &&
                              console.log(
                                `Navegar a por vencer: ${item.enlacePorVencer}`
                              )
                            }
                            title={
                              item.porvencer_d > 0
                                ? "Click para ver asuntos por vencer"
                                : "No hay asuntos por vencer"
                            }
                          >
                            {item.porvencer_d}
                          </span>
                        )}
                      </td>
                      <td className="px-4 py-4 text-sm text-center border-b border-gray-100 dark:border-gray-700">
                        {esReunion ? (
                          <span
                            className="text-gray-400"
                            title="Las reuniones no tienen categoría 'activos'"
                          >
                            -
                          </span>
                        ) : (
                          <span
                            className={`inline-block bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 px-3 py-1 rounded-full font-semibold transition-colors ${
                              item.pendactivos_d > 0
                                ? "cursor-pointer hover:bg-blue-200 dark:hover:bg-blue-800"
                                : "opacity-50"
                            }`}
                            onClick={() =>
                              item.pendactivos_d > 0 &&
                              console.log(
                                `Navegar a activos: ${item.enlaceActivos}`
                              )
                            }
                            title={
                              item.pendactivos_d > 0
                                ? "Click para ver asuntos activos"
                                : "No hay asuntos activos"
                            }
                          >
                            {item.pendactivos_d}
                          </span>
                        )}
                      </td>
                      <td className="px-4 py-4 text-sm text-center font-bold text-gray-900 dark:text-white border-b border-gray-100 dark:border-gray-700">
                        <span className="inline-block bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200 px-4 py-2 rounded-lg font-bold">
                          {calcularTotal(item)}
                        </span>
                      </td>
                    </tr>
                  );
                })}

                {/* Fila de totales */}
                <tr className="bg-gradient-to-r from-blue-50 to-blue-100 dark:from-blue-900/30 dark:to-blue-800/30 font-bold">
                  <td className="px-6 py-4 text-sm font-bold text-gray-900 dark:text-white border-t-2 border-blue-200 dark:border-blue-600">
                    TOTAL
                  </td>
                  <td className="px-4 py-4 text-sm text-center font-bold border-t-2 border-blue-200 dark:border-blue-600">
                    <span className="inline-block bg-red-200 dark:bg-red-800 text-red-900 dark:text-red-100 px-4 py-2 rounded-lg font-bold">
                      {totales.totalVencidos}
                    </span>
                  </td>
                  <td className="px-4 py-4 text-sm text-center font-bold border-t-2 border-blue-200 dark:border-blue-600">
                    <span className="inline-block bg-yellow-200 dark:bg-yellow-800 text-yellow-900 dark:text-yellow-100 px-4 py-2 rounded-lg font-bold">
                      {totales.totalPorVencer}
                    </span>
                  </td>
                  <td className="px-4 py-4 text-sm text-center font-bold border-t-2 border-blue-200 dark:border-blue-600">
                    <span className="inline-block bg-blue-200 dark:bg-blue-800 text-blue-900 dark:text-blue-100 px-4 py-2 rounded-lg font-bold">
                      {totales.totalActivos}
                    </span>
                  </td>
                  <td className="px-4 py-4 text-sm text-center font-bold border-t-2 border-blue-200 dark:border-blue-600">
                    <span className="inline-block bg-gray-300 dark:bg-gray-600 text-gray-900 dark:text-gray-100 px-4 py-2 rounded-lg font-bold text-lg">
                      {totales.total}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        {/* Footer */}
        <div className="px-6 py-4 border-t border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-700 flex justify-end">
          <button
            onClick={onClose}
            className="bg-gradient-to-r from-gray-500 to-gray-600 hover:from-gray-600 hover:to-gray-700 text-white px-6 py-2 rounded-lg transition-all duration-200 font-medium shadow-lg hover:shadow-xl"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
};

export default PendientesModal;
