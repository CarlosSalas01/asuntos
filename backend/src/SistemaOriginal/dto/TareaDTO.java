/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jacqueline
 */
public class TareaDTO implements Serializable, Comparable{
    private String fechaTarea;
    private String idTarea;
    private String tipoTarea;
    private String fechaTareaFormato;
    private String descripcionTarea;
    private String responsableTarea;
    private String usuarioCaptura;
    private List<AnexoAccionDTO> anexosTarea;
    private String acuerdoTarea;
    private String acuerdoFechaTarea;
    private String acuerdoResponsableTarea;
    private String estatusTarea;

    public int compareTo(Object o) {
        TareaDTO externo=(TareaDTO)o;
        return this.getFechaTarea().compareTo(externo.getFechaTarea());
    }

    /**
     * @return the fechaTarea
     */
    public String getFechaTarea() {
        return fechaTarea;
    }

    /**
     * @param fechaTarea the fechaTarea to set
     */
    public void setFechaTarea(String fechaTarea) {
        this.fechaTarea = fechaTarea;
    }

    /**
     * @return the idTarea
     */
    public String getIdTarea() {
        return idTarea;
    }

    /**
     * @param idTarea the idTarea to set
     */
    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    /**
     * @return the tipoTarea
     */
    public String getTipoTarea() {
        return tipoTarea;
    }

    /**
     * @param tipoTarea the tipoTarea to set
     */
    public void setTipoTarea(String tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    /**
     * @return the fechaTareaFormato
     */
    public String getFechaTareaFormato() {
        return fechaTareaFormato;
    }

    /**
     * @param fechaTareaFormato the fechaTareaFormato to set
     */
    public void setFechaTareaFormato(String fechaTareaFormato) {
        this.fechaTareaFormato = fechaTareaFormato;
    }

    /**
     * @return the descripcionTarea
     */
    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    /**
     * @param descripcionTarea the descripcionTarea to set
     */
    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    /**
     * @return the responsableTarea
     */
    public String getResponsableTarea() {
        return responsableTarea;
    }

    /**
     * @param responsableTarea the responsableTarea to set
     */
    public void setResponsableTarea(String responsableTarea) {
        this.responsableTarea = responsableTarea;
    }

    /**
     * @return the anexosTarea
     */
    public List<AnexoAccionDTO> getAnexosTarea() {
        return anexosTarea;
    }

    /**
     * @param anexosTarea the anexosTarea to set
     */
    public void setAnexosTarea(List<AnexoAccionDTO> anexosTarea) {
        this.anexosTarea = anexosTarea;
    }

    /**
     * @return the acuerdoTarea
     */
    public String getAcuerdoTarea() {
        return acuerdoTarea;
    }

    /**
     * @param acuerdoTarea the acuerdoTarea to set
     */
    public void setAcuerdoTarea(String acuerdoTarea) {
        this.acuerdoTarea = acuerdoTarea;
    }

    /**
     * @return the acuerdoFechaTarea
     */
    public String getAcuerdoFechaTarea() {
        return acuerdoFechaTarea;
    }

    /**
     * @param acuerdoFechaTarea the acuerdoFechaTarea to set
     */
    public void setAcuerdoFechaTarea(String acuerdoFechaTarea) {
        this.acuerdoFechaTarea = acuerdoFechaTarea;
    }

    /**
     * @return the acuerdoResponsableTarea
     */
    public String getAcuerdoResponsableTarea() {
        return acuerdoResponsableTarea;
    }

    /**
     * @param acuerdoResponsableTarea the acuerdoResponsableTarea to set
     */
    public void setAcuerdoResponsableTarea(String acuerdoResponsableTarea) {
        this.acuerdoResponsableTarea = acuerdoResponsableTarea;
    }

    /**
     * @return the estatusTarea
     */
    public String getEstatusTarea() {
        return estatusTarea;
    }

    /**
     * @param estatusTarea the estatusTarea to set
     */
    public void setEstatusTarea(String estatusTarea) {
        this.estatusTarea = estatusTarea;
    }

    /**
     * @return the usuarioCaptura
     */
    public String getUsuarioCaptura() {
        return usuarioCaptura;
    }

    /**
     * @param usuarioCaptura the usuarioCaptura to set
     */
    public void setUsuarioCaptura(String usuarioCaptura) {
        this.usuarioCaptura = usuarioCaptura;
    }


}
