import React, { useState } from "react";
import { useFormValidation } from "./ModalEnviarDatos";

const ConveniosCaptura = () => {
  const { showValidationModal } = useFormValidation("ConveniosCaptura");

  const [formData, setFormData] = useState({
    estatus: "",
    tipoConvenio: "",
    tipoDocumento: "",
    fechaYFirma: "",
    fechaVigencia: "",
    indefinida: false,
    responsables: "",
    responsablesAsignados: "",
    convenioConArea: "",
    convenioDetalle: "",
    objeto: "",
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
      console.log("Datos del convenio enviados:", validData);
      // Aquí puedes agregar la lógica para enviar los datos al servidor
    });

    if (isValid) {
      console.log("Formulario de convenio válido y enviado");
    }
  };

  const handleClear = () => {
    setFormData({
      estatus: "",
      tipoConvenio: "",
      tipoDocumento: "",
      fechaYFirma: "",
      fechaVigencia: "",
      indefinida: false,
      responsables: "",
      responsablesAsignados: "",
      convenioConArea: "",
      convenioDetalle: "",
      objeto: "",
      observaciones: "",
      anexo1: null,
    });
  };

  const handleExitWithoutSaving = () => {
    if (
      window.confirm("¿Está seguro de que desea salir sin guardar los cambios?")
    ) {
      handleClear();
      // Aquí podrías agregar lógica para navegar a otra página
      console.log("Saliendo sin guardar...");
    }
  };

  return (
    <div className="my-3 p-6 min-h-full bg-white dark:bg-gray-800 rounded-lg shadow-md">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 bg-gray-100 dark:bg-gray-700 px-4 py-2 rounded">
          Registro Convenio
        </h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Primera fila: Estatus */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Estatus
          </label>
          <div className="col-span-4">
            <select
              name="estatus"
              value={formData.estatus}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar estatus...</option>
              <option value="borrador">Borrador</option>
              <option value="revision">En revisión</option>
              <option value="aprobado">Aprobado</option>
              <option value="firmado">Firmado</option>
              <option value="vigente">Vigente</option>
              <option value="vencido">Vencido</option>
            </select>
          </div>
        </div>

        {/* Segunda fila: Tipo convenio */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Tipo convenio
          </label>
          <div className="col-span-4">
            <select
              name="tipoConvenio"
              value={formData.tipoConvenio}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar tipo...</option>
              <option value="colaboracion">Colaboración</option>
              <option value="intercambio">Intercambio</option>
              <option value="cooperacion">Cooperación</option>
              <option value="prestacion-servicios">
                Prestación de servicios
              </option>
              <option value="marco">Marco</option>
              <option value="especifico">Específico</option>
            </select>
          </div>
        </div>

        {/* Tercera fila: Tipo de documento */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Tipo de documento
          </label>
          <div className="col-span-6">
            <input
              type="text"
              name="tipoDocumento"
              value={formData.tipoDocumento}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Cuarta fila: Fecha y firma */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Fecha y firma
          </label>
          <div className="col-span-3">
            <input
              type="date"
              name="fechaYFirma"
              value={formData.fechaYFirma}
              onChange={handleInputChange}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Quinta fila: Fecha vigencia con checkbox Indefinida */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Fecha vigencia
          </label>
          <div className="col-span-3">
            <input
              type="date"
              name="fechaVigencia"
              value={formData.fechaVigencia}
              onChange={handleInputChange}
              disabled={formData.indefinida}
              className="w-full px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100 disabled:bg-gray-100 disabled:dark:bg-gray-600"
            />
          </div>
          <div className="col-span-3">
            <label className="flex items-center">
              <input
                type="checkbox"
                name="indefinida"
                checked={formData.indefinida}
                onChange={handleInputChange}
                className="mr-2"
              />
              <span className="text-sm text-gray-700 dark:text-gray-300">
                Indefinida
              </span>
            </label>
          </div>
        </div>

        {/* Sexta fila: Responsables */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Responsables
          </label>
          <div className="col-span-6 flex gap-2">
            <select
              name="responsables"
              value={formData.responsables}
              onChange={handleInputChange}
              className="w-48 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar...</option>
              <option value="direccion-general">Dirección General</option>
              <option value="direccion-adjunta">Dirección Adjunta</option>
              <option value="coordinacion">Coordinación</option>
              <option value="jefatura-departamento">
                Jefatura de Departamento
              </option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 whitespace-nowrap"
            >
              Agrega responsable
            </button>
          </div>
        </div>

        {/* Séptima fila: Responsables asignados */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Responsables asignados
          </label>
          <div className="col-span-10">
            <textarea
              name="responsablesAsignados"
              value={formData.responsablesAsignados}
              onChange={handleInputChange}
              rows={2}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Octava fila: Convenio con */}
        <div className="grid grid-cols-12 gap-4 items-center">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300">
            Convenio con
          </label>
          <div className="col-span-6 flex gap-2">
            <select
              name="convenioConArea"
              value={formData.convenioConArea}
              onChange={handleInputChange}
              className="w-48 px-3 py-1 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            >
              <option value="">Seleccionar área...</option>
              <option value="institucion-publica">Institución pública</option>
              <option value="institucion-privada">Institución privada</option>
              <option value="universidad">Universidad</option>
              <option value="organismo-internacional">
                Organismo internacional
              </option>
              <option value="empresa">Empresa</option>
            </select>
            <button
              type="button"
              className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white text-sm rounded focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 whitespace-nowrap"
            >
              Agrega área
            </button>
          </div>
        </div>

        {/* Novena fila: Convenio detalle (área grande) */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Detalle del convenio
          </label>
          <div className="col-span-10">
            <textarea
              name="convenioDetalle"
              value={formData.convenioDetalle}
              onChange={handleInputChange}
              rows={6}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
            />
          </div>
        </div>

        {/* Décima fila: Objeto */}
        <div className="grid grid-cols-12 gap-4 items-start">
          <label className="col-span-2 text-sm text-gray-700 dark:text-gray-300 pt-1">
            Objeto
          </label>
          <div className="col-span-10">
            <textarea
              name="objeto"
              value={formData.objeto}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-gray-100"
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
                id="convenio-file-upload"
              />
              <label
                htmlFor="convenio-file-upload"
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
            Guardar
          </button>
          <button
            type="button"
            onClick={handleClear}
            className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Limpiar datos
          </button>
          <button
            type="button"
            onClick={handleExitWithoutSaving}
            className="px-3 py-1 bg-blue-800 hover:bg-blue-700 text-white font-medium rounded focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          >
            Salir sin guardar
          </button>
        </div>
      </form>
    </div>
  );
};

export default ConveniosCaptura;
