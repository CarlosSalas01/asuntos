// AsuntoBean.js - Migración funcional de AsuntoBean.java
// Representa un asunto y métodos de utilidad para formateo y procesamiento

class AsuntoBean {
  constructor(data = {}) {
    // Asigna todos los atributos del asunto
    this.idasunto = data.idasunto || null;
    this.idconsecutivo = data.idconsecutivo || null;
    this.nocontrol = data.nocontrol || "";
    this.descripcion = data.descripcion || "";
    this.estatus = data.estatus || "";
    this.presidencia = data.presidencia || "";
    this.fechaingreso = data.fechaingreso || "";
    this.fechaoriginal = data.fechaoriginal || "";
    this.fechaUltimaReprogramacion = data.fechaUltimaReprogramacion || "";
    this.responsables = data.responsables || [];
    this.noResponsables = data.noResponsables || 0;
    this.estatustexto = data.estatustexto || "";
    this.descripcionFormatoHTML = data.descripcionFormatoHTML || "";
    // ...agrega más atributos según necesidad...
  }

  // Métodos de utilidad
  getDescripcionFormatoHTML() {
    // Limpia saltos de línea y formatea para HTML
    return (this.descripcion || "").replace(/\n/g, "<br>");
  }

  getFechaingresoFormatoTexto() {
    // Formatea la fecha a texto legible (YYYY-MM-DD)
    if (!this.fechaingreso) return "";
    return this.fechaingreso.substring(0, 10);
  }

  getFechaoriginalFormatoTexto() {
    if (!this.fechaoriginal) return "";
    return this.fechaoriginal.substring(0, 10);
  }

  getReprogramado() {
    // Devuelve true si el asunto tiene reprogramaciones
    return !!this.fechaUltimaReprogramacion;
  }

  setFechaUltimaReprogramacion(valor) {
    this.fechaUltimaReprogramacion = valor;
  }

  setDescripcion(valor) {
    this.descripcion = valor;
  }

  // ...agrega más métodos según necesidad...
}

export default AsuntoBean;
