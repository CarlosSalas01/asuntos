/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline
 */
public class ProgramacionDTO {
    private int idasunto;
    private int idacuerdo;
    private String fechareprograma;
    private String fecharealizada;
    private String justificacion;
    private int idreprogramacion;
    private int reunion;

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
     * @return the idacuerdo
     */
    public int getIdacuerdo() {
        return idacuerdo;
    }

    /**
     * @param idacuerdo the idacuerdo to set
     */
    public void setIdacuerdo(int idacuerdo) {
        this.idacuerdo = idacuerdo;
    }

    

    /**
     * @return the fecharealizada
     */
    public String getFecharealizada() {
        return fecharealizada;
    }

    /**
     * @param fecharealizada the fecharealizada to set
     */
    public void setFecharealizada(String fecharealizada) {
        this.fecharealizada = fecharealizada;
    }

    /**
     * @return the justificacion
     */
    public String getJustificacion() {
        return justificacion;
    }

    /**
     * @param justificacion the justificacion to set
     */
    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    /**
     * @return the fechareprograma
     */
    public String getFechareprograma() {
        return fechareprograma;
    }

    /**
     * @param fechareprograma the fechareprograma to set
     */
    public void setFechareprograma(String fechareprograma) {
        this.fechareprograma = fechareprograma;
    }
    
    public String getFecharealizadaFormato(){
       return Utiles.getFechaCorta(this.fecharealizada);
    }
    
    public String getFechareprogramaFormato(){
       return Utiles.getFechaCorta(this.fechareprograma);
    }

    /**
     * @return the idreprogramacion
     */
    public int getIdreprogramacion() {
        return idreprogramacion;
    }

    /**
     * @param idreprogramacion the idreprogramacion to set
     */
    public void setIdreprogramacion(int idreprogramacion) {
        this.idreprogramacion = idreprogramacion;
    }

    /**
     * @return the reunion
     */
    public int getReunion() {
        return reunion;
    }

    /**
     * @param reunion the reunion to set
     */
    public void setReunion(int reunion) {
        this.reunion = reunion;
    }
    
}
