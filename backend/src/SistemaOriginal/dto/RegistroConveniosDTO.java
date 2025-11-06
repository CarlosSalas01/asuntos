/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

/**
 *
 * @author JACQUELINE.NINO
 */
public class RegistroConveniosDTO {
  private String fecha;
  private int vigentes;
  private int tramite;
  private int concluidos;
  private int cancelados;
  private int idarea;


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
     * @return the vigentes
     */
    public int getVigentes() {
        return vigentes;
    }

    /**
     * @param vigentes the vigentes to set
     */
    public void setVigentes(int vigentes) {
        this.vigentes = vigentes;
    }

    /**
     * @return the tramite
     */
    public int getTramite() {
        return tramite;
    }

    /**
     * @param tramite the tramite to set
     */
    public void setTramite(int tramite) {
        this.tramite = tramite;
    }

    /**
     * @return the concluidos
     */
    public int getConcluidos() {
        return concluidos;
    }

    /**
     * @param concluidos the concluidos to set
     */
    public void setConcluidos(int concluidos) {
        this.concluidos = concluidos;
    }

    /**
     * @return the cancelados
     */
    public int getCancelados() {
        return cancelados;
    }

    /**
     * @param cancelados the cancelados to set
     */
    public void setCancelados(int cancelados) {
        this.cancelados = cancelados;
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


}
