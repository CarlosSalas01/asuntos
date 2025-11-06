/*
 * AnexoAsuntoDTO.java
 *
 * Created on 27 de octubre de 2006, 11:15 PM
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
public class AnexoAsuntoDTO extends AnexoDTO implements Serializable {
    
    private int idAsunto;
    private int idAccion;
    
    /** Creates a new instance of AnexoAsuntoDTO */
    public AnexoAsuntoDTO() {
    }

    public int getIdAsunto() {
        return idAsunto;
    }

    public void setIdAsunto(int idAsunto) {
        this.idAsunto = idAsunto;
    }

    /**
     * @return the idAccion
     */
    public int getIdAccion() {
        return idAccion;
    }

    /**
     * @param idAccion the idAccion to set
     */
    public void setIdAccion(int idAccion) {
        this.idAccion = idAccion;
    }
    
    
 
    
}
