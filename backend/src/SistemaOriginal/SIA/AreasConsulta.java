/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.modelo;

import java.io.Serializable;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;

/**
 *
 * @author jacqueline
 */
public class AreasConsulta implements Serializable{
   private List<AreaDTO> areasConsulta;
   private List<AreaDTO> areasComparteResponsabilidad;
   private List<AreaDTO> areasResponsables;

    /**
     * @return the areasConsulta
     */
    public List<AreaDTO> getAreasConsulta() {
        return areasConsulta;
    }

    /**
     * @param areasConsulta the areasConsulta to set
     */
    public void setAreasConsulta(List<AreaDTO> areasConsulta) {
        this.areasConsulta = areasConsulta;
    }

    /**
     * @return the areasComparteResponsabilidad
     */
    public List<AreaDTO> getAreasComparteResponsabilidad() {
        return areasComparteResponsabilidad;
    }

    /**
     * @param areasComparteResponsabilidad the areasComparteResponsabilidad to set
     */
    public void setAreasComparteResponsabilidad(List<AreaDTO> areasComparteResponsabilidad) {
        this.areasComparteResponsabilidad = areasComparteResponsabilidad;
    }

    /**
     * @return the areasResponsables
     */
    public List<AreaDTO> getAreasResponsables() {
        return areasResponsables;
    }

    /**
     * @param areasResponsables the areasResponsables to set
     */
    public void setAreasResponsables(List<AreaDTO> areasResponsables) {
        this.areasResponsables = areasResponsables;
    }
  

   
}
