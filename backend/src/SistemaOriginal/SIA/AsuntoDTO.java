/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.util.List;

/**
 *
 * @author jacqueline
 */
public class AsuntoDTO {
    private int idconsecutivo;
    private int idasunto;
    private int informesperiodicidad;
    private int horasatencionurgente;
    private String descripcion;
    private String fechaingreso;
    private String fechaatender;
    private String fechaatendertexto;
    private String lugar;
    private String responsabletexto;
    private String estatus; //Tal vez ya no se utilice pq se toma del estatus de los responsables
    private String estatuscorto;
    private String estatustexto;
    private String observaciones;
    private String fuente;
    private String clasificacion;  
    private String descargaestatus;
    private String fechadescarga;
    private String publicoestatus;
    private String activoestatus;
    private String fechaalta;
    private String fechamodificacion;
    private String informesunidad;
    private String tipoatencion;
    private String tipoasunto;
    private String resumenacciones;
    private boolean asuntoDebeMostrarse;
    private boolean responsableEdicion;    
    private int idarea;
    private int idusuariomodificacion;
    //Nuevo
    private String nocontrol;
    private String asistentes;
    private String urgente;
    private String presidencia;
    private String modalidadreunion;
    private int idarchivocierre;
    private String fechaoriginal;
    private String reprograma;
    private int categoria;
    private String categoriaTxt;
    private boolean transversal;
    
    /**
     * @return the idasunto
     */
    public int getIdasunto() {
        return idasunto;
    }

    /**
     * @param idasunto the idasunto to set
     */
    public void setIdasunto(int idasunto) {
        this.idasunto = idasunto;
    }

    /**
     * @return the informesperiodicidad
     */
    public int getInformesperiodicidad() {
        return informesperiodicidad;
    }

    /**
     * @param informesperiodicidad the informesperiodicidad to set
     */
    public void setInformesperiodicidad(int informesperiodicidad) {
        this.informesperiodicidad = informesperiodicidad;
    }

    /**
     * @return the horasatencionurgente
     */
    public int getHorasatencionurgente() {
        return horasatencionurgente;
    }

