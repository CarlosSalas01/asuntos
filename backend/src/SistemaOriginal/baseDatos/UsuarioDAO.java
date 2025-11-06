/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;


import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.DatosLogin;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jacqueline
 */
public class UsuarioDAO {
    
    
    public UsuarioDTO buscaUsuario(DatosLogin datosLogin) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
       
        String sql = "SELECT idusuario, username, contrasenia, nombre, apellido, vigente, "
                     +"activoestatus, correo1, correo2, responsable, enviocorreoauto, superusuario FROM controlasuntospendientesnew.usuario "
                     +"WHERE username = ? and vigente = 'S'";
        
        Object[] params = {datosLogin.getUsuario().toLowerCase()};
     
       ResultSetHandler rsh = new BeanHandler(UsuarioDTO.class);
       UsuarioDTO dato = (UsuarioDTO) qr.query(sql, rsh, params);        

       return dato;
    }
    
    
    public UsuarioDTO buscaUsuario(Integer idusuario) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
       
        String sql = "SELECT idusuario, username, contrasenia, nombre, apellido, vigente, "
                     +"activoestatus, correo1, correo2, responsable, enviocorreoauto FROM controlasuntospendientesnew.usuario "
                     +"WHERE idusuario = ? and vigente = 'S' ";
        
        Object[] params = {idusuario};
     
       ResultSetHandler rsh = new BeanHandler(UsuarioDTO.class);
       UsuarioDTO dato = (UsuarioDTO) qr.query(sql, rsh, params);        
       return dato;
    }
    
    public UsuarioDTO buscaUsuarioVigenteoNo(Integer idusuario) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
       
        String sql = "SELECT idusuario, username, contrasenia, nombre, apellido, vigente, "
                     +"activoestatus, correo1, correo2, responsable, enviocorreoauto FROM controlasuntospendientesnew.usuario "
                     +"WHERE idusuario = ?";
        
        Object[] params = {idusuario};
     
       ResultSetHandler rsh = new BeanHandler(UsuarioDTO.class);
       UsuarioDTO dato = (UsuarioDTO) qr.query(sql, rsh, params);        

       return dato;
    }
    
    
    public List<UsuarioDTO> obtenUsuariosAdministradores() throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
       
        String sql = "SELECT u.idusuario, u.username, u.contrasenia, u.nombre, u.apellido, u.vigente, "
                     +"u.activoestatus, u.correo1, u.correo2, u.responsable, u.enviocorreoauto FROM controlasuntospendientesnew.usuario u, "
                     +"controlasuntospendientesnew.permiso p WHERE p.rol = 'A' and u.idusuario = p.idusuario and u.vigente='S' ";
        
     
       ResultSetHandler rsh = new BeanListHandler(UsuarioDTO.class);
       List<UsuarioDTO> dato = (List<UsuarioDTO>) qr.query(sql, rsh);        

       return dato;
    }
    public List<UsuarioDTO> obtenUsuariosNivel2() throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT a.correo1,a.nombre FROM controlasuntospendientesnew.usuario as a " +
                "inner join controlasuntospendientesnew.area as b on a. idusuario=b.idresponsable " +
                "where b.nivel='2' and a.vigente='S'";
       ResultSetHandler rsh = new BeanListHandler(UsuarioDTO.class);
       List<UsuarioDTO> dato = (List<UsuarioDTO>) qr.query(sql, rsh);        
       return dato;
    }
}
