/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.util.ArrayList;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles.Actividad;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.PermisoActividad;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.PermisoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author jacqueline
 */
public class AdministradorActividades {

    public List<PermisoActividad> asignaActividades(UsuarioBean usuario) throws Exception{
        String rolAcceso = usuario.getPermisoActual().getDatos().getRol();
        PermisoActividad  pa = new PermisoActividad();
        AreaBean area = null;

        List<PermisoActividad> permisos = new ArrayList<PermisoActividad>();

        if (usuario.getPermisoActual().getAreaBean() != null) {
           area = usuario.getPermisoActual().getAreaBean();
        }

        if (PermisoBean.getADMINISTRADOR().equals(rolAcceso)){

            pa = new PermisoActividad();
            pa.setActividad(Actividad.ADMONASUNTOS);
            pa.setDescripcion("Administra asuntos");
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.REPORTES);
            pa.setDescripcion("Generar reportes");
            permisos.add(pa);

            pa.setActividad(Actividad.ADMONACCIONES);
            pa.setDescripcion("Captura acuerdos/acciones");
            permisos.add(pa);
            
            /*pa = new PermisoActividad();
            pa.setActividad(Actividad.VERNOPUBLICADOS);
            pa.setDescripcion("Ver asuntos no publicados");
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados de la DGGMA");
            permisos.add(pa);*/
            
            pa = new PermisoActividad();
            pa.setActividad(Actividad.CONSULTA);
            pa.setDescripcion("Consultar asuntos y acuerdos");
            permisos.add(pa);
        }

        if (PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(rolAcceso)){

            pa.setActividad(Actividad.ADMONACCIONES);
            pa.setDescripcion("Administra acciones del área "+area.getDatos().getNombre());
            permisos.add(pa);

            /*pa = new PermisoActividad();
            pa.setActividad(Actividad.ADMONASUNTOS);
            pa.setDescripcion("Administra asuntos del �rea "+ area.getDatos().getNombre());
            permisos.add(pa);*/

            pa = new PermisoActividad();
            pa.setActividad(Actividad.REPORTES);
            pa.setDescripcion("Generar reportes");
            permisos.add(pa);

            /*pa = new PermisoActividad();
            pa.setActividad(Actividad.VERNOPUBLICADOS );
            pa.setDescripcion("Ver asuntos no publicados del �rea "+area.getDatos().getNombre());
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados del �rea "+ area.getDatos().getNombre());
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.EDITARESTATUS);
            pa.setDescripcion("Editar estatus y condici�n actual de los asuntos del �rea "+ area.getDatos().getNombre());
            permisos.add(pa);*/

            pa = new PermisoActividad();
            pa.setActividad(Actividad.CONSULTA);
            pa.setDescripcion("Consultar asuntos y acuerdos");
            permisos.add(pa);
            
            pa = new PermisoActividad();
            pa.setActividad(Actividad.CAPTURAAVANCE);
            pa.setDescripcion("Captura avances de asuntos o acuerdos");
            permisos.add(pa);

        }
        
        if (PermisoBean.getRESPONSABLE().equals(rolAcceso)){
            pa = new PermisoActividad();
            pa.setActividad(Actividad.CONSULTA);
            pa.setDescripcion("Consultar asuntos y acuerdos");
            permisos.add(pa);
            
            pa = new PermisoActividad();
            pa.setActividad(Actividad.CAPTURAAVANCE);
            pa.setDescripcion("Captura avances de asuntos o acuerdos");
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.REPORTES);
            pa.setDescripcion("Generar reportes");
            permisos.add(pa);

        }

        if (PermisoBean.getCONSULTA().equals(rolAcceso) && ( area == null)){
            pa = new PermisoActividad();
            pa.setActividad(Actividad.CONSULTA);
            pa.setDescripcion("Consultar asuntos y acuerdos");
            permisos.add(pa);
            
            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados de la DGGMA");
            permisos.add(pa);
        }


        if (PermisoBean.getCONSULTA().equals(rolAcceso) && ( area != null)){
            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados del área "+area.getDatos().getNombre());
            permisos.add(pa);
        }


        if (PermisoBean.getEJECUTIVO().equals(rolAcceso) && ( area == null) ){
            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados de la DGGMA");
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.ADMONINSTRUCCIONES);
            pa.setDescripcion("Administra instrucciones a cualquier área de la DGGMA ");
            permisos.add(pa);
        }

        if (PermisoBean.getEJECUTIVO().equals(rolAcceso) && ( area != null) ){
            pa = new PermisoActividad();
            pa.setActividad(Actividad.VERPUBLICADOS);
            pa.setDescripcion("Ver asuntos publicados del área "+area.getDatos().getNombre());
            permisos.add(pa);

            pa = new PermisoActividad();
            pa.setActividad(Actividad.ADMONINSTRUCCIONES);
            pa.setDescripcion("Administra instrucciones del área "+area.getDatos().getNombre());
            permisos.add(pa);
        }


        return permisos;

    }

}
