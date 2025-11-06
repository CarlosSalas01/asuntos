/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.dto;

/**
 *
 * @author RICARDO.SERNA
 */
public class HabilitaConveniosDTO {
    
    private String idusuario;
    private String permite;

    /**
     * @return the idusuario
     */
    public String getIdusuario() {
        return idusuario;
    }

    /**
     * @param idusuario the idusuario to set
     */
    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    /**
     * @return the permite
     */
    public String getPermite() {
        return permite;
    }

    /**
     * @param permite the permite to set
     */
    public void setPermite(String permite) {
        this.permite = permite;
    }
    
}
