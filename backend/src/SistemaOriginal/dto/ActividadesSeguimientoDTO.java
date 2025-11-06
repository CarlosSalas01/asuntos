/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.util.List;

/**
 *
 * @author RICARDO.SERNA
 */
public class ActividadesSeguimientoDTO {
    
    private int idactividad;
    private int idasunto;
    private String fechaactividad;
    private String descripcion;
    private String usuario;
    private List<AnexoAsuntoDTO> anexos;
    private String fechaactividadstr;
    private int idusuario;
    private int idaccion;
    private int idarea;
    private String siglas;

    /**
     * @return the idactividad
     */
    public int getIdactividad() {
        return idactividad;
    }

    /**
     * @param idactividad the idactividad to set
     */
    public void setIdactividad(int idactividad) {
        this.idactividad = idactividad;
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
     * @return the fechaactividad
     */
    public String getFechaactividad() {
        return fechaactividad;
    }

    /**
     * @param fechaactividad the fechaactividad to set
     */
    public void setFechaactividad(String fechaactividad) {
        this.fechaactividad = fechaactividad;
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
     * @return the anexos
     */
    public List<AnexoAsuntoDTO> getAnexos() {
        return anexos;
    }

    /**
     * @param anexos the anexos to set
     */
    public void setAnexos(List<AnexoAsuntoDTO> anexos) {
        this.anexos = anexos;
    }

    /**
     * @return the fechaactividadstr
     */
    public String getFechaactividadstr() {
        return fechaactividadstr;
    }

    /**
     * @param fechaactividadstr the fechaactividadstr to set
     */
    public void setFechaactividadstr(String fechaactividadstr) {
        this.fechaactividadstr = fechaactividadstr;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the idusuario
     */
    public int getIdusuario() {
        return idusuario;
    }

    /**
     * @param idusuario the idusuario to set
     */
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
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
     * @return the siglas
     */
    public String getSiglas() {
        return siglas;
    }

    /**
     * @param siglas the siglas to set
     */
    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }
 
}
