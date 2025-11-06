/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 *
 * @author jacqueline
 */
public class CorresponsableDAO {
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void insertarCorresponsable(AsuntoBean asunto, AreaBean corresponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "insert into controlasuntospendientesnew.corresponsable " +
                    "(idasunto,idarea) " +
                    "values (?,?)";
        
        Object[] params = {asunto.getIdasunto(),corresponsable.getDatos().getIdarea()};
        
        qr.update(sql,params);
        
    }

    public void eliminarCorresponsable(AsuntoBean asunto, AreaBean corresponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 

        String sql = "delete from controlasuntospendientesnew.corresponsable " +
                    "where idasunto=? and idarea=?";
        
        Object[] params = {asunto.getIdasunto(),corresponsable.getDatos().getIdarea()};
        
        qr.update(sql,params);
    }
    

    public boolean existeCorresponsableEnAsunto(AsuntoBean asunto, AreaBean corresponsable) throws SQLException,  NamingException {
    
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select count(*) as cantidad from controlasuntospendientesnew.corresponsable" +
                    " where idasunto=? and idarea=?";
        
         Object[] params = {asunto.getIdasunto(),corresponsable.getDatos().getIdarea()};

         ResultSetHandler rsh = new BeanHandler(Cantidad.class);
         Cantidad cantidad = (Cantidad) qr.query(sql, rsh, params);
         
         return cantidad.getCantidad() > 0;
         
    }
    public void eliminaCorrespTodos(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.corresponsable where idasunto=?";
        Object[] params = {idasunto};
        qr.update(sql,params);
    }
    
}
