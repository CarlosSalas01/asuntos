// Simulación de base de datos en memoria
let asuntos = [
  {
    id: 1,
    titulo: "Asunto de Ejemplo 1",
    descripcion: "Descripción del primer asunto de ejemplo",
    estado: "Activo",
    fecha: "2024-01-15",
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
  },
  {
    id: 2,
    titulo: "Asunto de Ejemplo 2",
    descripcion: "Descripción del segundo asunto de ejemplo",
    estado: "Pendiente",
    fecha: "2024-01-16",
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
  },
  {
    id: 3,
    titulo: "Asunto de Ejemplo 3",
    descripcion: "Descripción del tercer asunto de ejemplo",
    estado: "Cerrado",
    fecha: "2024-01-17",
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
  },
];

let siguienteId = 4;

// Obtener todos los asuntos
export const obtenerTodosLosAsuntos = async () => {
  // Simular delay de base de datos
  await new Promise((resolve) => setTimeout(resolve, 100));
  return asuntos;
};

// Obtener un asunto por ID
export const obtenerAsuntoPorId = async (id) => {
  await new Promise((resolve) => setTimeout(resolve, 100));
  return asuntos.find((asunto) => asunto.id === parseInt(id));
};

// Crear un nuevo asunto
export const crearAsunto = async (datosAsunto) => {
  await new Promise((resolve) => setTimeout(resolve, 100));

  const nuevoAsunto = {
    id: siguienteId++,
    titulo: datosAsunto.titulo,
    descripcion: datosAsunto.descripcion,
    estado: datosAsunto.estado || "Pendiente",
    fecha: datosAsunto.fecha || new Date().toISOString().split("T")[0],
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
  };

  asuntos.push(nuevoAsunto);
  return nuevoAsunto;
};

// Actualizar un asunto
export const actualizarAsunto = async (id, datosActualizacion) => {
  await new Promise((resolve) => setTimeout(resolve, 100));

  const indice = asuntos.findIndex((asunto) => asunto.id === parseInt(id));

  if (indice === -1) {
    return null;
  }

  asuntos[indice] = {
    ...asuntos[indice],
    ...datosActualizacion,
    id: parseInt(id), // Mantener el ID original
    fechaActualizacion: new Date().toISOString(),
  };

  return asuntos[indice];
};

// Eliminar un asunto
export const eliminarAsunto = async (id) => {
  await new Promise((resolve) => setTimeout(resolve, 100));

  const indice = asuntos.findIndex((asunto) => asunto.id === parseInt(id));

  if (indice === -1) {
    return false;
  }

  asuntos.splice(indice, 1);
  return true;
};
