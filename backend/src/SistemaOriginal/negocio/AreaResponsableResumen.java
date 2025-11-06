/*
 * ResponsableResumen.java
 *
 * Created on 14 de octubre de 2006, 12:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.io.Serializable;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AreaResponsableResumen implements Serializable, Comparable {

    private AreaBean areaBean;
    private int asuntosPendientesTotal;
    private int asuntosNuevos;
    private int asuntosNoAtendidos;
    
    private int asuntosPreDescargados;
    private int asuntosPorVencer;


    private int asuntosSinAcciones;
    private int asuntosNoPublicados;
    
    private int asuntosEnProceso;
    private int asuntosAtendidos;
    private int asuntosCancelados;
    private int asuntosSuspendidos;
    private int asuntosVencidos;


    private int asuntosUrgentes;
    private int asuntosUrgenesVencidos;
    private int asuntosImportantes;
    private int asuntosImportantesVencidos;
    private int asuntosNormales;
    private int asuntosNormalesVencidos;


    private int asuntosTotales;
    private boolean subareas;
    private boolean mostrar;
    private int asuntosTotalesEnProceso;

    /**
     * Creates a new instance of ResponsableResumen
     */
    public AreaResponsableResumen() {
    }

    public int getAsuntosPendientesTotal() {
        return asuntosPendientesTotal;
    }

    public void setAsuntosPendientesTotal(int asuntosPendientesTotal) {
        this.asuntosPendientesTotal = asuntosPendientesTotal;
    }

    public int getAsuntosNuevos() {
        return asuntosNuevos;
    }

    public void setAsuntosNuevos(int asuntosNuevos) {
        this.asuntosNuevos = asuntosNuevos;
    }

    public int getAsuntosNoAtendidos() {
        return asuntosNoAtendidos;
    }

    public void setAsuntosNoAtendidos(int asuntosNoAtendidos) {
        this.asuntosNoAtendidos = asuntosNoAtendidos;
    }

    public int getAsuntosEnProceso() {
        return asuntosEnProceso;
    }

    public void setAsuntosEnProceso(int asuntosEnProceso) {
        this.asuntosEnProceso = asuntosEnProceso;
    }

    public int getAsuntosPreDescargados() {
        return asuntosPreDescargados;
    }

    public void setAsuntosPreDescargados(int asuntosPreDescargados) {
        this.asuntosPreDescargados = asuntosPreDescargados;
    }

    /**
     * @return the areaBean
     */
    public AreaBean getAreaBean() {
        return areaBean;
    }

    /**
     * @param areaBean the areaBean to set
     */
    public void setAreaBean(AreaBean areaBean) {
        this.areaBean = areaBean;
    }

    /**
     * @return the asuntosVencidos
     */
    public int getAsuntosVencidos() {
        return asuntosVencidos;
    }

    /**
     * @param asuntosVencidos the asuntosVencidos to set
     */
    public void setAsuntosVencidos(int asuntosVencidos) {
        this.asuntosVencidos = asuntosVencidos;
    }

    /**
     * @return the asuntosPorVencer
     */
    public int getAsuntosPorVencer() {
        return asuntosPorVencer;
    }

    /**
     * @param asuntosPorVencer the asuntosPorVencer to set
     */
    public void setAsuntosPorVencer(int asuntosPorVencer) {
        this.asuntosPorVencer = asuntosPorVencer;
    }

    /**
     * @return the asuntosSinAcciones
     */
    public int getAsuntosSinAcciones() {
        return asuntosSinAcciones;
    }

    /**
     * @param asuntosSinAcciones the asuntosSinAcciones to set
     */
    public void setAsuntosSinAcciones(int asuntosSinAcciones) {
        this.asuntosSinAcciones = asuntosSinAcciones;
    }

    /**
     * @return the asuntosNoPublicados
     */
    public int getAsuntosNoPublicados() {
        return asuntosNoPublicados;
    }

    /**
     * @param asuntosNoPublicados the asuntosNoPublicados to set
     */
    public void setAsuntosNoPublicados(int asuntosNoPublicados) {
        this.asuntosNoPublicados = asuntosNoPublicados;
    }

    /**
     * @return the asuntosAtendidos
     */
    public int getAsuntosAtendidos() {
        return asuntosAtendidos;
    }

    /**
     * @param asuntosAtendidos the asuntosAtendidos to set
     */
    public void setAsuntosAtendidos(int asuntosAtendidos) {
        this.asuntosAtendidos = asuntosAtendidos;
    }

    /**
     * @return the asuntosCancelados
     */
    public int getAsuntosCancelados() {
        return asuntosCancelados;
    }

    /**
     * @param asuntosCancelados the asuntosCancelados to set
     */
    public void setAsuntosCancelados(int asuntosCancelados) {
        this.asuntosCancelados = asuntosCancelados;
    }

    /**
     * @return the asuntosSuspendidos
     */
    public int getAsuntosSuspendidos() {
        return asuntosSuspendidos;
    }

    /**
     * @param asuntosSuspendidos the asuntosSuspendidos to set
     */
    public void setAsuntosSuspendidos(int asuntosSuspendidos) {
        this.asuntosSuspendidos = asuntosSuspendidos;
    }

    /**
     * @return the asuntosUrgentes
     */
    public int getAsuntosUrgentes() {
        return asuntosUrgentes;
    }

    /**
     * @param asuntosUrgentes the asuntosUrgentes to set
     */
    public void setAsuntosUrgentes(int asuntosUrgentes) {
        this.asuntosUrgentes = asuntosUrgentes;
    }

    /**
     * @return the asuntosImportantes
     */
    public int getAsuntosImportantes() {
        return asuntosImportantes;
    }

    /**
     * @param asuntosImportantes the asuntosImportantes to set
     */
    public void setAsuntosImportantes(int asuntosImportantes) {
        this.asuntosImportantes = asuntosImportantes;
    }

    /**
     * @return the asuntosNormales
     */
    public int getAsuntosNormales() {
        return asuntosNormales;
    }

    /**
     * @param asuntosNormales the asuntosNormales to set
     */
    public void setAsuntosNormales(int asuntosNormales) {
        this.asuntosNormales = asuntosNormales;
    }

    /**
     * @return the asuntosTotales
     */
    public int getAsuntosTotales() {
        int total = this.asuntosEnProceso+this.asuntosAtendidos+this.asuntosCancelados+this.asuntosSuspendidos;
        return total;
    }

    /**
     * @param asuntosTotales the asuntosTotales to set
     */
    public void setAsuntosTotales(int asuntosTotales) {
        this.asuntosTotales = asuntosTotales;
    }

    /**
     * @return the asuntosImportantesVencidos
     */
    public int getAsuntosImportantesVencidos() {
        return asuntosImportantesVencidos;
    }

    /**
     * @param asuntosImportantesVencidos the asuntosImportantesVencidos to set
     */
    public void setAsuntosImportantesVencidos(int asuntosImportantesVencidos) {
        this.asuntosImportantesVencidos = asuntosImportantesVencidos;
    }

    /**
     * @return the asuntosNormalesVencidos
     */
    public int getAsuntosNormalesVencidos() {
        return asuntosNormalesVencidos;
    }

    /**
     * @param asuntosNormalesVencidos the asuntosNormalesVencidos to set
     */
    public void setAsuntosNormalesVencidos(int asuntosNormalesVencidos) {
        this.asuntosNormalesVencidos = asuntosNormalesVencidos;
    }

    /**
     * @return the asuntosUrgenesVencidos
     */
    public int getAsuntosUrgenesVencidos() {
        return asuntosUrgenesVencidos;
    }

    /**
     * @param asuntosUrgenesVencidos the asuntosUrgenesVencidos to set
     */
    public void setAsuntosUrgenesVencidos(int asuntosUrgenesVencidos) {
        this.asuntosUrgenesVencidos = asuntosUrgenesVencidos;
    }

    /**
     * @return the subareas
     */
    public boolean isSubareas() {
        return subareas;
    }

    /**
     * @param subareas the subareas to set
     */
    public void setSubareas(boolean subareas) {
        this.subareas = subareas;
    }

    public int compareTo(Object o) {
        AreaResponsableResumen elemento =(AreaResponsableResumen) o;
        String arealocal = this.areaBean.getDatos().getNombre();
        String areanew   = elemento.areaBean.getDatos().getNombre();
        int nivellocal =  this.areaBean.getDatos().getNivel();
        int nivelnew   = elemento.areaBean.getDatos().getNivel();
        if(arealocal==null){
            throw new RuntimeException("No existen datos en el nombre del �rea");
        }
        int diferencia=nivellocal-nivelnew;
        if(diferencia==0){
           return arealocal.compareToIgnoreCase(areanew);
        }else{
            return diferencia;
        }
    }

    /**
     * @return the mostrar
     */
    public boolean isMostrar() {
        return mostrar;
    }

    /**
     * @param mostrar the mostrar to set
     */
    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    /**
     * @return the asuntosTotalesEnProceso
     */
    public int getAsuntosTotalesEnProceso() {
        return this.asuntosImportantes+this.asuntosNormales+this.asuntosUrgentes;
    }

    /**
     * @param asuntosTotalesEnProceso the asuntosTotalesEnProceso to set
     */
    public void setAsuntosTotalesEnProceso(int asuntosTotalesEnProceso) {
        this.asuntosTotalesEnProceso = asuntosTotalesEnProceso;
    }
}
