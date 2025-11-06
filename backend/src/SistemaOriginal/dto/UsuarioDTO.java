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
public class UsuarioDTO implements Serializable {
    private int idusuario;
    private String username;
    private String nombre;
    private String apellido;
    private String vigente;
    private String activoestatus;
    private String correo1;
    private String correo2;
    private String responsable;
    private String enviocorreoauto;
    private String convenios;
    private String superusuario;

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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the vigente
     */
    public String getVigente() {
        return vigente;
    }

    /**
     * @param vigente the vigente to set
     */
    public void setVigente(String vigente) {
        this.vigente = vigente;
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
     * @return the correo1
     */
    public String getCorreo1() {
        return correo1;
    }

    /**
     * @param correo1 the correo1 to set
     */
    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    /**
     * @return the correo2
     */
    public String getCorreo2() {
        return correo2;
    }

    /**
     * @param correo2 the correo2 to set
     */
    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    /**
     * @return the responsable
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    /**
     * @return the enviocorreoauto
     */
    public String getEnviocorreoauto() {
        return enviocorreoauto;
    }

    /**
     * @param enviocorreoauto the enviocorreoauto to set
     */
    public void setEnviocorreoauto(String enviocorreoauto) {
        this.enviocorreoauto = enviocorreoauto;
    }

    /**
     * @return the convenios
     */
    public String getConvenios() {
        return convenios;
    }

    /**
     * @param convenios the convenios to set
     */
    public void setConvenios(String convenios) {
        this.convenios = convenios;
    }

    /**
     * @return the superusuario
     */
    public String getSuperusuario() {
        return superusuario;
    }

    /**
     * @param superusuario the superusuario to set
     */
    public void setSuperusuario(String superusuario) {
        this.superusuario = superusuario;
    }

    
}
