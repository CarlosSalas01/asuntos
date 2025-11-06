/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;

/**
 *
 * @author RICARDO.SERNA
 */
public class BitacoraDTO implements Serializable {
    private int id;
    private String observaciones;
    private String fecha;
    private int idusuariomodificacion;
    private String tipo;
    private int idbitacora;
    private String accion;
    private int idarea;
    private String detalle;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the idbitacora
     */
    public int getIdbitacora() {
        return idbitacora;
    }

    /**
     * @param idbitacora the idbitacora to set
     */
    public void setIdbitacora(int idbitacora) {
        this.idbitacora = idbitacora;
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
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
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
     * @return the detalle
     */
    public String getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    
}
