/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline.nino
 */
public class AvanceCanceladoDTO {
    private int idavance;
    private int idarea;
    private int idasunto;
    private int idaccion;
    private int idresponsable;
    private String descripcion;
    private String fecha;
    private int porcentaje;
    private int idusuariomodificacion;
    private String fechaFormatoTexto;
    
    private List<AnexoAvanceDTO> anexos;

    /**
     * @return the idavance
     */
    public int getIdavance() {
        return idavance;
    }

    /**
     * @param idavance the idavance to set
     */
    public void setIdavance(int idavance) {
        this.idavance = idavance;
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
     * @return the idaccion
     */
    public int getIdaccion() {
        return idaccion;
    }

    /**
     * @param idaccion the idaccion to set
     */
    public void setIdaccion(int idaccion) {
        this.idaccion = idaccion;
    }

    /**
     * @return the idresponsable
     */
    public int getIdresponsable() {
        return idresponsable;
    }

    /**
     * @param idresponsable the idresponsable to set
     */
    public void setIdresponsable(int idresponsable) {
        this.idresponsable = idresponsable;
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
     * @return the porcentaje
     */
    public int getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
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
     * @return the anexos
     */
    public List<AnexoAvanceDTO> getAnexos() {
        return anexos;
    }

    /**
     * @param anexos the anexos to set
     */
    public void setAnexos(List<AnexoAvanceDTO> anexos) {
        this.anexos = anexos;
    }
    
    /**
     * @return the fechaFormatoTexto
     */
    public String getFechaFormatoTexto() {
        return Utiles.getFechaCorta(this.getFecha().trim());
    }

    /**
     * @param fechaFormatoTexto the fechaFormatoTexto to set
     */
    public void setFechaFormatoTexto(String fechaFormatoTexto) {
        this.fechaFormatoTexto = fechaFormatoTexto;
    }

  
}
