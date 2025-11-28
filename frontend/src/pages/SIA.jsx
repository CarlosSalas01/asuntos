import React, { useEffect, useState } from "react";
import axios from "axios";
import { useTheme } from "../context/ThemeContext.jsx";

const SIA = ({ className = "" }) => {
  const { isDarkMode } = useTheme();
  const [asuntos, setAsuntos] = useState([]);
  const [loading, setLoading] = useState(true);

  const [idarea, setIdarea] = useState("");
  const [fechaInicio, setFechaInicio] = useState("2025-01-01");
  const [fechaFin, setFechaFin] = useState("2025-12-31");

  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("/api/sia/asuntos", {
        params: { idarea, fechaInicio, fechaFin },
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        setAsuntos(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, [idarea, fechaInicio, fechaFin]);

  if (loading) return <div>Cargando...</div>;
  return (
    <div className={`h-full w-full p-3 ${className}`}>
      <h1 className="text-3xl font-bold my-6">
        Sistema Integral de Asuntos (SIA)
      </h1>
      <div className="my-7"></div>

      <div className="overflow-x-auto rounded-sm shadow-sm">
        <table className="w-full">
          <thead className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md rounded-md">
            <tr>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Asunto
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Clasificación
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Descripción
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Instrucción
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Fecha envío
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Fecha vencimiento
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Anexos
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Acciones
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Destinatarios
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Estatus Responsable
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Avance
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Fecha de atención
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Días Proceso/Atención
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Días retraso
              </th>
              <th className="px-3 py-2 text-sm text-center font-semibold text-white border dark:border-gray-700 border-gray-500">
                Observaciones
              </th>
            </tr>
          </thead>
          <tbody>
            {/* <tr className="bg-white text-center">
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                1
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                208291
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                878/2024
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                ASUNTO PRUEBAS
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                INST. PRUEBAS
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2024-04-29
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2024-04-30
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                -
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                DIRECCIÓN GENERAL ADJUNTA DE INTEGRACIÓN DE INFORMACIÓN
                GEOESPACIAL
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                PENDIENTE
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                0%
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                -
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2024-04-30
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                1
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                0
              </td>
            </tr>
            <tr className="bg-gray-50">
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                208290
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                58/2024
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                ASUNTO TEST
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                PRUEBAS
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2024-04-23
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                2024-04-26
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                -
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center dark:text-slate-700">
                DIRECCIÓN GENERAL ADJUNTA DE INTEGRACIÓN DE INFORMACIÓN
                GEOESPACIAL
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                PENDIENTE
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                0%
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                -
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                2024-04-26
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                3
              </td>
              <td className="px-3 py-2 border border-gray-300 text-center">
                0
              </td>
            </tr> */}
            {asuntos.map((asunto, idx) => (
              <tr key={asunto.idconsecutivo}>
                <td>{idx + 1}</td>
                <td>{asunto.idconsecutivo}</td>
                <td>{asunto.nocontrol}</td>
                <td>{asunto.descripcionFormatoHTML}</td>
                <td>{asunto.estatustexto}</td>
                <td>{asunto.fechaEnvio}</td>
                <td>{asunto.fechaVencimiento}</td>
                <td>{asunto.anexos}</td>
                <td>{asunto.acciones}</td>
                <td>{asunto.acciones}</td>
                <td>{asunto.acciones}</td>
                <td>{asunto.acciones}</td>
                <td>{asunto.acciones}</td>
                <td>{asunto.acciones}</td>
                {asunto.responsables.map((resp, rIdx) => (
                  <td key={rIdx}>{resp.area.nombre}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SIA;
