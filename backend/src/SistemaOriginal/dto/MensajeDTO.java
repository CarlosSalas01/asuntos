/*
 * MensajeDTO.java
 *
 * Created on 26 de octubre de 2006, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class MensajeDTO implements Serializable {

    private int idMensaje;
    private String textoMensaje;
    private UsuarioBean remitente;
    private UsuarioBean destinatario;
    private String fecha;
    private boolean activoEstatus;
    private boolean descartado;

    /** Creates a new instance of MensajeDTO */
    public MensajeDTO() {
    }

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public UsuarioBean getRemitente() {
        return remitente;
    }

    public void setRemitente(UsuarioBean remitente) {
        this.remitente = remitente;
    }

    public UsuarioBean getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(UsuarioBean destinatario) {
        this.destinatario = destinatario;
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

    public boolean isDescartado() {
        return descartado;
    }

    public void setDescartado(boolean descartado) {
        this.descartado = descartado;
    }
}
