/*
 * Rutas.java
 *
 * Created on 12 de noviembre de 2005, 12:50 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.datosglobales;

/**
 *
 * @author jmondra
 */
public class DatosGlobales {
  
  public static final boolean ESTADO_PRODUCCION=false; //Base de datos 
  private static final boolean AUTENTICACION_LDAP=false; //Activar/desactivar autenticación

  private static final String version="2018.12.17";  

    public static String getVersion(){
       return version;    
    } 
    
    public static String getRuta(){
        if(ESTADO_PRODUCCION){
            // Directorio en Intranet (y Mapserver)
            return "/data/asuntosdggma/";
            //return "/apusa/asuntos/";
            
        }else{
            //Directorio de prueba
            return "d:\\temporal\\asuntosdggma\\";
        }
    }
    
    public static String getRutaSistema(){
        if(ESTADO_PRODUCCION){
            // Directorio en Intranet (y Mapserver)
            return "http://10.153.3.31:8082/SistemaSeguimiento/";
        }else{
            //Directorio de prueba
            return "http://10.153.3.31:8082/SistemaSeguimiento/";
        }
    }
    
    
    public static String getURLJDBC(){
        if(ESTADO_PRODUCCION){
          //base datos servidor
          return "jdbc:postgresql://10.153.3.21:5433/asuntosdggma_2014"; // nuevo serv.
        }else{
            //base datos prueba
          return "jdbc:postgresql://10.153.3.26:5438/asuntosdggma_2014";
         
        }
    }
    
    public static String getUserName(){
       return "dggma";
    }
    
    public static String getPassword(){
       return "asuntos1218";
    }
    
    public static boolean getAutenticaLDAP(){
        return AUTENTICACION_LDAP;
    }
    
    public static String anioBase(){
       return "01/01/2010";
    }
}
