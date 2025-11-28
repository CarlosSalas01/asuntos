/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ReporteXMesDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ResponsableDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jacqueline
 */
public class AreaDAO {
    
    public AreaDTO getArea(int idarea) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, trim(siglas) as siglas "
                     +"FROM controlasuntospendientesnew.area WHERE idarea=?"; 
        
        Object[] params = {idarea};
     
        ResultSetHandler rsh = new BeanHandler(AreaDTO.class);
        AreaDTO dato = (AreaDTO) qr.query(sql, rsh, params);    

        return dato;
    }
    
    public AreaDTO getAreadeResponsable(int idResponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE idresponsable=? order by nivel,idarea";
        
        Object[] params = {idResponsable};
     
        ResultSetHandler rsh = new BeanHandler(AreaDTO.class);
        AreaDTO dato = (AreaDTO) qr.query(sql, rsh, params);    

        return dato;
    }
    
    
    public List<AreaDTO> getAreasxId(int idarea) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE idarea=? order by nivel,idarea,nombre";

        Object[] params = {idarea};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;

    }
    
    public List<AreaDTO> areasDependientes(int dependeD, String ordenarPor) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String ordena = ordenarPor.equals("nivel") ? "nivel,":"";
        String ordena2 =  ordenarPor.equals("area") ? "idarea,":"";
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE dependede = ? order by "
                     + ordena+ordena2+"nombre";

        Object[] params = {dependeD};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;

    }
    
    public List<AreaDTO> areasxNivel(int nivel) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE nivel=? and (idarea!=179 and idarea!=184 and idarea!=152) order by nivel,idarea"; 

        Object[] params = {nivel};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;
    }
    
    public List<AreaDTO> areasxNivelxAreaSuper(int nivel, int idRSuper) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE  nivel=? and dependede = ? order by nivel,idarea"; 

        Object[] params = {nivel,idRSuper};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;
    }
    
    public List<AreaDTO> areasSubNivel(int dependede) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE  dependede = ? and activa='A' order by nivel,idarea"; 

        Object[] params = {dependede};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
             
        return datos;
    }
    public List<AreaDTO> areasSubNivelCaptura(int dependede) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE dependede = ? and (idarea!=179 and idarea!=184 and idarea!=152) order by nivel,idarea"; 
        Object[] params = {dependede};
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        return datos;
    }

    public List<AreaDTO> areasCorresponsablesxNivelRango(int nivelinf, int nivelsup) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area WHERE  nivel >=?  and nivel <= ? order by nivel,idarea"; 

        Object[] params = {nivelinf,nivelsup};
     
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;
    }
    
    public List<AreaDTO> areasCorresponsablesxAsunto(AsuntoBean asunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area "
                     +"WHERE idarea in "
                     + "(select idarea "
                     + "from controlasuntospendientesnew.corresponsable "
                     + "where idasunto = ?)";
        
        Object[] params = {asunto.getIdasunto()};

        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;
    }

    public List<AreaDTO> areasResponsablesxAsunto(AsuntoBean asunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area "
                     +"WHERE idarea in "
                     + "(select idarea "
                     + "from controlasuntospendientesnew.responsable "
                     + "where idasunto = ?) order by idarea";
        
        Object[] params = {asunto.getIdasunto()};

        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        
        return datos;
    }
    public List<AreaDTO> areasResponsablesxAsunto(int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idarea, nombre, dependede, idresponsable, nivel, siglas "
                     +"FROM controlasuntospendientesnew.area "
                     +"WHERE idarea in "
                     + "(select idarea "
                     + "from controlasuntospendientesnew.responsable "
                     + "where idasunto = ?) order by idarea";
        Object[] params = {idasunto};
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        return datos;
    }

    public List<ReporteXMesDTO> areaAsuntoMes1(int area, String anio) throws SQLException,  NamingException  {
         DataSource ds = AdministradorDataSource.getDataSource();
         QueryRunner qr = new QueryRunner(ds);
         //String[] mes = new String[]{anio+"01", anio+"01",};
         String sql = "select c.nombre as responsable,CASE WHEN a.tipoasunto='K' THEN 'SIA' "
              + "WHEN a.tipoasunto='R' THEN 'Reunion' "
              + "WHEN a.tipoasunto='M' THEN 'Comision' "
              + "WHEN a.tipoasunto='C' THEN 'Correo' "
              + "END as asunto, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as ene_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as ene_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as feb_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as feb_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as mar_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as mar_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as abr_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as abr_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as may_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as may_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as jun_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as jun_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as jul_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as jul_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as ago_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as ago_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as sep_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as sep_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as oct_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as oct_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as nov_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as nov_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as dic_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,6)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as dic_p "
              + "from controlasuntospendientesnew.asunto as a,controlasuntospendientesnew.area as c where c.idarea=? and a.tipoasunto<>'R' group by a.tipoasunto,c.nombre";
         Object[] params = {anio+"01",area,anio+"01",area,anio+"02",area,anio+"02",area,anio+"03",area,anio+"03",area,anio+"04",area,anio+"04",area,anio+"05",area,anio+"05",area,anio+"06",area,anio+"06",area,anio+"07",area,anio+"07",area,anio+"08",area,anio+"08",area,anio+"09",area,anio+"09",area,anio+"10",area,anio+"10",area,anio+"11",area,anio+"11",area,anio+"12",area,anio+"12",area,area};
         ResultSetHandler rsh = new BeanListHandler(ReporteXMesDTO.class);
         List<ReporteXMesDTO> datos = (List<ReporteXMesDTO>) qr.query(sql, rsh, params);
         return datos;
    }
    public List<ReporteXMesDTO> areaAsuntoMes2(int area, String anioMes) throws SQLException,  NamingException  {
         DataSource ds = AdministradorDataSource.getDataSource();
         QueryRunner qr = new QueryRunner(ds);
         String sql = "select 'Reunion' as asunto, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as ene_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as ene_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as feb_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as feb_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as mar_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as mar_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as abr_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as abr_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as may_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as may_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as jun_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as jun_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as jul_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as jul_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as ago_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as ago_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as sep_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as sep_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as oct_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as oct_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as nov_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as nov_p,"
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as dic_a, "
                 + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,6)=? and a2.idarea = ?) as dic_p "
                 + "from controlasuntospendientesnew.asunto as a where a.idarea=? and a.tipoasunto='R' group by a.tipoasunto";
         Object[] params = {anioMes+"01",area,anioMes+"01",area,anioMes+"02",area,anioMes+"02",area,anioMes+"03",area,anioMes+"03",area,anioMes+"04",area,anioMes+"04",area,anioMes+"05",area,anioMes+"05",area,anioMes+"06",area,anioMes+"06",area,anioMes+"7",area,anioMes+"07",area,anioMes+"08",area,anioMes+"08",area,anioMes+"09",area,anioMes+"09",area,anioMes+"10",area,anioMes+"10",area,anioMes+"11",area,anioMes+"11",area,anioMes+"12",area,anioMes+"12",area,area};
         ResultSetHandler rsh = new BeanListHandler(ReporteXMesDTO.class);
         List<ReporteXMesDTO> datos = (List<ReporteXMesDTO>) qr.query(sql, rsh, params);
         return datos;
    }
    public List<ReporteXMesDTO> areaAsuntoMes3(int area, String anioMes) throws SQLException,  NamingException  {
         DataSource ds = AdministradorDataSource.getDataSource();
         QueryRunner qr = new QueryRunner(ds);
         String sql = "select 'Acuerdo' as asunto, a.idarea, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as ene_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as ene_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as feb_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as feb_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as mar_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as mar_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as abr_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as abr_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as may_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as may_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as jun_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as jun_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as jul_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as jul_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as ago_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as ago_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as sep_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as sep_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as oct_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as oct_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as nov_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as nov_p, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='A') as dic_a, "
                 + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,6)=?) and estatus='P') as dic_p "
                 + "from controlasuntospendientesnew.responsable as a where a.idarea=? group by a.idarea";
         Object[] params = {area,anioMes+"01",area,anioMes+"01",area,anioMes+"02",area,anioMes+"02",area,anioMes+"03",area,anioMes+"03",area,anioMes+"04",area,anioMes+"04",area,anioMes+"05",area,anioMes+"05",area,anioMes+"06",area,anioMes+"06",area,anioMes+"07",area,anioMes+"07",area,anioMes+"08",area,anioMes+"08",area,anioMes+"09",area,anioMes+"09",area,anioMes+"10",area,anioMes+"10",area,anioMes+"11",area,anioMes+"11",area,anioMes+"12",area,anioMes+"12",area};
         ResultSetHandler rsh = new BeanListHandler(ReporteXMesDTO.class);
         List<ReporteXMesDTO> datos = (List<ReporteXMesDTO>) qr.query(sql, rsh, params);
         return datos;
    }

    public int obtenAreaSuperior(int idarea, int nivel) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "";
        if(nivel==3){
            sql="select dependede from controlasuntospendientesnew.area where idarea = "+idarea+" ";
        }
        if(nivel==4){
            sql="select a.dependede as dir,b.dependede as dependede from controlasuntospendientesnew.area as a "
                    + "inner join controlasuntospendientesnew.area as b on a.dependede=b.idarea "
                    + "where a.idarea = "+idarea+" ";
        }
        if(nivel==5){
            sql="select a.dependede as subd,b.dependede as dir,c.dependede as dependede from controlasuntospendientesnew.area as a "
                    + "inner join controlasuntospendientesnew.area as b on a.dependede=b.idarea "
                    + "inner join controlasuntospendientesnew.area as c on b.dependede=c.idarea "
                    + "where a.idarea = "+idarea+" ";
        }
        ResultSetHandler rsh = new BeanHandler(AreaDTO.class);
        AreaDTO dato = (AreaDTO) qr.query(sql, rsh);    
        return dato.getDependede();
    }
    public int obtenNivelNext(int idasunto, int idarea) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select nivel from controlasuntospendientesnew.responsable as a "
                + "inner join controlasuntospendientesnew.area as b on a.idarea=b.idarea "
                + "where a.idasunto = ? and a.idarea=? ";
        Object[] params = {idasunto, idarea};
        ResultSetHandler rsh = new BeanHandler(AreaDTO.class);
        AreaDTO dato = (AreaDTO) qr.query(sql, rsh, params);
        return dato.getNivel() + 1;
    }
    public int obtenNivelNextAcuerdo(int idaccion, int idarea) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select nivel from controlasuntospendientesnew.responsable as a "
                + "inner join controlasuntospendientesnew.area as b on a.idarea=b.idarea "
                + "where a.idaccion = ? and a.idarea=? ";
        Object[] params = {idaccion, idarea};
        ResultSetHandler rsh = new BeanHandler(AreaDTO.class);
        AreaDTO dato = (AreaDTO) qr.query(sql, rsh, params);
        return dato.getNivel() + 1;
    }
    public List<AreaDTO> areasResponsablesNivel2(int idasunto, String tipoAsunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql = "";
        if(tipoAsunto.equals("A")) sql = "select idarea,siglas from controlasuntospendientesnew.area where nivel=2 and idarea in (select idarea from controlasuntospendientesnew.responsable where idasunto=?)";
        if(tipoAsunto.equals("B")) sql = "select idarea,siglas from controlasuntospendientesnew.area where nivel=2 and idarea in (select idarea from controlasuntospendientesnew.responsable where idaccion=?)";
        Object[] params = {idasunto};
        ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
        List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);    
        return datos;
    }
}
