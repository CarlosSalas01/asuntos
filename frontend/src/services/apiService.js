import axios from "axios";

// Configuración base de axios
const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:9003/api";

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para agregar el token a las peticiones
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("authToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar respuestas y errores
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Solo redirigir a login si el error 401 NO viene del endpoint de login
    // (es decir, solo redirigir cuando un token es inválido/expirado, no cuando las credenciales son incorrectas)
    if (
      error.response?.status === 401 &&
      !error.config?.url?.includes("/auth/login")
    ) {
      // Token expirado o inválido en otras peticiones
      localStorage.removeItem("authToken");
      localStorage.removeItem("userData");
      localStorage.removeItem("userRole");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export const apiService = {
  // Autenticación
  login: async (credentials) => {
    try {
      const response = await apiClient.post("/auth/login", credentials);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || "Error de conexión");
    }
  },

  logout: async () => {
    try {
      await apiClient.post("/auth/logout");
    } catch (error) {
      console.error("Error en logout:", error);
    } finally {
      localStorage.removeItem("authToken");
      localStorage.removeItem("userData");
      localStorage.removeItem("userRole");
    }
  },

  // Asuntos
  obtenerAsuntos: async () => {
    try {
      const response = await apiClient.get("/asuntos");
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || "Error al obtener asuntos"
      );
    }
  },

  /**
   * Obtiene asuntos por tipo específico
   * @param {string} tipoAsunto - 'K' (SIA), 'C' (Correos), 'R' (Reuniones), 'A' (Acuerdos), 'M' (Comisiones)
   * @param {Object} filtros - { search, limit, offset }
   */
  obtenerAsuntosPorTipo: async (tipoAsunto, filtros = {}) => {
    try {
      const params = new URLSearchParams();
      if (filtros.search) params.append("search", filtros.search);
      if (filtros.limit) params.append("limit", filtros.limit);
      if (filtros.offset) params.append("offset", filtros.offset);

      const queryString = params.toString() ? `?${params.toString()}` : "";
      const response = await apiClient.get(
        `/sia/asuntos/tipo/${tipoAsunto}${queryString}`
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          `Error al obtener asuntos tipo ${tipoAsunto}`
      );
    }
  },

  // Métodos de conveniencia para cada tipo de asunto
  obtenerAsuntosSIA: async (filtros = {}) =>
    apiService.obtenerAsuntosPorTipo("K", filtros),
  obtenerCorreos: async (filtros = {}) =>
    apiService.obtenerAsuntosPorTipo("C", filtros),
  obtenerReuniones: async (filtros = {}) =>
    apiService.obtenerAsuntosPorTipo("R", filtros),
  obtenerAcuerdos: async (filtros = {}) =>
    apiService.obtenerAsuntosPorTipo("A", filtros),
  obtenerComisiones: async (filtros = {}) =>
    apiService.obtenerAsuntosPorTipo("M", filtros),

  obtenerAsuntoPorId: async (id) => {
    try {
      const response = await apiClient.get(`/asuntos/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || "Error al obtener asunto"
      );
    }
  },

  crearAsunto: async (asunto) => {
    try {
      const response = await apiClient.post("/asuntos", asunto);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || "Error al crear asunto");
    }
  },

  actualizarAsunto: async (id, asunto) => {
    try {
      const response = await apiClient.put(`/asuntos/${id}`, asunto);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || "Error al actualizar asunto"
      );
    }
  },

  eliminarAsunto: async (id) => {
    try {
      const response = await apiClient.delete(`/asuntos/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || "Error al eliminar asunto"
      );
    }
  },
};

export default apiService;
