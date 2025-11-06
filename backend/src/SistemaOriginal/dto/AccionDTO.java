/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;

/**
 *
 * @author jacqueline
 */
public class AccionDTO implements Serializable{
    private int idconsecutivo;
    private int idAccion;
    private int idAsunto;
    private int dependede;
    private int idarearesponsable; 
    
    
    // private String idAccionMuestra;
    private int idAsuntoConsecutivo;
     
    private String descripcion;
    private String fecha;
    private String derivaDe;
    private String fechaFormat;
    private String condicionFormatoHTML;
    private String fechaFormatoTexto;
    
    private String estatus;
    private String estatusImagen;
    private String acuerdo;
    private String acuerdoFormatoHTML;
    private String acuerdo_responsable;
    private String acuerdo_fecha;
    private String acuerdo_fecha_formato;
    private String activoestatus;
    
    private String publicoEstatus;
    private String realizada;
    
    private String tiene_acuerdos;
  
    private String fechacaptura;
    private String fechaoriginal;
    
    private int idusuariomodificacion;
    private String reprograma;
    private String prioridad;
    
   
    /**
     * @return the idAccion
     */
    public int getIdAccion() {
        return idAccion;
    }

    /**
     * @param idAccion the idAccion to set
     */
    public void setIdAccion(int idAccion) {
        this.idAccion = idAccion;
    }

    /**
     * @return the idAsunto
     */
    public int getIdAsunto() {
        return idAsunto;
    }

    /**
     * @param idAsunto the idAsunto to set
     */
    public void setIdAsunto(int idAsunto) {
        this.idAsunto = idAsunto;
    }

    /**
     * @return the dependede
     */
    public int getDependede() {
        return dependede;
    }

    /**
     * @param dependede the dependede to set
     */
    public void setDependede(int dependede) {
        this.dependede = dependede;
    }

   
    /**
     * @return the idarearesponsable
     */
    public int getIdarearesponsable() {
        return idarearesponsable;
    }

    /**
     * @param idarearesponsable the idarearesponsable to set
     */
    public void setIdarearesponsable(int idarearesponsable) {
        this.idarearesponsable = idarearesponsable;
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
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the derivaDe
     */
    public String getDerivaDe() {
        return derivaDe;
    }

    /**
     * @param derivaDe the derivaDe to set
     */
    public void setDerivaDe(String derivaDe) {
        this.derivaDe = derivaDe;
    }

    /**
     * @return the fechaFormat
     */
    public String getFechaFormat() {
        return fechaFormat;
    }

    /**
     * @param fechaFormat the fechaFormat to set
     */
    public void setFechaFormat(String fechaFormat) {
        this.fechaFormat = fechaFormat;
    }

   

    /**
     * @return the condicionFormatoHTML
     */
    public String getCondicionFormatoHTML() {
        return condicionFormatoHTML;
    }

    /**
     * @param condicionFormatoHTML the condicionFormatoHTML to set
     */
    public void setCondicionFormatoHTML(String condicionFormatoHTML) {
        this.condicionFormatoHTML = condicionFormatoHTML;
    }

    /**
     * @return the fechaFormatoTexto
     */
    public String getFechaFormatoTexto() {
        return fechaFormatoTexto;
    }

    /**
     * @param fechaFormatoTexto the fechaFormatoTexto to set
     */
    public void setFechaFormatoTexto(String fechaFormatoTexto) {
        this.fechaFormatoTexto = fechaFormatoTexto;
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
     * @return the estatusImagen
     */
    public String getEstatusImagen() {
        return estatusImagen;
    }

    /**
     * @param estatusImagen the estatusImagen to set
     */
    public void setEstatusImagen(String estatusImagen) {
        this.estatusImagen = estatusImagen;
    }

    /**
     * @return the acuerdo
     */
    public String getAcuerdo() {
        return acuerdo;
    }

    /**
     * @param acuerdo the acuerdo to set
     */
    public void setAcuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
    }

    /**
     * @return the acuerdoFormatoHTML
     */
    public String getAcuerdoFormatoHTML() {
        return acuerdoFormatoHTML;
    }

    /**
     * @param acuerdoFormatoHTML the acuerdoFormatoHTML to set
     */
    public void setAcuerdoFormatoHTML(String acuerdoFormatoHTML) {
        this.acuerdoFormatoHTML = acuerdoFormatoHTML;
    }

    /**
     * @return the acuerdo_responsable
     */
    public String getAcuerdo_responsable() {
        return acuerdo_responsable;
    }

    /**
     * @param acuerdo_responsable the acuerdo_responsable to set
     */
    public void setAcuerdo_responsable(String acuerdo_responsable) {
        this.acuerdo_responsable = acuerdo_responsable;
    }

    /**
     * @return the acuerdo_fecha
     */
    public String getAcuerdo_fecha() {
        return acuerdo_fecha;
    }

    /**
     * @param acuerdo_fecha the acuerdo_fecha to set
     */
    public void setAcuerdo_fecha(String acuerdo_fecha) {
        this.acuerdo_fecha = acuerdo_fecha;
    }

    /**
     * @return the acuerdo_fecha_formato
     */
    public String getAcuerdo_fecha_formato() {
        return acuerdo_fecha_formato;
    }

    /**
     * @param acuerdo_fecha_formato the acuerdo_fecha_formato to set
     */
    public void setAcuerdo_fecha_formato(String acuerdo_fecha_formato) {
        this.acuerdo_fecha_formato = acuerdo_fecha_formato;
    }

    /**
     * @return the publicoEstatus
     */
    public String getPublicoEstatus() {
        return publicoEstatus;
    }

    /**
     * @param publicoEstatus the publicoEstatus to set
     */
    public void setPublicoEstatus(String publicoEstatus) {
        this.publicoEstatus = publicoEstatus;
    }

    /**
     * @return the realizada
     */
    public String getRealizada() {
        return realizada;
    }

    /**
     * @param realizada the realizada to set
     */
    public void setRealizada(String realizada) {
        this.realizada = realizada;
    }


    /**
     * @return the tiene_acuerdos
     */
    public String getTiene_acuerdos() {
        return tiene_acuerdos;
    }

    /**
     * @param tiene_acuerdos the tiene_acuerdos to set
     */
    public void setTiene_acuerdos(String tiene_acuerdos) {
        this.tiene_acuerdos = tiene_acuerdos;
    }

    /**
     * @return the fechacaptura
     */
    public String getFechacaptura() {
        return fechacaptura;
    }

    /**
     * @param fechacaptura the fechacaptura to set
     */
    public void setFechacaptura(String fechacaptura) {
        this.fechacaptura = fechacaptura;
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
     * @return the idAsuntoConsecutivo
     */
    public int getIdAsuntoConsecutivo() {
        return idAsuntoConsecutivo;
    }

    /**
     * @param idAsuntoConsecutivo the idAsuntoConsecutivo to set
     */
    public void setIdAsuntoConsecutivo(int idAsuntoConsecutivo) {
        this.idAsuntoConsecutivo = idAsuntoConsecutivo;
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
     * @return the prioridad
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * @param prioridad the prioridad to set
     */
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    
}
