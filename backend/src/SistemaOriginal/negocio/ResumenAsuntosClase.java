/*
 * ResumenAsuntosClase.java
 *
 * Created on 14 de octubre de 2006, 12:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;


import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FechaReferencia;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.PermisoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class ResumenAsuntosClase {

    private List<AreaResponsableResumen> responsables;

    /** Creates a new instance of ResumenAsuntosClase */
    public ResumenAsuntosClase(UsuarioBean usuario, FechaReferencia fechaPivote) throws Exception {
        AdministraUsuariosAreas administraUsuarios = new AdministraUsuariosAreas();
        responsables = administraUsuarios.obtenResumenAsuntos(usuario,fechaPivote);
        //Collections.sort(responsables);
    }

    
    public List<AreaResponsableResumen> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<AreaResponsableResumen> responsables) {
        this.responsables = responsables;
    }

    public String getLigaGraficoPorResponsable() {
        String valoresStr = "";
        String pipeStr = "";
        for (AreaResponsableResumen responsable : responsables) {
            valoresStr = valoresStr + pipeStr + responsable.getAsuntosNuevos() + "," + responsable.getAsuntosNoAtendidos() + "," + responsable.getAsuntosEnProceso();
            pipeStr = "|";
        }
        String responsablesStr = "";
        String comaStr = "";
        for (AreaResponsableResumen responsable : responsables) {
            responsablesStr = responsablesStr + comaStr + responsable.getAreaBean().getDatos().getNombre();
            comaStr = ",";
        }

        return "http://mapserver.inegi.gob.mx/capGrafica/grafica.cfm?" +
                "t=Nuevos,No atendidos,En proceso" +
                "&s=" + valoresStr + " " +
                "&n=" + responsablesStr + " " +
                "&e=1&titulo=Resumen gr&aacute;fico por responsable";
    }

    public String getLigaGraficoPorEstatus() {
        String valoresStr = "";
        String pipeStr = "";
        for (AreaResponsableResumen responsable : responsables) {
            valoresStr = valoresStr + pipeStr + responsable.getAsuntosNuevos() + "," + responsable.getAsuntosNoAtendidos() + "," + responsable.getAsuntosEnProceso();
            pipeStr = "|";
        }
        String responsablesStr = "";
        String comaStr = "";
        for (AreaResponsableResumen responsable : responsables) {
            responsablesStr = responsablesStr + comaStr + responsable.getAreaBean().getDatos().getNombre();
            comaStr = ",";
        }

        return "http://mapserver.inegi.gob.mx/capGrafica/grafica.cfm?" +
                "t=Nuevos,No atendidos,En proceso" +
                "&s=" + valoresStr + " " +
                "&n=" + responsablesStr + " " +
                "&e=0&titulo=Resumen gr&aacute;fico por estatus";
    }

    public void visualizaAreasxUsuario(UsuarioBean usuario){
        String rol = usuario.getPermisoActual().getDatos().getRol();
        boolean consultaArea = usuario.getPermisoActual().getAreaBean() != null;
        int areamax;
        if (PermisoBean.getADMINISTRADOR().equals(rol) || (PermisoBean.getCONSULTA().equals(rol) && !consultaArea) || (PermisoBean.getEJECUTIVO().equals(rol) && !consultaArea )) {
            areamax = responsables.get(0).getAreaBean().getDatos().getIdarea();

             for (AreaResponsableResumen ab : responsables) {
                 ab.setMostrar((ab.getAreaBean().getDatos().getIdarea() == areamax) || (ab.getAreaBean().getDatos().getDependede() == areamax) ? true:false);
             }
         } else {
            areamax = usuario.getPermisoActual().getAreaBean().getDatos().getIdarea();
            for (AreaResponsableResumen ab : responsables) {
                if (ab.getAreaBean().getDatos().getIdarea() == areamax) {
                    ab.setSubareas(false);
                }
                ab.setMostrar((ab.getAreaBean().getDatos().getIdarea() == areamax) || (ab.getAreaBean().getDatos().getDependede() == areamax) ? true:false);
            }
         }
    }
    
}
