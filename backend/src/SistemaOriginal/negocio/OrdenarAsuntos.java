/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;

/**
 *
 * @author jacqueline
 */
public class OrdenarAsuntos {

    class comparadorID implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return new Integer(o1.getIdasunto()).compareTo(new Integer(o2.getIdasunto()));
        }
    }

    class comparadorIDDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return new Integer(o2.getIdasunto()).compareTo(new Integer(o1.getIdasunto()));
        }
    }

    class comparadorAsunto implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getDescripcion().compareTo(o2.getDescripcion());
        }
    }

    class comparadorAsuntoDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o2.getDescripcion().compareTo(o1.getDescripcion());
        }
    }

    class comparadorFechaIngreso implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getFechaingreso().compareTo(o2.getFechaingreso());
        }
    }

    class comparadorFechaIngresoDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getFechaingreso().compareTo(o2.getFechaingreso());
        }
    }


    class comparadorFechaAtender implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getFechaatender().compareTo(o2.getFechaatender());
        }
    }

   class comparadorFechaAtenderDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getFechaatender().compareTo(o2.getFechaatender());
        }
    }

    class comparadorDescripcion implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getEstatustexto().compareTo(o2.getEstatustexto());
        }
    }

   class comparadorDescripcionDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            return o1.getEstatustexto().compareTo(o2.getEstatustexto());
        }
    }


    class comparadorResponsable implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            int result = 0;
            if (o1.getResponsable().getDatos().getIdarea() > o2.getResponsable().getDatos().getIdarea()){
              result = 1;
            } else {
              result = -1;
            }
            return result ;
        }
    }

    class comparadorResponsableDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            int result = 0;
            if (o1.getResponsable().getDatos().getIdarea() > o2.getResponsable().getDatos().getIdarea()){
              result = 1;
            } else {
              result = -1;
            }
            return result ;
            
        }
    }


    class comparadorCondicion implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
           // return o1.getObservaciones().compareTo(o2.getc);
            return 0;
        }
    }

    class comparadorCondicionDesc implements Comparator<AsuntoBean> {

        public int compare(AsuntoBean o1, AsuntoBean o2) {
            //return o1.getcgetCondicionactual().compareTo(o2.getCondicionactual());
            return 0;
        }
    }


    public List<AsuntoBean> ordena(List<AsuntoBean> asuntos, String campo, String orden) {
        AsuntoBean[] asuntosArray = asuntos.toArray(new AsuntoBean[asuntos.size()]);
        Comparator< AsuntoBean > comparator = null;
        if (campo.equals("0") && orden.equals("0")) //ID
            comparator = new comparadorID();

        if (campo.equals("0") && orden.equals("1")) //ID
            comparator = new comparadorIDDesc();

        if (campo.equals("1") && orden.equals("0")) //Asunto
            comparator = new comparadorAsunto();

        if (campo.equals("1") && orden.equals("1")) //Asunto
            comparator = new comparadorAsuntoDesc();

        if (campo.equals("2") && orden.equals("0")) //FechaIngreso
            comparator = new comparadorFechaIngreso();

        if (campo.equals("2") && orden.equals("1")) //FechaIngreso
            comparator =  new comparadorFechaIngresoDesc();

        if (campo.equals("3") && orden.equals("0")) //FechaAtender
            comparator = new comparadorFechaAtender();

        if (campo.equals("3") && orden.equals("1")) //FechaAtender
            comparator = new comparadorFechaAtenderDesc();

        if (campo.equals("4") && orden.equals("0")) //Descripcion
            comparator = new comparadorDescripcion();

        if (campo.equals("4") && orden.equals("1")) //Descripcion
            comparator = new comparadorDescripcionDesc();

        if (campo.equals("5") && orden.equals("0")) //Responsable
            comparator = new comparadorResponsable();

        if (campo.equals("5") && orden.equals("1")) //Responsable
            comparator =  new comparadorResponsableDesc();

        if (campo.equals("6") && orden.equals("0")) //Condici�n Actual
            comparator =  new comparadorCondicion();

        if (campo.equals("6") && orden.equals("1")) //Condici�n Actual
            comparator = new comparadorCondicionDesc();


        Arrays.sort(asuntosArray, comparator);

        return Arrays.asList(asuntosArray);
    }
}
