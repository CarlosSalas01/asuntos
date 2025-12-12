import React, { useEffect, useState } from "react";
import { RightArrow } from "../components/icons/CustomIcons.jsx";
import ScrollButton from "../components/ScrollButton.jsx";
import Paginacion, {
  PaginacionNavegacion,
  usePaginacion,
} from "../components/Paginacion.jsx";
import apiService from "../services/apiService.js";

const Correos = ({ className = "" }) => {
  const [correos, setCorreos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroGeneral, setFiltroGeneral] = useState("");
  const [filtroEstatus, setFiltroEstatus] = useState("");
  const [mostrarFiltros, setMostrarFiltros] = useState(true);

  const cargarCorreos = async (search = "") => {
    try {
      setLoading(true);
      const filtros = search ? { search } : {};
      const data = await apiService.obtenerCorreos(filtros);
      setCorreos(data);
    } catch (err) {
      console.error("Error cargando correos:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarCorreos();
  }, []);

  // Filtrar correos en tiempo real
  const correosFiltrados = correos.filter((correo) => {
    // Filtro por estatus del correo
    if (filtroEstatus && correo.estatus !== filtroEstatus) {
      return false;
    }

    // Filtro general (búsqueda local adicional)
    if (!filtroGeneral.trim()) return true;
    const busqueda = filtroGeneral.trim().toLowerCase();

    // Buscar en campos del correo
    const coincideCorreo =
      String(correo.idasunto ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(correo.idconsecutivo ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(correo.descripcion ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(correo.fechaingreso ?? "")
        .toLowerCase()
        .includes(busqueda);

    // Buscar en responsables
    const coincideResponsable = (correo.responsables || []).some((r) =>
      String(r.siglas ?? "")
        .toLowerCase()
        .includes(busqueda)
    );

    return coincideCorreo || coincideResponsable;
  });

  // Hook de paginación
  const {
    paginaActual,
    setPaginaActual,
    registrosPorPagina,
    setRegistrosPorPagina,
    totalRegistros,
    totalPaginas,
    indiceInicio,
    indiceFin,
    datosPaginados: correosPaginados,
  } = usePaginacion(correosFiltrados);

  // Resetear a página 1 cuando cambian los filtros
  useEffect(() => {
    setPaginaActual(1);
  }, [filtroGeneral, filtroEstatus]);

  const getEstatusLabel = (estatus) => {
    switch (estatus) {
      case "A":
        return "Atendido";
      case "P":
        return "Pendiente";
      default:
        return estatus || "";
    }
  };

  const getClasificacionLabel = (fuente) => {
    switch (fuente) {
      case "I":
        return "Interno";
      case "E":
        return "Externo";
      default:
        return fuente || "";
    }
  };

  // Formatear fecha de YYYYMMDD a DD/MM/YYYY
  const formatearFecha = (fecha) => {
    if (!fecha || fecha.length !== 8) return fecha || "";
    const anio = fecha.substring(0, 4);
    const mes = fecha.substring(4, 6);
    const dia = fecha.substring(6, 8);
    return `${dia}/${mes}/${anio}`;
  };

  if (loading) return <div>Cargando...</div>;

  return (
    <div className={`h-full w-full p-3 overflow-auto relative ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Correos</h1>
      </div>
      <div className="my-7"></div>

      {/* Sección de Filtros */}
      <div className="mt-11">
        <h1
          className="font-bold my-2 text-slate-700 dark:text-slate-200 text-lg cursor-pointer select-none flex items-center w-fit"
          onClick={() => setMostrarFiltros(!mostrarFiltros)}
        >
          Filtros
          <RightArrow
            className={`inline-block ml-2 h-5 w-5 text-gray-500 transition-transform duration-300 ease-in-out ${
              mostrarFiltros ? "rotate-90" : "rotate-0"
            }`}
          />
        </h1>
        <div
          className={`flex gap-11 flex-wrap bg-gradient-to-br from-slate-200 to-slate-100 dark:from-slate-800 dark:to-slate-700 p-4 rounded-md transition-all duration-300 ease-in-out overflow-hidden ${
            mostrarFiltros
              ? "max-h-96 opacity-100 mt-2"
              : "max-h-0 opacity-0 p-0 border-0 mt-0"
          }`}
          id="divFiltros"
        >
          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Búsqueda general
            </label>
            <input
              type="text"
              placeholder="Ingresa texto"
              className="py-2 px-2 rounded-md shadow-sm text-slate-800 dark:bg-gray-300 placeholder:text-slate-600"
              value={filtroGeneral}
              onChange={(e) => setFiltroGeneral(e.target.value)}
            />
          </div>
          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Estatus
            </label>
            <select
              className="py-2 px-2 rounded-md shadow-sm text-slate-800 dark:bg-gray-300"
              value={filtroEstatus}
              onChange={(e) => setFiltroEstatus(e.target.value)}
            >
              <option value="">Todos</option>
              <option value="A">Atendido</option>
              <option value="P">Pendiente</option>
            </select>
          </div>
        </div>
        <hr
          className={`border-slate-500 transition-all duration-300 ${
            mostrarFiltros ? "mt-6" : "mt-0 mb-0"
          }`}
        />
      </div>
      <div className="my-7"></div>

      {/* Controles de paginación superior */}
      <Paginacion
        paginaActual={paginaActual}
        totalPaginas={totalPaginas}
        onCambiarPagina={setPaginaActual}
        totalRegistros={totalRegistros}
        registrosPorPagina={registrosPorPagina}
        onCambiarRegistrosPorPagina={setRegistrosPorPagina}
        indiceInicio={indiceInicio}
        indiceFin={indiceFin}
      />

      <div className="rounded-sm shadow-sm overflow-hidden">
        <table className="w-full border border-gray-300 table-fixed">
          <thead className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md rounded-md">
            <tr>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Asunto
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Clasificación
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Remitente
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-64">
                Descripción
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Fecha de envío
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Fecha de vencimiento
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Destinatarios
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Estatus responsable
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-20">
                Avance
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Fecha de atención
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Días de proceso
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Días de retraso
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Observaciones
              </th>
            </tr>
          </thead>
          <tbody>
            {correosPaginados.map((correo) => {
              const responsables = correo.responsables || [];
              const numResp = Math.max(1, responsables.length);

              return (
                <React.Fragment key={correo.idasunto}>
                  <tr>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {correo.idconsecutivo}
                      <br />
                      <span className="text-xs">
                        {getEstatusLabel(correo.estatus)}
                      </span>
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {correo.presidencia === "P" && (
                        <>
                          <b>Presidencia</b>
                          <br />
                        </>
                      )}
                      {getClasificacionLabel(correo.fuente)}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {/* Remitente - Por ahora muestra el idarea, pendiente integrar nombre */}
                      {correo.idarea || "-"}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {correo.descripcion}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {formatearFecha(correo.fechaingreso)}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {correo.urgente}
                      <br />
                      {formatearFecha(correo.fechaatender)}
                    </td>
                    {/* Columnas del primer responsable (sin rowSpan) */}
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.siglas || ""}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {getEstatusLabel(responsables[0]?.estatus)}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.avance
                        ? `${responsables[0].avance}%`
                        : ""}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.fechaatencion ?? ""}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {correo.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {correo.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {correo.observaciones}
                    </td>
                  </tr>

                  {/* Filas adicionales por cada responsable restante */}
                  {responsables.slice(1).map((resp, idx) => (
                    <tr key={`${correo.idasunto}-resp-${idx}`}>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.siglas}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {getEstatusLabel(resp.estatus)}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.avance ?? ""}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.fechaatencion ?? ""}
                      </td>
                    </tr>
                  ))}
                </React.Fragment>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Controles de paginación inferior */}
      <PaginacionNavegacion
        paginaActual={paginaActual}
        totalPaginas={totalPaginas}
        onCambiarPagina={setPaginaActual}
      />

      {/* Botón flotante de scroll */}
      <ScrollButton />
    </div>
  );
};

export default Correos;
