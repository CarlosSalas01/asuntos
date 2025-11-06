/*
 * AsuntoDAO.java
 *
 * Created on 4 de marzo de 2006, 04:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAsuntoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AnexoAsuntoDAO {

    /** Creates a new instance of AsuntoDAO */
    public AnexoAsuntoDAO() {
        
    }

    public AnexoAsuntoDTO buscaxID(int idanexo) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "select * from controlasuntospendientesnew.anexo " +
                    "where idanexo=? ";
        
       Object[] params = {idanexo};
        
       ResultSetHandler rsh = new BeanHandler(AnexoAsuntoDTO.class);
       AnexoAsuntoDTO dato = (AnexoAsuntoDTO) qr.query(sql, rsh, params);
        
       return dato;
    }
    
    
    
    public  void insert(AnexoAsuntoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
      
        String sql = "insert into controlasuntospendientesnew.anexo " +
                    "(activoestatus,titulo,nombrearchivo,nombreenservidor," +
                    "contenttype,fechacarga,tamanio,idasunto) " +
                    "values (?,?,?,?,?,?,?,?)";
      
         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdAsunto()};
         
         qr.update(sql,params);                  

    }


    public  void update(AnexoAsuntoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "update controlasuntospendientesnew.anexo " +
                    "set activoestatus=?,titulo=?,nombrearchivo=?,nombreenservidor=?," +
                    "contenttype=?,fechacarga=?,tamanio=?,idasunto=? " +
                    "where idanexo=?";

         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdAsunto(),
                            
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


    public  List<AnexoAsuntoDTO> buscarAnexosPorAsunto(AsuntoBean asunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idasunto=?" +
                "order by fechacarga";
        
        Object[] params = {asunto.getIdasunto()};
        
        ResultSetHandler rsh = new BeanListHandler(AnexoAsuntoDTO.class);
        List<AnexoAsuntoDTO> datos = (List<AnexoAsuntoDTO>) qr.query(sql, rsh, params);
        
        return datos;
    }
    

    
}
