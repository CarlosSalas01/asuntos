import React, { useEffect, useState } from "react";
import { RightArrow } from "../components/icons/CustomIcons.jsx";
import ScrollButton from "../components/ScrollButton.jsx";
import Paginacion, {
  PaginacionNavegacion,
  usePaginacion,
} from "../components/Paginacion.jsx";
import apiService from "../services/apiService.js";

const Comisiones = ({ className = "" }) => {
  const [comisiones, setComisiones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroGeneral, setFiltroGeneral] = useState("");
  const [filtroEstatus, setFiltroEstatus] = useState("");
  const [mostrarFiltros, setMostrarFiltros] = useState(true);

  const cargarComisiones = async (search = "") => {
    try {
      setLoading(true);
      const filtros = search ? { search } : {};
      const data = await apiService.obtenerComisiones(filtros);
      setComisiones(data);
    } catch (err) {
      console.error("Error cargando comisiones:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarComisiones();
  }, []);

  // Filtrar comisiones en tiempo real
  const comisionesFiltradas = comisiones.filter((comision) => {
    if (filtroEstatus && comision.estatus !== filtroEstatus) {
      return false;
    }

    if (!filtroGeneral.trim()) return true;
    const busqueda = filtroGeneral.trim().toLowerCase();

    const coincideComision =
      String(comision.idasunto ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(comision.idconsecutivo ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(comision.descripcion ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(comision.fechaingreso ?? "")
        .toLowerCase()
        .includes(busqueda);

    const coincideResponsable = (comision.responsables || []).some((r) =>
      String(r.siglas ?? "")
        .toLowerCase()
        .includes(busqueda)
    );

    return coincideComision || coincideResponsable;
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
    datosPaginados: comisionesPaginadas,
  } = usePaginacion(comisionesFiltradas);

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

  if (loading) return <div>Cargando...</div>;

  return (
    <div className={`h-full w-full p-3 overflow-auto relative ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Comisiones</h1>
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
                Comisión
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Comisionados
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Objetivo
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Lugar
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Inicio
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Fin
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Fecha vencimiento
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Anexos
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Observaciones
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Responsable
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Estatus responsable
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Avance
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Fecha de atención
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Días de atención
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Días de retraso
              </th>
            </tr>
          </thead>
          <tbody>
            {comisionesPaginadas.map((comision) => {
              const responsables = comision.responsables || [];
              const numResp = Math.max(1, responsables.length);

              return (
                <React.Fragment key={comision.idasunto}>
                  <tr>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {comision.idconsecutivo}
                      <br />
                      <span className="text-xs">
                        {getEstatusLabel(comision.estatus)}
                      </span>
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {comision.presidencia}
                      <br />
                      {comision.nocontrol}
                      <br />
                      {comision.estatuscorto}
                      <br />
                      {comision.fuente}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.descripcion}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {comision.fechaingreso}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {comision.urgente}
                      <br />
                      {comision.fechaatender}
                    </td>
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
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {comision.observaciones}
                    </td>
                  </tr>

                  {responsables.slice(1).map((resp, idx) => (
                    <tr key={`${comision.idasunto}-resp-${idx}`}>
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

export default Comisiones;
