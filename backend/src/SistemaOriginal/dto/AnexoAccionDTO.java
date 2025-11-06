/*
 * AnexoAccionDTO.java
 *
 * Created on 27 de octubre de 2006, 11:16 PM
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
public class AnexoAccionDTO extends AnexoDTO implements Serializable {
  
  private int idaccion;

  

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
  
    
  
  
}
