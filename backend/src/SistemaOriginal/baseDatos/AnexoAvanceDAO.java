/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAvanceDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AvanceBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jacqueline
 */
public class AnexoAvanceDAO {
    
     public AnexoAvanceDTO findByPrimaryKey(int pk) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idavance=?" ;
        
        Object[] params = {pk};
        
        ResultSetHandler rsh = new BeanHandler(AnexoAvanceDTO.class);
        AnexoAvanceDTO dato = (AnexoAvanceDTO) qr.query(sql, rsh, params);
        
        return dato;
    }
    
    public void insert(AnexoAvanceDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
         String sql = "insert into controlasuntospendientesnew.anexo " +
                    "(activoestatus,titulo,nombrearchivo,nombreenservidor," +
                    "contenttype,fechacarga,tamanio,idavance) " +
                    "values (?,?,?,?,?,?,?,?)";

         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdavance()};
                           
          qr.update(sql,params);                  

    }

    public void update(AnexoAvanceDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
            String sql = "update controlasuntospendientesnew.anexo " +
                    "set activoestatus=?,titulo=?,nombrearchivo=?,nombreenservidor=?," +
                    "contenttype=?,fechacarga=?,tamanio=?,idavance=? " +
                    "where idanexo=?";
            
            Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),
                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdavance(),

                            anexo.getIdanexo()};
                           
            qr.update(sql,params);        
            
    }


    public void delete(int idanexo) throws SQLException,  NamingException {
        //Se elimina directamente en bd pero se 
        //Agregar a bitacora tmb la eliminaci�n
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "DELETE FROM controlasuntospendientesnew.anexo "+
                     "where idanexo =?";
        Object[] params = {idanexo};
                
        qr.update(sql,params);
        
    }


    public  List<AnexoAvanceDTO> buscarAnexosPorAvance(AvanceBean avance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idavance=? " +
                "order by fechacarga";
        
        Object[] params = {avance.getIdavance()};
        
        ResultSetHandler rsh = new BeanListHandler(AnexoAvanceDTO.class);
        List<AnexoAvanceDTO> datos = (List<AnexoAvanceDTO>) qr.query(sql, rsh, params);
        
        return datos;
    }
    
      
    
     public  List<AnexoAvanceDTO> buscarAnexosPorAvance(int idavance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idavance=? " +
                "order by fechacarga";
        
        Object[] params = {idavance};
        
        ResultSetHandler rsh = new BeanListHandler(AnexoAvanceDTO.class);
        List<AnexoAvanceDTO> datos = (List<AnexoAvanceDTO>) qr.query(sql, rsh, params);
        
        return datos;
    }
      
    
}
