import React, { useState } from "react";
import { useFormValidation } from "./ModalEnviarDatos";

const CorreosCaptura = () => {
  const { showValidationModal } = useFormValidation("CorreosCaptura");

  const [formData, setFormData] = useState({
    clasificacion: "",
    prioridad: "",
    remitente: "",
    asunto: "",
    destinatario: "",
    destinatariosAsignados: "",
    fechaEnvio: "30/10/2025",
    fechaVencimiento: "",
    observaciones: "",
    anexo1: null,
    presidencia: false,
  });

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
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
      console.log("Datos del correo enviados:", validData);
      // Aquí puedes agregar la lógica para enviar los datos al servidor
    });

    if (isValid) {
      console.log("Formulario de correo válido y enviado");
    }
  };

  const handleClear = () => {
    setFormData({
      clasificacion: "",
      prioridad: "",
      remitente: "",
      asunto: "",
      destinatario: "",
      destinatariosAsignados: "",
      fechaEnvio: "30/10/2025",
      fechaVencimiento: "",
      observaciones: "",
      anexo1: null,
      presidencia: false,
    });
  };

  return (
    <div className="my-3 p-6 min-h-full bg-white dark:bg-gray-800 rounded-lg shadow-md">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 bg-gray-100 dark:bg-gray-700 px-4 py-2 rounded">
          Registro Correo
        </h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Primera fila: Clasificación y Presidencia */}
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
          <div className="col-span-6">
            <label className="flex items-center">
              <input
                type="checkbox"
                name="presidencia"
                checked={formData.presidencia}
                onChange={handleInputChange}
                className="mr-2"
              />
              <span className="text-sm text-gray-700 dark:text-gray-300">
                Presidencia
              </span>
            </label>
          </div>
        </div>

        {/* Segunda fila: Prioridad */}
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

        {/* Tercera fila: Remitente */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Remitente
          </label>
          <div className="col-span-4">
            <select
              name="remitente"
              value={formData.remitente}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="direccion-general">Dirección General</option>
              <option value="direccion-adjunta">Dirección Adjunta</option>
              <option value="coordinacion">Coordinación</option>
              <option value="externo">Externo</option>
            </select>
          </div>
        </div>

        {/* Cuarta fila: Asunto */}
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
              className="px-3 py-2 bg-gradient-to-br from-blue-900 to-teal-900 text-white text-sm rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
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

        {/* Séptima fila: Fecha de envío */}
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

        {/* Octava fila: Fecha de vencimiento */}
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

        {/* Novena fila: Observaciones */}
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

        {/* Décima fila: Anexo 1 */}
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
                id="correo-file-upload"
              />
              <label
                htmlFor="correo-file-upload"
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
            className="px-3 py-2 bg-gradient-to-br from-blue-900 to-teal-900 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Enviar datos
          </button>
          <button
            type="button"
            onClick={handleClear}
            className="px-3 py-2 bg-gradient-to-br from-blue-900 to-teal-900 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Limpiar datos
          </button>
        </div>
      </form>
    </div>
  );
};

export default CorreosCaptura;
