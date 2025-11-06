/**
 * Singleton para gestionar el caché global del dashboard
 * Evita múltiples peticiones desde diferentes instancias
 */

class DashboardCache {
  constructor() {
    this.cache = null;
    this.loading = false;
    this.subscribers = [];
    this.lastRequest = 0;
  }

  // Suscribir un componente a cambios
  subscribe(callback) {
    this.subscribers.push(callback);
    return () => {
      this.subscribers = this.subscribers.filter((sub) => sub !== callback);
    };
  }

  // Notificar a todos los suscriptores
  notify(data) {
    this.subscribers.forEach((callback) => callback(data));
  }

  // Verificar si hay datos válidos en caché
  hasValidCache() {
    if (!this.cache) return false;
    const now = Date.now();
    return now - this.cache.timestamp < 30000; // 30 segundos
  }

  // Obtener datos del caché
  getCache() {
    return this.cache ? this.cache.data : null;
  }

  // Establecer datos en caché
  setCache(data) {
    this.cache = {
      data,
      timestamp: Date.now(),
    };
    this.notify(data);
  }

  // Verificar si se puede hacer una nueva petición
  canMakeRequest() {
    const now = Date.now();
    if (this.loading) return false;
    if (now - this.lastRequest < 2000) return false; // 2 segundos throttle
    return true;
  }

  // Marcar inicio de petición
  startRequest() {
    this.loading = true;
    this.lastRequest = Date.now();
  }

  // Marcar fin de petición
  endRequest() {
    this.loading = false;
  }

  // Limpiar caché
  clearCache() {
    this.cache = null;
    this.loading = false;
  }
}

// Instancia única
export const dashboardCache = new DashboardCache();
