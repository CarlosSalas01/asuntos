/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

import java.io.Serializable;

/**
 *
 * @author jacqueline
 */
public class AreaDTO implements Serializable {
  private int idarea;
  private String nombre;
  private int dependede;
  private int idresponsable;
  private int nivel;
  private String siglas;

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
     * @return the dependede
     */
    public int getDependede() {
        return dependede;
    }

    /**
     * @param dependede the dependede to set
     */
    public void setDependede(int dependede) {
        this.dependede = dependede;
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
     * @return the nivel
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
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

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
