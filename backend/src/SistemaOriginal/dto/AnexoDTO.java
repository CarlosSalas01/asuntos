/*
 * AnexoDTO.java
 *
 * Created on 26 de octubre de 2006, 11:35 AM
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
public class AnexoDTO implements Serializable {
  private int idanexo; 
  private String activoestatus;
  private String titulo;
  private String nombrearchivo;
  private String nombreenservidor;
  private String contenttype;
  private String fechacarga;
  private int tamanio;
  private int tipodoc;
  private int idactividad;

    /**
     * @return the idanexo
     */
    public int getIdanexo() {
        return idanexo;
    }

    /**
     * @param idanexo the idanexo to set
     */
    public void setIdanexo(int idanexo) {
        this.idanexo = idanexo;
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
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the nombrearchivo
     */
    public String getNombrearchivo() {
        return nombrearchivo;
    }

    /**
     * @param nombrearchivo the nombrearchivo to set
     */
    public void setNombrearchivo(String nombrearchivo) {
        this.nombrearchivo = nombrearchivo;
    }

    /**
     * @return the nombreenservidor
     */
    public String getNombreenservidor() {
        return nombreenservidor;
    }

    /**
     * @param nombreenservidor the nombreenservidor to set
     */
    public void setNombreenservidor(String nombreenservidor) {
        this.nombreenservidor = nombreenservidor;
    }

    /**
     * @return the contenttype
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * @param contenttype the contenttype to set
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    /**
     * @return the fechacarga
     */
    public String getFechacarga() {
        return fechacarga;
    }

    /**
     * @param fechacarga the fechacarga to set
     */
    public void setFechacarga(String fechacarga) {
        this.fechacarga = fechacarga;
    }

    /**
     * @return the tamanio
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * @param tamanio the tamanio to set
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }
    
     public boolean isActivoestatus(){
        return this.activoestatus.equals("S");
   }
     
   public int getTipoDocumento(){
       int tipo=0;
       if (this.getContenttype().equals("application/msword") || this.getContenttype().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
         tipo = 1;
       }
       if (this.getContenttype().equals("application/vnd.ms-excel") || this.getContenttype().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
         tipo = 2;
       }
       if (this.getContenttype().equals("application/vnd.ms-powerpoint")) {
         tipo = 3;
       }
       if (this.getContenttype().equals("application/pdf")){
         tipo = 4;  
       }
       if (this.getContenttype().equals("application/octet-stream")) {
         tipo = 5;
       }
        if (this.getContenttype().equals("application/x-zip-compressed")) {
         tipo = 6;
       }
       
       return tipo;
    }

    /**
     * @return the tipodoc
     */
    public int getTipodoc() {
        return tipodoc;
    }

    /**
     * @param tipodoc the tipodoc to set
     */
    public void setTipodoc(int tipodoc) {
        this.tipodoc = tipodoc;
    }

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
    
}
