import React, { useState } from "react";
import { useFormValidation } from "./ModalEnviarDatos";

const ReunionesCaptura = () => {
  const { showValidationModal } = useFormValidation("ReunionesCaptura");

  const [formData, setFormData] = useState({
    modalidad: "",
    categoria: "",
    transversal: false,
    tema: "",
    objetivo: "",
    fecha: "30/10/2025",
    horaInicio: "",
    horaTermino: "",
    lugar: "",
    responsable: "",
    corresponsales: "",
    corresponsalesAsignados: "",
    asistentes1: "",
    asistentes2: "",
    observaciones: "",
    anexo1: null,
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
      console.log("Datos de la reunión enviados:", validData);
      // Aquí puedes agregar la lógica para enviar los datos al servidor
    });

    if (isValid) {
      console.log("Formulario de reunión válido y enviado");
    }
  };

  const handleClear = () => {
    setFormData({
      modalidad: "",
      categoria: "",
      transversal: false,
      tema: "",
      objetivo: "",
      fecha: "30/10/2025",
      horaInicio: "",
      horaTermino: "",
      lugar: "",
      responsable: "",
      corresponsales: "",
      corresponsalesAsignados: "",
      asistentes1: "",
      asistentes2: "",
      observaciones: "",
      anexo1: null,
    });
  };

  return (
    <div className="my-3 p-6 min-h-full bg-white dark:bg-gray-800 rounded-lg shadow-md">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 bg-gray-100 dark:bg-gray-700 px-4 py-2 rounded">
          Registro Reunión
        </h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Primera fila: Modalidad */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Modalidad
          </label>
          <div className="col-span-4">
            <select
              name="modalidad"
              value={formData.modalidad}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar Modalidad...</option>
              <option value="presencial">Presencial</option>
              <option value="virtual">Virtual</option>
              <option value="hibrida">Híbrida</option>
            </select>
          </div>
        </div>

        {/* Segunda fila: Categoría y Transversal */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Categoría
          </label>
          <div className="col-span-4">
            <select
              name="categoria"
              value={formData.categoria}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar Categoría...</option>
              <option value="administrativa">Administrativa</option>
              <option value="tecnica">Técnica</option>
              <option value="directiva">Directiva</option>
              <option value="capacitacion">Capacitación</option>
            </select>
          </div>
          <div className="col-span-6">
            <label className="flex items-center">
              <input
                type="checkbox"
                name="transversal"
                checked={formData.transversal}
                onChange={handleInputChange}
                className="mr-2"
              />
              <span className="text-sm text-gray-700 dark:text-gray-300">
                Transversal
              </span>
            </label>
          </div>
        </div>

        {/* Tercera fila: Tema */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300 pt-1">
            Tema
          </label>
          <div className="col-span-10">
            <textarea
              name="tema"
              value={formData.tema}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Cuarta fila: Objetivo */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300 pt-1">
            Objetivo
          </label>
          <div className="col-span-10">
            <textarea
              name="objetivo"
              value={formData.objetivo}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Quinta fila: Fecha */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Fecha
          </label>
          <div className="col-span-3">
            <input
              type="text"
              name="fecha"
              value={formData.fecha}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm bg-gray-100 dark:bg-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:text-gray-100"
              readOnly
            />
          </div>
        </div>

        {/* Sexta fila: Hora inicio y Hora término */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Hora inicio
          </label>
          <div className="col-span-2">
            <select
              name="horaInicio"
              value={formData.horaInicio}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">HR</option>
              {Array.from({ length: 24 }, (_, i) => (
                <option key={i} value={String(i).padStart(2, "0")}>
                  {String(i).padStart(2, "0")}
                </option>
              ))}
            </select>
          </div>
          <div className="col-span-2">
            <select
              name="horaInicioMin"
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">MIN</option>
              <option value="00">00</option>
              <option value="15">15</option>
              <option value="30">30</option>
              <option value="45">45</option>
            </select>
          </div>
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Hora término
          </label>
          <div className="col-span-2">
            <select
              name="horaTermino"
              value={formData.horaTermino}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">HR</option>
              {Array.from({ length: 24 }, (_, i) => (
                <option key={i} value={String(i).padStart(2, "0")}>
                  {String(i).padStart(2, "0")}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Séptima fila: Lugar */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Lugar
          </label>
          <div className="col-span-10">
            <input
              type="text"
              name="lugar"
              value={formData.lugar}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Octava fila: Responsable */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Responsable
          </label>
          <div className="col-span-4">
            <select
              name="responsable"
              value={formData.responsable}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="direccion-general">Dirección General</option>
              <option value="direccion-adjunta">Dirección Adjunta</option>
              <option value="coordinacion">Coordinación</option>
              <option value="jefatura-departamento">
                Jefatura de Departamento
              </option>
            </select>
          </div>
        </div>

        {/* Novena fila: Corresponsales */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Corresponsales
          </label>
          <div className="col-span-4 flex gap-2">
            <select
              name="corresponsales"
              value={formData.corresponsales}
              onChange={handleInputChange}
              className="flex-1 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="corresponsal-1">Corresponsal 1</option>
              <option value="corresponsal-2">Corresponsal 2</option>
              <option value="corresponsal-3">Corresponsal 3</option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Agregar corresponsal
            </button>
          </div>
        </div>

        {/* Décima fila: Corresponsales asignados */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300 pt-1">
            Corresponsales asignados
          </label>
          <div className="col-span-10">
            <textarea
              name="corresponsalesAsignados"
              value={formData.corresponsalesAsignados}
              onChange={handleInputChange}
              rows={2}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Undécima fila: Asistentes (primera línea) */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Asistentes
          </label>
          <div className="col-span-4 flex gap-2">
            <select
              name="asistentes1"
              value={formData.asistentes1}
              onChange={handleInputChange}
              className="flex-1 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Dirección Adjunta...</option>
              <option value="direccion-adjunta-1">Dirección Adjunta 1</option>
              <option value="direccion-adjunta-2">Dirección Adjunta 2</option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Agregar asistente
            </button>
          </div>
        </div>

        {/* Duodécima fila: Asistentes (segunda línea) */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Asistentes
          </label>
          <div className="col-span-4 flex gap-2">
            <select
              name="asistentes2"
              value={formData.asistentes2}
              onChange={handleInputChange}
              className="flex-1 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Dirección de Área...</option>
              <option value="direccion-area-1">Dirección de Área 1</option>
              <option value="direccion-area-2">Dirección de Área 2</option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Agregar asistente
            </button>
          </div>
        </div>

        {/* Decimotercera fila: Observaciones */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300 pt-1">
            Observaciones
          </label>
          <div className="col-span-10">
            <textarea
              name="observaciones"
              value={formData.observaciones}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-1 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Decimocuarta fila: Anexo 1 */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm font-semibold text-gray-700 dark:text-gray-300">
            Anexo 1
          </label>
          <div className="col-span-6">
            <div className="flex">
              <input
                type="file"
                name="anexo1"
                onChange={handleFileChange}
                className="hidden"
                id="reunion-file-upload"
              />
              <label
                htmlFor="reunion-file-upload"
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

export default ReunionesCaptura;
