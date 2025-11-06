import React, { useState } from "react";
import { useFormValidation } from "./ModalEnviarDatos";

const AsuntosSIA = () => {
  const { showValidationModal } = useFormValidation("AsuntosSIA");

  const [formData, setFormData] = useState({
    numeroTurno: "",
    año: "2025",
    numeroControl2: "",
    clasificacion: "",
    prioridad: "",
    destinatario: "",
    destinatariosAsignados: "",
    asunto: "",
    instruccion: "",
    fechaEnvio: "30/10/2025",
    fechaVencimiento: "",
    observaciones: "",
    anexo1: null,
    tipoPresidencia: "con-resp", // 'con-resp' o 'sin-resp'
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      anexo1: e.target.files[0],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validar y mostrar modal
    const isValid = await showValidationModal(formData, (validData) => {
      // Callback que se ejecuta si la validación es exitosa
      console.log("Datos del formulario enviados:", validData);
      // Aquí puedes agregar la lógica para enviar los datos al servidor
    });

    if (isValid) {
      console.log("Formulario válido y enviado");
    }
  };

  const handleClear = () => {
    setFormData({
      numeroTurno: "",
      año: "2025",
      numeroControl2: "",
      clasificacion: "",
      prioridad: "",
      destinatario: "",
      destinatariosAsignados: "",
      asunto: "",
      instruccion: "",
      fechaEnvio: "30/10/2025",
      fechaVencimiento: "",
      observaciones: "",
      anexo1: null,
      tipoPresidencia: "con-resp",
    });
  };

  return (
    <div className="my-3 p-6 min-h-full bg-white dark:bg-gray-800 rounded-lg shadow-md">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 bg-gray-100 dark:bg-gray-700 px-4 py-2 rounded">
          Registro SIA
        </h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Primera fila: Número de turno */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Número de turno
          </label>
          <div className="col-span-3 flex gap-2">
            <input
              type="text"
              name="numeroTurno"
              value={formData.numeroTurno}
              onChange={handleInputChange}
              className="flex-1 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
            <span className="text-gray-500 self-center">/</span>
            <input
              type="text"
              name="año"
              value={formData.año}
              onChange={handleInputChange}
              className="w-16 px-2 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
          <div className="col-span-7 flex gap-4">
            <label className="flex items-center">
              <input
                type="radio"
                name="tipoPresidencia"
                value="con-resp"
                checked={formData.tipoPresidencia === "con-resp"}
                onChange={handleInputChange}
                className="mr-2"
              />
              <span className="text-sm text-gray-700 dark:text-gray-300">
                Presidencia con resp.
              </span>
            </label>
            <label className="flex items-center">
              <input
                type="radio"
                name="tipoPresidencia"
                value="sin-resp"
                checked={formData.tipoPresidencia === "sin-resp"}
                onChange={handleInputChange}
                className="mr-2"
              />
              <span className="text-sm text-gray-700 dark:text-gray-300">
                Presidencia sin resp.
              </span>
            </label>
          </div>
        </div>

        {/* Segunda fila: Número control 2 */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Número de control 2
          </label>
          <div className="col-span-3">
            <input
              type="text"
              name="numeroControl2"
              value={formData.numeroControl2}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Tercera fila: Clasificación */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Clasificación
          </label>
          <div className="col-span-4">
            <select
              name="clasificacion"
              value={formData.clasificacion}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="urgente">Urgente</option>
              <option value="normal">Normal</option>
              <option value="baja">Baja</option>
            </select>
          </div>
        </div>

        {/* Cuarta fila: Prioridad */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Prioridad
          </label>
          <div className="col-span-4">
            <select
              name="prioridad"
              value={formData.prioridad}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="alta">Alta</option>
              <option value="media">Media</option>
              <option value="baja">Baja</option>
            </select>
          </div>
        </div>

        {/* Quinta fila: Destinatario */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Destinatario
          </label>
          <div className="col-span-4 flex gap-2">
            <select
              name="destinatario"
              value={formData.destinatario}
              onChange={handleInputChange}
              className="flex-1 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="direccion-general">Dirección General</option>
              <option value="direccion-adjunta">Dirección Adjunta</option>
              <option value="coordinacion">Coordinación</option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Agregar destinatario
            </button>
          </div>
        </div>

        {/* Sexta fila: Destinatarios asignados */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Destinatarios asignados
          </label>
          <div className="col-span-10">
            <textarea
              name="destinatariosAsignados"
              value={formData.destinatariosAsignados}
              onChange={handleInputChange}
              rows={2}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Séptima fila: Asunto */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Asunto
          </label>
          <div className="col-span-10">
            <textarea
              name="asunto"
              value={formData.asunto}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Octava fila: Instrucción */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Instrucción
          </label>
          <div className="col-span-10">
            <textarea
              name="instruccion"
              value={formData.instruccion}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Novena fila: Fecha de envío */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Fecha de envío
          </label>
          <div className="col-span-3">
            <input
              type="text"
              name="fechaEnvio"
              value={formData.fechaEnvio}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm bg-gray-100 dark:bg-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:text-gray-100"
              readOnly
            />
          </div>
        </div>

        {/* Décima fila: Fecha de vencimiento */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Fecha de vencimiento
          </label>
          <div className="col-span-3">
            <input
              type="date"
              name="fechaVencimiento"
              value={formData.fechaVencimiento}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Undécima fila: Observaciones */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Observaciones
          </label>
          <div className="col-span-10">
            <textarea
              name="observaciones"
              value={formData.observaciones}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Duodécima fila: Anexo 1 */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Anexo 1
          </label>
          <div className="col-span-6">
            <div className="flex">
              <input
                type="file"
                name="anexo1"
                onChange={handleFileChange}
                className="hidden"
                id="file-upload"
              />
              <label
                htmlFor="file-upload"
                className="px-4 py-1 bg-gray-200 hover:bg-gray-300 dark:bg-gray-600 dark:hover:bg-gray-500 border border-gray-300 dark:border-gray-600 rounded-l text-sm cursor-pointer focus:ring-2 focus:ring-blue-500 dark:text-gray-100"
              >
                Elegir archivo
              </label>
              <span className="flex-1 px-3 py-1 border-t border-b border-r border-gray-300 dark:border-gray-600 rounded-r bg-white dark:bg-gray-700 text-sm text-gray-500 dark:text-gray-400">
                {formData.anexo1
                  ? formData.anexo1.name
                  : "No se ha seleccionado ningún archivo"}
              </span>
              <button
                type="button"
                className="ml-2 px-2 py-1 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
                title="Más opciones"
              ></button>
            </div>
          </div>
        </div>

        {/* Botones de acción */}
        <div className="flex gap-4 pt-6 justify-end">
          <button
            type="submit"
            className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Enviar datos
          </button>
          <button
            type="button"
            onClick={handleClear}
            className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Limpiar datos
          </button>
        </div>
      </form>
    </div>
  );
};

export default AsuntosSIA;
