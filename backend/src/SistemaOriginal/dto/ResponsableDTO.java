/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline
 */
public class ResponsableDTO implements Serializable {
  private int idresponsable;
  private int idaccion;
  private int idarea;
  private int idasunto;
  private int avance;
  private String fechaatencion;
  private int diasatencion;
  private int diasretraso;
  private String estatus;
  private String delegado;
  private int asignadopor;
  private String fechaasignado;
  private String instruccion;
  private String comentario;
  
    

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
     * @return the avance
     */
    public int getAvance() {
        return avance;
    }

    /**
     * @param avance the avance to set
     */
    public void setAvance(int avance) {
        this.avance = avance;
    }

    /**
     * @return the fechaatencion
     */
    public String getFechaatencion() {
        return fechaatencion;
    }

    /**
     * @param fechaatencion the fechaatencion to set
     */
    public void setFechaatencion(String fechaatencion) {
        this.fechaatencion = fechaatencion;
    }

    /**
     * @return the diasatencion
     */
    public int getDiasatencion() {
        return diasatencion;
    }

    /**
     * @param diasatencion the diasatencion to set
     */
    public void setDiasatencion(int diasatencion) {
        this.diasatencion = diasatencion;
    }

    /**
     * @return the diasretraso
     */
    public int getDiasretraso() {
        return diasretraso;
    }

    /**
     * @param diasretraso the diasretraso to set
     */
    public void setDiasretraso(int diasretraso) {
        this.diasretraso = diasretraso;
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
     * @return the delegado
     */
    public String getDelegado() {
        return delegado;
    }

    /**
     * @param delegado the delegado to set
     */
    public void setDelegado(String delegado) {
        this.delegado = delegado;
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
     * @return the asignadopor
     */
    public int getAsignadopor() {
        return asignadopor;
    }

    /**
     * @param asignadopor the asignadopor to set
     */
    public void setAsignadopor(int asignadopor) {
        this.asignadopor = asignadopor;
    }

    /**
     * @return the fechaasignado
     */
    public String getFechaasignado() {
        return fechaasignado;
    }

    /**
     * @param fechaasignado the fechaasignado to set
     */
    public void setFechaasignado(String fechaasignado) {
        this.fechaasignado = fechaasignado;
    }

    /**
     * @return the instruccion
     */
    public String getInstruccion() {
        return instruccion;
    }

    /**
     * @param instruccion the instruccion to set
     */
    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    
    
}
