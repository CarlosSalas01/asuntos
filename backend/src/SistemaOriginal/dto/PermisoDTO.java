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
public class PermisoDTO implements Serializable {
  private int idpermiso;
  private int idusuario;
  private int idarea;
  private String rol;

    /**
     * @return the idpermiso
     */
    public int getIdpermiso() {
        return idpermiso;
    }

    /**
     * @param idpermiso the idpermiso to set
     */
    public void setIdpermiso(int idpermiso) {
        this.idpermiso = idpermiso;
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
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
    
}
