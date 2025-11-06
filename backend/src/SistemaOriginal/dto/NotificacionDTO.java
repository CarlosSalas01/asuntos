/*
 * NotificacionDTO.java
 *
 * Created on 27 de octubre de 2006, 10:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;

/**
 *
 * @author Jos Luis Mondragn
 */
public class NotificacionDTO implements Serializable {
    
    private int idNotificacion;
    private String textoNotificacion;
    private String fecha;
    private boolean activoEstatus;
    private boolean descartada;
    private int idAsunto;
    
    /** Creates a new instance of NotificacionDTO */
    public NotificacionDTO() {
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTextoNotificacion() {
        return textoNotificacion;
    }

    public void setTextoNotificacion(String textoNotificacion) {
        this.textoNotificacion = textoNotificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isActivoEstatus() {
        return activoEstatus;
    }

    public void setActivoEstatus(boolean activoEstatus) {
        this.activoEstatus = activoEstatus;
    }

    public boolean isDescartada() {
        return descartada;
    }

    public void setDescartada(boolean descartada) {
        this.descartada = descartada;
    }

    public int getIdAsunto() {
        return idAsunto;
    }

    public void setIdAsunto(int idAsunto) {
        this.idAsunto = idAsunto;
    }
    
}
