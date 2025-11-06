/*
 * RecordatorioDTO.java
 *
 * Created on 22 de febrero de 2007, 01:04 AM
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
public class RecordatorioDTO implements Serializable{
    
    private int idAsunto;
    private String texto;
    
    /** Creates a new instance of RecordatorioDTO */
    public RecordatorioDTO() {
    }

    public int getIdAsunto() {
        return idAsunto;
    }

    public void setIdAsunto(int idAsunto) {
        this.idAsunto = idAsunto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
}