    /**
     * @param horasatencionurgente the horasatencionurgente to set
     */
    public void setHorasatencionurgente(int horasatencionurgente) {
        this.horasatencionurgente = horasatencionurgente;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fechaingreso
     */
    public String getFechaingreso() {
        return fechaingreso;
    }

    /**
     * @param fechaingreso the fechaingreso to set
     */
    public void setFechaingreso(String fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    /**
     * @return the fechaatender
     */
    public String getFechaatender() {
        return fechaatender;
    }

    /**
     * @param fechaatender the fechaatender to set
     */
    public void setFechaatender(String fechaatender) {
        this.fechaatender = fechaatender;
    }

    /**
     * @return the fechaatendertexto
     */
    public String getFechaatendertexto() {
        return fechaatendertexto;
    }

    /**
     * @param fechaatendertexto the fechaatendertexto to set
     */
    public void setFechaatendertexto(String fechaatendertexto) {
        this.fechaatendertexto = fechaatendertexto;
    }

    /**
     * @return the lugar
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * @param lugar the lugar to set
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * @return the responsabletexto
     */
    public String getResponsabletexto() {
        return responsabletexto;
    }

    /**
     * @param responsabletexto the responsabletexto to set
     */
    public void setResponsabletexto(String responsabletexto) {
        this.responsabletexto = responsabletexto;
    }

    /**
     * @return the estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the estatuscorto
     */
    public String getEstatuscorto() {
        return estatuscorto;
    }

    /**
     * @param estatuscorto the estatuscorto to set
     */
    public void setEstatuscorto(String estatuscorto) {
        this.estatuscorto = estatuscorto;
    }

    /**
     * @return the estatustexto
     */
    public String getEstatustexto() {
        return estatustexto;
    }

    /**
     * @param estatustexto the estatustexto to set
     */
    public void setEstatustexto(String estatustexto) {
        this.estatustexto = estatustexto;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @return the fuente
     */
    public String getFuente() {
        return fuente;
    }

    /**
     * @param fuente the fuente to set
     */
    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    /**
     * @return the clasificacion
     */
    public String getClasificacion() {
        return clasificacion;
    }

    /**
     * @param clasificacion the clasificacion to set
     */
    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    /**
     * @return the descargaestatus
     */
    public String getDescargaestatus() {
        return descargaestatus;
    }

    /**
     * @param descargaestatus the descargaestatus to set
     */
    public void setDescargaestatus(String descargaestatus) {
        this.descargaestatus = descargaestatus;
    }

    /**
     * @return the fechadescarga
     */
    public String getFechadescarga() {
        return fechadescarga;
    }

    /**
     * @param fechadescarga the fechadescarga to set
     */
    public void setFechadescarga(String fechadescarga) {
        this.fechadescarga = fechadescarga;
    }

    /**
     * @return the publicoestatus
     */
    public String getPublicoestatus() {
        return publicoestatus;
    }

    /**
     * @param publicoestatus the publicoestatus to set
     */
    public void setPublicoestatus(String publicoestatus) {
        this.publicoestatus = publicoestatus;
    }

    /**
     * @return the activoestatus
     */
    public String getActivoestatus() {
        return activoestatus;
    }

    /**
     * @param activoestatus the activoestatus to set
     */
    public void setActivoestatus(String activoestatus) {
        this.activoestatus = activoestatus;
    }

    /**
     * @return the fechaalta
     */
    public String getFechaalta() {
        return fechaalta;
    }

    /**
     * @param fechaalta the fechaalta to set
     */
    public void setFechaalta(String fechaalta) {
        this.fechaalta = fechaalta;
    }

    /**
     * @return the fechamodificacion
     */
    public String getFechamodificacion() {
        return fechamodificacion;
    }

    /**
     * @param fechamodificacion the fechamodificacion to set
     */
    public void setFechamodificacion(String fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }


    /**
     * @return the informesunidad
     */
    public String getInformesunidad() {
        return informesunidad;
    }

    /**
     * @param informesunidad the informesunidad to set
     */
    public void setInformesunidad(String informesunidad) {
        this.informesunidad = informesunidad;
    }

    /**
     * @return the tipoatencion
     */
    public String getTipoatencion() {
        return tipoatencion;
    }

    /**
     * @param tipoatencion the tipoatencion to set
     */
    public void setTipoatencion(String tipoatencion) {
        this.tipoatencion = tipoatencion;
    }

    /**
     * @return the tipoasunto
     */
    public String getTipoasunto() {
        return tipoasunto;
    }

    /**
     * @param tipoasunto the tipoasunto to set
     */
    public void setTipoasunto(String tipoasunto) {
        this.tipoasunto = tipoasunto;
    }

    /**
     * @return the administrador
     */
//    public String getAdministrador() {
//        //return administrador == null? "":administrador ;
//        return administrador;
//    }
//
//    /**
//     * @param administrador the administrador to set
//     */
//    public void setAdministrador(String administrador) {
//        this.administrador = administrador;
//    }

    /**
     * @return the resumenacciones
     */
    public String getResumenacciones() {
        return resumenacciones;
    }

    /**
     * @param resumenacciones the resumenacciones to set
     */
    public void setResumenacciones(String resumenacciones) {
        this.resumenacciones = resumenacciones;
    }

    /**
     * @return the asuntoDebeMostrarse
     */
    public boolean isAsuntoDebeMostrarse() {
        return asuntoDebeMostrarse;
    }

    /**
     * @param asuntoDebeMostrarse the asuntoDebeMostrarse to set
     */
    public void setAsuntoDebeMostrarse(boolean asuntoDebeMostrarse) {
        this.asuntoDebeMostrarse = asuntoDebeMostrarse;
    }

    /**
     * @return the responsableEdicion
     */
    public boolean isResponsableEdicion() {
        return responsableEdicion;
    }

    /**
     * @param responsableEdicion the responsableEdicion to set
     */
    public void setResponsableEdicion(boolean responsableEdicion) {
        this.responsableEdicion = responsableEdicion;
    }

    /**
     * @return the idarea
     */
    public int getIdarea() {
        return idarea;
    }

    /**
     * @param idarea the idarea to set
     */
    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    /**
     * @return the idusuariomodificacion
     */
    public int getIdusuariomodificacion() {
        return idusuariomodificacion;
    }

    /**
     * @param idusuariomodificacion the idusuariomodificacion to set
     */
    public void setIdusuariomodificacion(int idusuariomodificacion) {
        this.idusuariomodificacion = idusuariomodificacion;
    }

    /**
     * @return the nocontrol
     */
    public String getNocontrol() {
        return nocontrol;
    }

    /**
     * @param nocontrol the nocontrol to set
     */
    public void setNocontrol(String nocontrol) {
        this.nocontrol = nocontrol;
    }

    /**
     * @return the asistentes
     */
    public String getAsistentes() {
        return asistentes;
    }

    /**
     * @param asistentes the asistentes to set
     */
    public void setAsistentes(String asistentes) {
        this.asistentes = asistentes;
    }

    /**
     * @return the urgente
     */
    public String getUrgente() {
        return urgente;
    }

    /**
     * @param urgente the urgente to set
     */
    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    /**
     * @return the idconsecutivo
     */
    public int getIdconsecutivo() {
        return idconsecutivo;
    }

    /**
     * @param idconsecutivo the idconsecutivo to set
     */
    public void setIdconsecutivo(int idconsecutivo) {
        this.idconsecutivo = idconsecutivo;
    }

    /**
     * @return the presidencia
     */
    public String getPresidencia() {
        return presidencia;
    }

    /**
     * @param presidencia the presidencia to set
     */
    public void setPresidencia(String presidencia) {
        this.presidencia = presidencia;
    }

    /**
     * @return the modalidadreunion
     */
    public String getModalidadreunion() {
        return modalidadreunion;
    }

    /**
     * @param modalidadreunion the modalidadreunion to set
     */
    public void setModalidadreunion(String modalidadreunion) {
        this.modalidadreunion = modalidadreunion;
    }

   

    /**
     * @return the idarchivocierre
     */
    public int getIdarchivocierre() {
        return idarchivocierre;
    }

    /**
     * @param idarchivocierre the idarchivocierre to set
     */
    public void setIdarchivocierre(int idarchivocierre) {
        this.idarchivocierre = idarchivocierre;
    }

    /**
     * @return the reprograma
     */
    public String getReprograma() {
        return reprograma;
    }

    /**
     * @param reprograma the reprograma to set
     */
    public void setReprograma(String reprograma) {
        this.reprograma = reprograma;
    }

    /**
     * @return the fechaoriginal
     */
    public String getFechaoriginal() {
        return fechaoriginal;
    }

    /**
     * @param fechaoriginal the fechaoriginal to set
     */
    public void setFechaoriginal(String fechaoriginal) {
        this.fechaoriginal = fechaoriginal;
    }

    /**
     * @return the categoria
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the categoriaTxt
     */
    public String getCategoriaTxt() {
        return categoriaTxt;
    }

    /**
     * @param categoriaTxt the categoriaTxt to set
     */
    public void setCategoriaTxt(String categoriaTxt) {
        this.categoriaTxt = categoriaTxt;
    }

    /**
     * @return the transversal
     */
    public boolean isTransversal() {
        return transversal;
    }

    /**
     * @param transversal the transversal to set
     */
    public void setTransversal(boolean transversal) {
        this.transversal = transversal;
    }

    
}
