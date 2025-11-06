/**
 * Componente de tarjeta de área para el dashboard
 */

import React from "react";

const AreaCard = ({ area, onConsultarPendientes, formatearNumero }) => {
  return (
    <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1">
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-950 to-teal-950 dark:from-blue-900 dark:to-teal-950 px-6 py-4">
        <h3 className="text-xl font-semibold text-white text-center">
          {area.siglas}
        </h3>
      </div>

      <div className="p-2">
        {/* Botón Total Pendientes */}
        <div className="text-center mb-5">
          <button
            onClick={() => onConsultarPendientes(area.id)}
            className="inline-flex items-center gap-2 text-zinc-700 dark:text-white font-medium py-3 px-6"
            title="Consultar pendientes"
          >
            Total pendientes
            <span className="bg-gradient-to-r from-blue-700 to-teal-800 text-white px-3 py-1 rounded-full text-sm font-bold">
              {formatearNumero(area.pendientes)}
            </span>
          </button>
        </div>

        {/* Barra de Progreso */}
        <div className="my-6">
          <div className="flex rounded-lg overflow-hidden h-4 bg-gray-200 dark:bg-gray-700 shadow-inner">
            {area.pendientes > 0 ? (
              <>
                {area.porcentajes.vencidos > 0 && (
                  <div
                    className="bg-gradient-to-r from-red-500 to-red-600 transition-all duration-500"
                    style={{ width: `${area.porcentajes.vencidos}%` }}
                    title={`Vencidos: ${area.porcentajes.vencidos.toFixed(1)}%`}
                  ></div>
                )}
                {area.porcentajes.porvencer > 0 && (
                  <div
                    className="bg-gradient-to-r from-yellow-400 to-yellow-500 transition-all duration-500"
                    style={{ width: `${area.porcentajes.porvencer}%` }}
                    title={`Por vencer: ${area.porcentajes.porvencer.toFixed(
                      1
                    )}%`}
                  ></div>
                )}
                {area.porcentajes.sinvencer > 0 && (
                  <div
                    className="bg-gradient-to-r from-blue-500 to-blue-600 transition-all duration-500"
                    style={{ width: `${area.porcentajes.sinvencer}%` }}
                    title={`Sin vencer: ${area.porcentajes.sinvencer.toFixed(
                      1
                    )}%`}
                  ></div>
                )}
              </>
            ) : (
              // Barra completa en verde cuando no hay pendientes
              <div
                className="bg-gradient-to-r from-green-500 to-green-600 transition-all duration-500 w-full flex items-center justify-center"
                title="Sin asuntos pendientes - ¡Todo al día!"
              >
                <span className="text-white text-xs font-bold px-2">
                  ¡Todo al día!
                </span>
              </div>
            )}
          </div>
        </div>

        {/* Desglose con badges */}
        <div className="space-y-3 mb-4 text-sm">
          {/* Primera fila: 3 badges principales */}
          <div className="flex justify-between items-center gap-1">
            <div className="flex items-center justify-center gap-2 bg-red-50 dark:bg-red-900/20 rounded-lg p-2 flex-1 min-w-0">
              <span
                className={`px-2 py-1 rounded-full text-xs font-bold shadow-md flex-shrink-0 ${
                  area.vencidos > 0
                    ? "bg-gradient-to-r from-red-500 to-red-600 text-white"
                    : "bg-gray-200 dark:bg-gray-600 text-gray-600 dark:text-gray-300"
                }`}
              >
                {area.vencidos}
              </span>
              <span className="text-red-700 dark:text-red-300 font-medium text-xs truncate">
                Vencidos
              </span>
            </div>

            <div className="flex items-center justify-center gap-1 bg-yellow-50 dark:bg-yellow-900/20 rounded-lg p-2 flex-1 min-w-0">
              <span
                className={`px-2 py-1 rounded-full text-xs font-bold shadow-md flex-shrink-0 ${
                  area.porvencer > 0
                    ? "bg-gradient-to-r from-yellow-400 to-yellow-500 text-white"
                    : "bg-gray-200 dark:bg-gray-600 text-gray-600 dark:text-gray-300"
                }`}
              >
                {area.porvencer}
              </span>
              <span className="text-yellow-700 dark:text-yellow-300 font-medium text-xs truncate">
                Por vencer
              </span>
            </div>

            <div className="flex items-center justify-center gap-1 bg-blue-50 dark:bg-blue-900/20 rounded-lg p-2 flex-1 min-w-0">
              <span
                className={`px-2 py-1 rounded-full text-xs font-bold shadow-md flex-shrink-0 ${
                  area.sinvencer > 0
                    ? "bg-gradient-to-r from-blue-500 to-blue-600 text-white"
                    : "bg-gray-200 dark:bg-gray-600 text-gray-600 dark:text-gray-300"
                }`}
              >
                {area.sinvencer}
              </span>
              <span className="text-blue-700 dark:text-blue-300 font-medium text-xs truncate">
                Sin vencer
              </span>
            </div>
          </div>

          {/* Segunda fila: Badge de atendidos */}
          <div className="flex justify-center">
            <div className="flex items-center justify-center gap-2 bg-green-100 dark:bg-green-900/20 rounded-lg p-2 px-6">
              <span
                className={`px-3 py-1 rounded-full text-xs font-bold shadow-md ${
                  formatearNumero(area.atendidos) > 0
                    ? "bg-gradient-to-r from-green-500 to-green-700 text-white"
                    : "bg-gray-200 dark:bg-gray-600 text-gray-600 dark:text-gray-300"
                }`}
              >
                {formatearNumero(area.atendidos)}
              </span>
              <span className="text-green-700 dark:text-green-400 font-medium text-sm">
                Atendidos
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AreaCard;
