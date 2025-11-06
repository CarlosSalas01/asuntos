/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

/**
 *
 * @author jacqueline
 */
public class AvanceDTO {
  private int idavance;
  private int idarea;
  private int idasunto;
  private int idaccion;
  private String descripcion;
  private String fecha;
  private int porcentaje;
  private int idusuariomodificacion;

    /**
     * @return the idavance
     */
    public int getIdavance() {
        return idavance;
    }

    /**
     * @param idavance the idavance to set
     */
    public void setIdavance(int idavance) {
        this.idavance = idavance;
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
     * @return the porcentaje
     */
    public int getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
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
    
}
