/*
 * InstruccionDTO.java
 *
 * Created on 5 de octubre de 2006, 05:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class InstruccionDTO implements Serializable {

    private int idInstruccion;
    private String textoInstruccion;
    private String fecha;


    private String textoRespuesta;
    private String fechaRespuesta;
    private UsuarioBean usuarioImparte;
    private UsuarioBean responsableAtiende;
    private int idAsunto;
    
    private int idusuarioimparte;
    private int idresponsableatiende;
    private String activoestatus;
    private String atendida;
    
    
    /**
     * Creates a new instance of InstruccionDTO
     */
    public InstruccionDTO() {
    }

    public String getTextoInstruccionFormatoHTML() {
        return Utiles.parrafeaHTML(this.textoInstruccion.trim());
    }

    public String getTextoRespuestaFormatoHTML() {
        String respuesta = null;
        if (this.textoRespuesta != null) {
            respuesta = Utiles.parrafeaHTML(this.textoRespuesta.trim());
        }
        return respuesta;
    }

    public int getIdInstruccion() {
        return idInstruccion;
    }

    public void setIdInstruccion(int idInstruccion) {
        this.idInstruccion = idInstruccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
     


    public String getFechaFormatoTexto() {
        return Utiles.getFechaFlexible(this.fecha);
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getFechaRespuestaFormatoTexto() {
        return Utiles.getFechaFlexible(this.fechaRespuesta);
    }

    public UsuarioBean getUsuarioImparte() {
        return usuarioImparte;
    }

    public void setUsuarioImparte(UsuarioBean usuarioImparte) {
        this.usuarioImparte = usuarioImparte;
    }

    public int getIdAsunto() {
        return idAsunto;
    }

    public void setIdAsunto(int idAsunto) {
        this.idAsunto = idAsunto;
    }

    public String getTextoInstruccion() {
        return textoInstruccion;
    }

    public void setTextoInstruccion(String textoInstruccion) {
        this.textoInstruccion = textoInstruccion;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    public UsuarioBean getResponsableAtiende() {
        return responsableAtiende;
    }

    public void setResponsableAtiende(UsuarioBean responsableAtiende) {
        this.responsableAtiende = responsableAtiende;
    }

    public boolean isActivoEstatus() {
        return getActivoestatus().equals("S");
    }


    public boolean isAtendida() {
        return getAtendida().equals("S");
    }


    public TareaDTO getTarea(){
        TareaDTO t=new TareaDTO();
        t.setAcuerdoFechaTarea("");
        t.setAcuerdoResponsableTarea("");
        t.setAcuerdoTarea("");
        t.setAnexosTarea(null);
        t.setDescripcionTarea(this.getTextoInstruccionFormatoHTML());
        t.setEstatusTarea("");
        t.setFechaTarea(fecha);
        t.setFechaTareaFormato(Utiles.getFechaFlexibleSDia(this.fecha));
        t.setIdTarea(this.idAsunto+"-"+this.idInstruccion);
        t.setResponsableTarea("");
        t.setTipoTarea("I");
        t.setUsuarioCaptura(this.usuarioImparte.getNombreCompleto());

        return t;
    }

    /**
     * @return the idusuarioimparte
     */
    public int getIdusuarioimparte() {
        return idusuarioimparte;
    }

    /**
     * @param idusuarioimparte the idusuarioimparte to set
     */
    public void setIdusuarioimparte(int idusuarioimparte) {
        this.idusuarioimparte = idusuarioimparte;
    }

    /**
     * @return the idresponsableatiende
     */
    public int getIdresponsableatiende() {
        return idresponsableatiende;
    }

    /**
     * @param idresponsableatiende the idresponsableatiende to set
     */
    public void setIdresponsableatiende(int idresponsableatiende) {
        this.idresponsableatiende = idresponsableatiende;
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
     * @return the atendida
     */
    public String getAtendida() {
        return atendida;
    }

    /**
     * @param atendida the atendida to set
     */
    public void setAtendida(String atendida) {
        this.atendida = atendida;
    }


}
