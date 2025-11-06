/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.GraficaDTO;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author RICARDO.SERNA
 */
public class GraficasDAO {
public List<GraficaDTO> xasuntoarea(List areas, String fecha1, String fecha2) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      List params = new ArrayList();
      String sql = "select CASE WHEN a.tipoasunto='K' THEN 'SIA' "
              + "WHEN a.tipoasunto='R' THEN 'Reunión' "
              + "WHEN a.tipoasunto='M' THEN 'Comisión' "
              + "WHEN a.tipoasunto='C' THEN 'Correo' "
              + "END as asunto, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and a2.fechaingreso >= ? and a2.fechaingreso <= ? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea in (";
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
          //params.add(ar.getIdarea());
      }
      sql = sql.substring(0, sql.length()-2) + "))) as atendido, ";
      sql+="(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and a2.fechaingreso >= ? and a2.fechaingreso <= ? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea in ( ";
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
      }
      sql = sql.substring(0, sql.length()-2) + "))) as pendiente ";
      sql+="from controlasuntospendientesnew.asunto as a where a.tipoasunto<>'R' and a.tipoasunto<>'V' group by a.tipoasunto";
      //Object[] params2 = {params.toString()};
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, fecha1, fecha2, fecha1, fecha2);
      return datos;
}  
public List<GraficaDTO> xasuntoareareunion(List areas, String fecha1, String fecha2) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "select 'Reunión' as asunto, ";
      sql+="(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and a2.fechaingreso >= ? and a2.fechaingreso <= ? and a2.idarea in (";
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
      }
      sql = sql.substring(0, sql.length()-2) + ")) as atendido, ";
      sql +="(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and a2.fechaingreso >= ? and a2.fechaingreso <= ? and a2.idarea in (" ;
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
      }
      sql = sql.substring(0, sql.length()-2) + ")) as pendiente ";
      sql +="from controlasuntospendientesnew.asunto as a where a.tipoasunto='R' group by a.tipoasunto";
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, fecha1, fecha2, fecha1, fecha2);
      return datos;
  }
  public List<GraficaDTO> xasuntoareaacuerdo(String fecha1, String fecha2) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "select 'Acuerdo' as asunto, "
/*              + "(select count(*) from controlasuntospendientesnew.accion) as total, "*/
              + "(select count(*) from controlasuntospendientesnew.accion where estatus='A' and fecha >= ? and fecha <= ?) as atendido, "
              + "(select count(*) from controlasuntospendientesnew.accion where estatus='P' and fecha >= ? and fecha <= ?) as pendiente";
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, fecha1, fecha2, fecha1, fecha2);
      return datos;
  }
  public List<GraficaDTO> xarea(List areas, String fecha1, String fecha2) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "SELECT distinct(resp.idarea), trim(ar.siglas) as responsable, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='K' and b.idarea=resp.idarea and a.fechaingreso >= ? and a.fechaingreso <= ?) as keet_tot, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='K' and b.idarea=resp.idarea and b.estatus='A' and a.fechaingreso >= ? and a.fechaingreso <= ?) as keet_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='K' and b.idarea=resp.idarea and b.estatus='P' and a.fechaingreso >= ? and a.fechaingreso <= ?) as keet_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='C' and b.idarea=resp.idarea and a.fechaingreso >= ? and a.fechaingreso <= ?) as correo_tot, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='C' and b.idarea=resp.idarea and b.estatus='A' and a.fechaingreso >= ? and a.fechaingreso <= ?) as correo_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='C' and b.idarea=resp.idarea and b.estatus='P' and a.fechaingreso >= ? and a.fechaingreso <= ?) as correo_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='M' and b.idarea=resp.idarea and a.fechaingreso >= ? and a.fechaingreso <= ?) as comision_tot, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='M' and b.idarea=resp.idarea and b.estatus='A' and a.fechaingreso >= ? and a.fechaingreso <= ?) as comision_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='M' and b.idarea=resp.idarea and b.estatus='P' and a.fechaingreso >= ? and a.fechaingreso <= ?) as comision_p, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='R' and b.idarea=resp.idarea and a.fechaingreso >= ? and a.fechaingreso <= ?) as reunion_tot, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='R' and b.idarea=resp.idarea and b.estatus='A' and a.fechaingreso >= ? and a.fechaingreso <= ?) as reunion_a, "
              + "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and a.tipoasunto='R' and b.idarea=resp.idarea and b.estatus='P' and a.fechaingreso >= ? and a.fechaingreso <= ?) as reunion_p "
              + "FROM controlasuntospendientesnew.responsable as resp "
              + "inner join controlasuntospendientesnew.area as ar on resp.idarea=ar.idarea " 
              + "where resp.idarea in (";
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
      }
      sql = sql.substring(0, sql.length()-2) + ") ";
      sql+="group by resp.idarea, ar.siglas order by responsable";
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2, fecha1, fecha2);
      return datos;
  }
  public List<GraficaDTO> xareaacuerdo(String stat, List areas, String fecha1, String fecha2) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String alias="";
      if(stat.equals("A")) alias = "acuerdo_a";
      if(stat.equals("P")) alias = "acuerdo_p";
      String sql = "select distinct(b.idarea), trim(ar.siglas) as responsable, count(1) as "+alias+" "
              + "from controlasuntospendientesnew.accion as a "
              + "inner join controlasuntospendientesnew.responsable as b on a.idaccion=b.idaccion "
              + "inner join controlasuntospendientesnew.area as ar on b.idarea=ar.idarea "
              + "where a.estatus=? and b.idarea in (";
      for (int i = 0; i < areas.size(); i++) {
          AreaDTO ar = (AreaDTO) areas.get(i);
          sql+=ar.getIdarea()+", ";
      }
      sql = sql.substring(0, sql.length()-2) + ") ";
      sql+="and a.fecha >= ? and a.fecha <= ? group by b.idarea,ar.siglas order by responsable";
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, stat, fecha1, fecha2);
      return datos;
  }
