/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.PermisoDTO;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jacqueline
 */
public class PermisoDAO {
    public List<PermisoDTO> permisosUsuario(int idusuario) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idpermiso, idusuario, idarea, rol FROM controlasuntospendientesnew.permiso "
                     +"WHERE idusuario = ? order by idarea";
        

        Object[] params = {idusuario};
     
        ResultSetHandler rsh = new BeanListHandler(PermisoDTO.class);
        List<PermisoDTO> datos = (List<PermisoDTO>) qr.query(sql, rsh, params);       
        
        return datos;
    }
    
    public List<PermisoDTO> permisosArea(int idarea) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idpermiso, idusuario, idarea, rol FROM controlasuntospendientesnew.permiso "
                     +"WHERE idarea = ?";
        

        Object[] params = {idarea};
     
        ResultSetHandler rsh = new BeanListHandler(PermisoDTO.class);
        List<PermisoDTO> datos = (List<PermisoDTO>) qr.query(sql, rsh, params);       
        
        return datos;
    }

    public int idAreaByPermiso(int idpermiso) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idarea FROM controlasuntospendientesnew.permiso where idpermiso = ?";
        ResultSetHandler rsh = new BeanHandler(PermisoDTO.class);
        PermisoDTO dato = (PermisoDTO) qr.query(sql, rsh, idpermiso);       
        return dato.getIdarea();
    }
    
}
