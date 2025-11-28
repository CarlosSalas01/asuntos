/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.modelo;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AvanceDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ResponsableDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.DelegadoNegocio;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline
 */
public class ResponsableBean implements Serializable {
    private ResponsableDTO datos;
    private AreaDTO area;
    private UsuarioDTO usuario;
    private String fechaatencionFormatoTexto;
    private String fechaasignadoFormato;
    private String ultimoAvance;
    private String ultimoAvanceGlobal;
    
    /**
     * @return the datos
     */
    public ResponsableDTO getDatos() {
        return datos;
    }

    /**
     * @param datos the datos to set
     */
    public void setDatos(ResponsableDTO datos) {
        this.datos = datos;
    }

    /**
     * @return the area
     */
    public AreaDTO getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(AreaDTO area) {
        this.area = area;
    }

    /**
     * @return the usuario
     */
    public UsuarioDTO getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the usuarioNombreCompleto
     */
    public String getUsuarioNombreCompleto() {
        //return this.usuario.getNombre()+" "+this.usuario.getApellido();
        return this.usuario.getNombre();
    }

    /**
     * @return the fechaatencionFormatoTexto
     */
    public String getFechaatencionFormatoTexto() {
        String fecha="";
        if (datos.getFechaatencion() != null && !datos.getFechaatencion().equals("")){
          fecha = Utiles.getFechaCorta(datos.getFechaatencion());
        }
        return fecha;
    }

    /**
     * @param fechaatencionFormatoTexto the fechaatencionFormatoTexto to set
     */
    public void setFechaatencionFormatoTexto(String fechaatencionFormatoTexto) {
        this.fechaatencionFormatoTexto = fechaatencionFormatoTexto;
    }

    
    /**
     * @return the fechaasignadoFormato
     */
    public String getFechaasignadoFormato() {
        return Utiles.getFechaCorta(datos.getFechaasignado());
    }

    /**
     * @param fechaasignadoFormato the fechaasignadoFormato to set
     */
    public void setFechaasignadoFormato(String fechaasignadoFormato) {
        this.fechaasignadoFormato = fechaasignadoFormato;
    }
    

    public String getUltimoAvance() throws SQLException, Exception{
        FachadaDAO fachada = new FachadaDAO(null);
        
        List<AvanceBean> avances = this.datos.getIdasunto() <= 0 ? fachada.obtenAvancesResponsableAcuerdo(this.datos.getIdaccion(), this.getArea().getIdarea()):fachada.obtenAvancesResponsableAsunto(this.datos.getIdasunto(), this.getArea().getIdarea());
        String avance="";
        if (avances.size() > 0) {
            AvanceBean av = avances.get(0);
            avance = av.getFechaFormato()+" "+av.getDescripcion();
        }
        return avance;
    
    }

    /**
     * @param ultimoAvance the ultimoAvance to set
     */
    public void setUltimoAvance(String ultimoAvance) {
        this.ultimoAvance = ultimoAvance;
    }

    /**
     * @return the ultimoAvanceGlobal
     */
    public String getUltimoAvanceGlobal() {
        FachadaDAO fachada = new FachadaDAO(null);
        String avance="";
        try{
            List<AvanceBean> avances = new ArrayList<AvanceBean>();
            if (this.datos.getIdasunto() > 0) {
               fachada.obtenAvancesxAsunto(this.datos.getIdasunto(), this.getArea().getIdarea(),avances);
            } else if (this.datos.getIdaccion() > 0) {
               fachada.obtenAvancesxAcuerdo(this.datos.getIdaccion(), this.getArea().getIdarea(),avances);
            }   
            
            if (avances.size() > 0) {
                AvanceBean av = avances.get(0);
                avance = av.getFechaFormato()+" "+av.getDescripcion() +" - "+av.getArea().getDatos().getSiglas();
            }
        } catch (Exception ex){
            avance = "Error, en la obtensi�n del avance";
        }   
        return avance;
    }

    /**
     * @param ultimoAvanceGlobal the ultimoAvanceGlobal to set
     */
    public void setUltimoAvanceGlobal(String ultimoAvanceGlobal) {
        this.ultimoAvanceGlobal = ultimoAvanceGlobal;
    }

    
}