public List<GraficaDTO> xasuntoareaUna(int area, String anio) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "select c.nombre as responsable,CASE WHEN a.tipoasunto='K' THEN 'SIA' "
              + "WHEN a.tipoasunto='R' THEN 'Reunión' "
              + "WHEN a.tipoasunto='M' THEN 'Comisión' "
              + "WHEN a.tipoasunto='C' THEN 'Correo' "
              + "END as asunto, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,4)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as atendido, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a.tipoasunto=a2.tipoasunto and substring(a2.fechaingreso,1,4)=? and a2.idasunto in (select idasunto from controlasuntospendientesnew.responsable as b where b.idarea = ?)) as pendiente "
              + "from controlasuntospendientesnew.asunto as a,controlasuntospendientesnew.area as c "
              + "where c.idarea=? and a.tipoasunto<>'R' group by a.tipoasunto,c.nombre";
      Object[] params = {anio, area, anio, area, area};
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, params);
      return datos;
}
public List<GraficaDTO> xasuntoareareunionUna(int area, String anio) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "select 'Reunión' as asunto, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='A' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,4)=? and a2.idarea = ?) as atendido, "
              + "(select count(1) from controlasuntospendientesnew.asunto as a2 where a2.estatus='P' and a2.tipoasunto='R' and substring(a2.fechaingreso,1,4)=? and a2.idarea = ?) as pendiente "
              + "from controlasuntospendientesnew.asunto as a where a.tipoasunto='R' group by a.tipoasunto";
      Object[] params = {anio, area, anio, area};
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, params);
      return datos;
  }
  public List<GraficaDTO> xasuntoareaacuerdoUna(int area, String anio) throws SQLException,  NamingException  {
      DataSource ds = AdministradorDataSource.getDataSource();
      QueryRunner qr = new QueryRunner(ds);
      String sql = "select 'Acuerdo' as asunto, a.idarea, "
              + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion)) as total, "
              + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,4)=?) and estatus='A') as atendido, "
              + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where substring(fecha,1,4)=?) and estatus='P') as pendiente "
              + "from controlasuntospendientesnew.responsable as a where a.idarea=? group by a.idarea";
      Object[] params = {area, area, anio, area, anio, area};
      ResultSetHandler rsh = new BeanListHandler(GraficaDTO.class);
      List<GraficaDTO> datos = (List<GraficaDTO>) qr.query(sql, rsh, params);
      return datos;
  }
}