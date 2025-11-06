import { showError, showSuccess } from "../../services/modalService";

// Configuraciones de validación para cada formulario
const formValidationConfig = {
  AsuntosSIA: {
    requiredFields: [
      { field: "numeroTurno", label: "Número de turno" },
      { field: "clasificacion", label: "Clasificación" },
      { field: "prioridad", label: "Prioridad" },
      { field: "destinatario", label: "Destinatario" },
      { field: "asunto", label: "Asunto" },
      { field: "instruccion", label: "Instrucción" },
      { field: "fechaVencimiento", label: "Fecha de vencimiento" },
    ],
    formName: "Registro SIA",
  },

  CorreosCaptura: {
    requiredFields: [
      { field: "clasificacion", label: "Clasificación" },
      { field: "prioridad", label: "Prioridad" },
      { field: "remitente", label: "Remitente" },
      { field: "asunto", label: "Asunto" },
      { field: "destinatario", label: "Destinatario" },
      { field: "fechaVencimiento", label: "Fecha de vencimiento" },
    ],
    formName: "Registro Correo",
  },

  ReunionesCaptura: {
    requiredFields: [
      { field: "modalidad", label: "Modalidad" },
      { field: "categoria", label: "Categoría" },
      { field: "tema", label: "Tema" },
      { field: "objetivo", label: "Objetivo" },
      { field: "horaInicio", label: "Hora inicio" },
      { field: "horaTermino", label: "Hora término" },
      { field: "lugar", label: "Lugar" },
      { field: "responsable", label: "Responsable" },
    ],
    formName: "Registro Reunión",
  },

  ConveniosCaptura: {
    requiredFields: [
      { field: "estatus", label: "Estatus" },
      { field: "tipoConvenio", label: "Tipo convenio" },
      { field: "tipoDocumento", label: "Tipo de documento" },
      { field: "fechaYFirma", label: "Fecha y firma" },
      { field: "responsables", label: "Responsables" },
      { field: "convenioConArea", label: "Convenio con" },
      { field: "objeto", label: "Objeto" },
    ],
    formName: "Registro Convenio",
  },
};

// Hook personalizado para validación de formularios
export const useFormValidation = (formType) => {
  const validateForm = (formData) => {
    const config = formValidationConfig[formType];

    if (!config) {
      console.error(
        `Configuración de validación no encontrada para: ${formType}`
      );
      return { isValid: false, missingFields: [] };
    }

    const missingFields = [];

    config.requiredFields.forEach(({ field, label }) => {
      const value = formData[field];

      // Verificar si el campo está vacío
      if (!value || (typeof value === "string" && value.trim() === "")) {
        missingFields.push(label);
      }
    });

    return {
      isValid: missingFields.length === 0,
      missingFields,
      formName: config.formName,
    };
  };

  const showValidationModal = async (formData, onSuccess) => {
    const validation = validateForm(formData);

    if (!validation.isValid) {
      // Mostrar modal de error con los campos faltantes
      const fieldsList = validation.missingFields.join(", ");
      await showError(
        "Campos requeridos faltantes",
        `Los siguientes campos deben ser completados: ${fieldsList}`
      );
      return false;
    } else {
      // Mostrar modal de éxito y ejecutar callback
      await showSuccess(
        "¡Formulario válido!",
        `El ${validation.formName} ha sido enviado correctamente`,
        2000
      );

      if (onSuccess) {
        setTimeout(() => {
          onSuccess(formData);
        }, 2000);
      }

      return true;
    }
  };

  return {
    validateForm,
    showValidationModal,
  };
};

// Función auxiliar para validación rápida sin modal
export const validateFormData = (formType, formData) => {
  const config = formValidationConfig[formType];

  if (!config) {
    return { isValid: false, missingFields: [], formName: "" };
  }

  const missingFields = [];

  config.requiredFields.forEach(({ field, label }) => {
    const value = formData[field];

    if (!value || (typeof value === "string" && value.trim() === "")) {
      missingFields.push(label);
    }
  });

  return {
    isValid: missingFields.length === 0,
    missingFields,
    formName: config.formName,
  };
};

// Función para mostrar modal de error de validación
export const showValidationError = async (missingFields) => {
  const fieldsList = missingFields.join(", ");
  return await showError(
    "Todos los campos deben ser llenados",
    `Los siguientes campos son obligatorios: ${fieldsList}`
  );
};

// Función para mostrar modal de éxito de envío
export const showSubmitSuccess = async (formName, onSuccess) => {
  await showSuccess(
    "¡Datos enviados correctamente!",
    `El ${formName} ha sido procesado exitosamente`,
    2000
  );

  if (onSuccess) {
    setTimeout(() => {
      onSuccess();
    }, 2000);
  }
};

// Export por defecto del hook principal
export default useFormValidation;
