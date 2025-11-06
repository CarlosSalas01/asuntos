/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.HabilitaConveniosDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.RegistroConveniosDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.TablaConveniosDatos;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author JACQUELINE.NINO
 */
public class RegistroConveniosDAO {
    
    public  List<RegistroConveniosDTO> totalesxPeriodo() throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT sum(vigentes) as vigentes, sum(tramite) as tramite, sum(concluidos) as concluidos, sum(cancelados) as cancelados " +
                     "  FROM controlasuntospendientesnew.registroconvenios GROUP BY fecha";
        
        ResultSetHandler rsh = new BeanListHandler(RegistroConveniosDTO.class);
        List<RegistroConveniosDTO> datos = (List<RegistroConveniosDTO>) qr.query(sql, rsh);
        
        return datos;
    }
    
    public  RegistroConveniosDTO totalesxPeriodo(String fecha) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT sum(vigentes) as vigentes, sum(tramite) as tramite, sum(concluidos) as concluidos, sum(cancelados) as cancelados " +
                     "  FROM controlasuntospendientesnew.registroconvenios WHERE fecha = ? GROUP BY fecha";
        Object[] parametros = {fecha};
        ResultSetHandler rsh = new BeanHandler(RegistroConveniosDTO.class);
        RegistroConveniosDTO dato = (RegistroConveniosDTO) qr.query(sql, rsh, parametros);
        return dato;
    }

    public  List<RegistroConveniosDTO> totalesxPeriodoxArea(int idarea) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT sum(vigentes) as vigentes, sum(tramite) as tramite, sum(concluidos) as concluidos, sum(cancelados) as cancelados " +
                     "  FROM controlasuntospendientesnew.registroconvenios WHERE idarea = ? GROUP BY fecha,idarea";
        
        Object[] parametros = {idarea};
        
        ResultSetHandler rsh = new BeanListHandler(RegistroConveniosDTO.class);
        List<RegistroConveniosDTO> datos = (List<RegistroConveniosDTO>) qr.query(sql, rsh, parametros);
        
        return datos;
    }
    
    
    
    
    public void insertaCierre(RegistroConveniosDTO cierre) throws SQLException, NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
        
       String sql = "INSERT INTO controlasuntospendientesnew.registroconvenios(fecha, vigentes, tramite, concluidos, cancelados, idarea) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";
        
         Object[] params = {cierre.getFecha(),cierre.getVigentes(), cierre.getTramite(), cierre.getConcluidos(), cierre.getCancelados(), cierre.getIdarea()}; 

         qr.update(sql,params);
    }

    public TablaConveniosDatos desgloceConvenios(String estatus, String fechaPenultima, String fechaUltima) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String estat=estatus.toUpperCase();
        if(estat.equals("TRAMITE")) estat = "TR�MITE";
        String sql="SELECT '"+estat+"' as estatus, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=97 and fecha='"+fechaPenultima+"') as rnmaP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=97 and fecha='"+fechaUltima+"') as rnmaU, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=2 and fecha='"+fechaPenultima+"') as igbP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=2 and fecha='"+fechaUltima+"') as igbU, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=129 and fecha='"+fechaPenultima+"') as icrP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=129 and fecha='"+fechaUltima+"') as icrU, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=47 and fecha='"+fechaPenultima+"') as iigP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=47 and fecha='"+fechaUltima+"') as iigU, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=184 and fecha='"+fechaPenultima+"') as dpcsreP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=184 and fecha='"+fechaUltima+"') as dpcsreU, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=164 and fecha='"+fechaPenultima+"') as dmgP, " +
            "(select "+estatus+" FROM controlasuntospendientesnew.registroconvenios where idarea=164 and fecha='"+fechaUltima+"') as dmgU;";
        //Object[] params = {fechaPenultima, fechaUltima};
        ResultSetHandler rsh = new BeanHandler(TablaConveniosDatos.class);
        TablaConveniosDatos datos = (TablaConveniosDatos) qr.query(sql, rsh);
        return datos;
    }    
    public void habilitaCaptura(int idusuario, String permite) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "UPDATE controlasuntospendientesnew.habilitaconvenios SET idusuario=?, permite=? ";
        qr.update(sql,idusuario, permite);
    }
    public String permiteCaptura() throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT permite FROM controlasuntospendientesnew.habilitaconvenios";
        ResultSetHandler rsh = new BeanHandler(HabilitaConveniosDTO.class);
        HabilitaConveniosDTO dato = (HabilitaConveniosDTO) qr.query(sql, rsh);
        return dato.getPermite();
    }   
}
