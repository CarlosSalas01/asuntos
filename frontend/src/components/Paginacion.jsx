import React from "react";

/**
 * Componente de paginación reutilizable
 * @param {number} paginaActual - Página actual (1-indexed)
 * @param {number} totalPaginas - Total de páginas
 * @param {function} onCambiarPagina - Callback cuando cambia la página
 * @param {number} totalRegistros - Total de registros
 * @param {number} registrosPorPagina - Registros por página
 * @param {function} onCambiarRegistrosPorPagina - Callback cuando cambia registros por página
 * @param {number} indiceInicio - Índice del primer registro mostrado
 * @param {number} indiceFin - Índice del último registro mostrado
 */
const Paginacion = ({
  paginaActual,
  totalPaginas,
  onCambiarPagina,
  totalRegistros,
  registrosPorPagina,
  onCambiarRegistrosPorPagina,
  indiceInicio,
  indiceFin,
}) => {
  const cambiarPagina = (nuevaPagina) => {
    if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
      onCambiarPagina(nuevaPagina);
    }
  };

  return (
    <>
      {/* Controles de paginación superior */}
      <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
        <div className="flex items-center gap-2">
          <label className="text-slate-700 dark:text-slate-200 text-sm">
            Mostrar
          </label>
          <select
            className="py-1 px-2 rounded-md shadow-sm dark:text-slate-200 text-slate-700 border dark:border-gray-400 border-gray-300 dark:bg-slate-700 bg-white"
            value={registrosPorPagina}
            onChange={(e) =>
              onCambiarRegistrosPorPagina(Number(e.target.value))
            }
          >
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
          </select>
          <label className="text-slate-700 dark:text-slate-200 text-sm">
            registros
          </label>
        </div>
        <div className="text-slate-600 dark:text-slate-300 text-sm">
          Mostrando {totalRegistros === 0 ? 0 : indiceInicio + 1} -{" "}
          {Math.min(indiceFin, totalRegistros)} de {totalRegistros} registros
        </div>
      </div>
    </>
  );
};

/**
 * Componente de navegación de páginas (botones de paginación inferior)
 */
export const PaginacionNavegacion = ({
  paginaActual,
  totalPaginas,
  onCambiarPagina,
}) => {
  const cambiarPagina = (nuevaPagina) => {
    if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
      onCambiarPagina(nuevaPagina);
    }
  };

  if (totalPaginas <= 1) return null;

  return (
    <div className="flex flex-wrap items-center justify-center gap-2 mt-7">
      <button
        onClick={() => cambiarPagina(1)}
        disabled={paginaActual === 1}
        className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
      >
        «
      </button>
      <button
        onClick={() => cambiarPagina(paginaActual - 1)}
        disabled={paginaActual === 1}
        className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
      >
        ‹
      </button>

      {/* Números de página */}
      {(() => {
        const paginas = [];
        const maxVisible = 5;
        let inicio = Math.max(1, paginaActual - Math.floor(maxVisible / 2));
        let fin = Math.min(totalPaginas, inicio + maxVisible - 1);

        if (fin - inicio + 1 < maxVisible) {
          inicio = Math.max(1, fin - maxVisible + 1);
        }

        for (let i = inicio; i <= fin; i++) {
          paginas.push(
            <button
              key={i}
              onClick={() => cambiarPagina(i)}
              className={`px-3 py-1 rounded-md transition-colors ${
                i === paginaActual
                  ? "bg-gradient-to-br from-blue-900 to-teal-900 text-white hover:from-teal-800 hover:to-blue-900"
                  : "bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 hover:bg-slate-300 dark:hover:bg-slate-600"
              }`}
            >
              {i}
            </button>
          );
        }
        return paginas;
      })()}

      <button
        onClick={() => cambiarPagina(paginaActual + 1)}
        disabled={paginaActual === totalPaginas}
        className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
      >
        ›
      </button>
      <button
        onClick={() => cambiarPagina(totalPaginas)}
        disabled={paginaActual === totalPaginas}
        className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
      >
        »
      </button>
    </div>
  );
};

/**
 * Hook personalizado para manejar la lógica de paginación
 */
export const usePaginacion = (datos, registrosIniciales = 10) => {
  const [paginaActual, setPaginaActual] = React.useState(1);
  const [registrosPorPagina, setRegistrosPorPagina] =
    React.useState(registrosIniciales);

  // Cálculos de paginación
  const totalRegistros = datos.length;
  const totalPaginas = Math.ceil(totalRegistros / registrosPorPagina);
  const indiceInicio = (paginaActual - 1) * registrosPorPagina;
  const indiceFin = indiceInicio + registrosPorPagina;
  const datosPaginados = datos.slice(indiceInicio, indiceFin);

  // Resetear a página 1 cuando cambian los registros por página
  React.useEffect(() => {
    setPaginaActual(1);
  }, [registrosPorPagina]);

  // Resetear a página 1 cuando cambian los datos filtrados
  React.useEffect(() => {
    if (paginaActual > totalPaginas && totalPaginas > 0) {
      setPaginaActual(1);
    }
  }, [datos.length, totalPaginas, paginaActual]);

  return {
    paginaActual,
    setPaginaActual,
    registrosPorPagina,
    setRegistrosPorPagina,
    totalRegistros,
    totalPaginas,
    indiceInicio,
    indiceFin,
    datosPaginados,
  };
};

export default Paginacion;
