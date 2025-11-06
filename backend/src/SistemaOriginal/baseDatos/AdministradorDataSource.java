/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.io.Serializable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import org.apache.commons.dbcp2.BasicDataSource;



/**
 *
 * @author joseluis.mondragon
 */
public class AdministradorDataSource implements Serializable {

    private static DataSource dsp;

    protected static synchronized DataSource getDataSource() throws NamingException {
         if (dsp == null) {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName("org.postgresql.Driver");

            bds.setUrl(DatosGlobales.getURLJDBC());
            bds.setUsername(DatosGlobales.getUserName());
            bds.setPassword(DatosGlobales.getPassword());
            
            bds.setTestWhileIdle(true);
            bds.setTestOnBorrow(true);
            bds.setTestOnReturn(false);
            bds.setValidationQuery("select 1 as uno;");
            bds.setValidationQueryTimeout(3000);
            bds.setTimeBetweenEvictionRunsMillis(5000);
            bds.setRemoveAbandonedTimeout(5);
            bds.setRemoveAbandonedOnBorrow(true);
            bds.setLogAbandoned(true);
            bds.setMaxWaitMillis(5000);
            bds.setMaxTotal(10);
            bds.setMinIdle(1);
            bds.setInitialSize(1);
            //bds.setMaxConnLifetimeMillis(10000);
            //bds.setMaxIdle(10);

            dsp = bds;
        }
       
        return dsp;
    }

   

}
