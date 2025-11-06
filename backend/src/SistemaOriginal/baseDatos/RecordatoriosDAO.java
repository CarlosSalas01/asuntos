/*
 * AdvertenciasDAO.java
 *
 * Created on 22 de febrero de 2007, 10:17 PM
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.RecordatorioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class RecordatoriosDAO {

    /** Creates a new instance of AdvertenciasDAO */
    public RecordatoriosDAO() {
    }

    public static List<RecordatorioDTO> obtenerRecordatorios(Connection con, UsuarioBean usuario) throws SQLException {
        List<RecordatorioDTO> listado = new ArrayList<RecordatorioDTO>();

        List<RecordatorioDTO> listaVencimiento = obtenerRecordatoriosAsuntosVencimiento(con, usuario);
        //List<RecordatorioDTO> listaSinAcciones = obtenerRecordatoriosAsuntosSinAccion(con, usuario);

        for (RecordatorioDTO rec : listaVencimiento) {
            listado.add(rec);
        }

        /*
        for (RecordatorioDTO rec : listaSinAcciones) {
            listado.add(rec);
        }
        */

        return listado;
    }

    private static List<RecordatorioDTO> obtenerRecordatoriosAsuntosVencimiento(Connection con, UsuarioBean usuario) throws SQLException {
        List<RecordatorioDTO> lista = new ArrayList<RecordatorioDTO>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select idasunto, fechaatender, descripcion, urgente, descargaestatus " +
                "from controlasuntospendientesnew.asunto " +
                "where activoestatus=? and publicoestatus=? and descargaestatus<>? " +
                "and fechaatender<>? " +
                "and (idarea=? or idasunto in (select idasunto from controlasuntospendientesnew.corresponsable where idcorresponsable=?)) " +
                "and fechaatender<? " +
                "order by urgente, fechaatender";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, "S");
            pst.setString(2, "S");
            pst.setString(3, "D");
            pst.setString(4, "9999");
            pst.setInt(5, usuario.getPermisoActual().getAreaBean().getDatos().getIdarea());
            pst.setInt(6, usuario.getPermisoActual().getAreaBean().getDatos().getIdarea());
            pst.setString(7, getFechaAnioMesDia());
            rs = pst.executeQuery();
            while (rs.next()) {
                RecordatorioDTO rec = new RecordatorioDTO();
                String descripcion = rs.getString("descripcion");
                if (descripcion.length() > 50) {
                    descripcion = descripcion.substring(0, 50) + "...";
                }
                String texto = "El asunto " + rs.getInt("idasunto") + " (" + descripcion + ") vence el " + Utiles.getFechaFlexible(rs.getString("fechaatender").trim());
                rec.setIdAsunto(rs.getInt("idasunto"));
                rec.setTexto(texto);
                lista.add(rec);
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    private static List<RecordatorioDTO> obtenerRecordatoriosAsuntosSinAccion(Connection con, UsuarioBean usuario) throws SQLException {
        List<RecordatorioDTO> lista = new ArrayList<RecordatorioDTO>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select idasunto, fechaatender, descripcion, urgente, descargaestatus " +
                "from controlasuntospendientesnew.asunto " +
                "where activoestatus=? and publicoestatus=? and descargaestatus<>? " +
                "and (idarea=? or idasunto in (select idasunto from controlasuntospendientesnew.corresponsable where idcorresponsable=?)) " +
                "and not(idasunto in(select idasunto from controlasuntospendientesnew.accion acc where acc.activoestatus=?)) " +
                "order by urgente, fechaatender";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, "S");
            pst.setString(2, "S");
            pst.setString(3, "D");
            pst.setInt(4, usuario.getPermisoActual().getAreaBean().getDatos().getIdarea());
            pst.setInt(5, usuario.getPermisoActual().getAreaBean().getDatos().getIdarea());
            pst.setString(6, "S");

            rs = pst.executeQuery();
            while (rs.next()) {
                RecordatorioDTO rec = new RecordatorioDTO();
                String descripcion = rs.getString("descripcion");
                if (descripcion.length() > 50) {
                    descripcion = descripcion.substring(0, 50) + "...";
                }
                String texto = "El asunto " + rs.getInt("idasunto") + " (" + descripcion + ") no tiene acciones registradas";
                rec.setIdAsunto(rs.getInt("idasunto"));
                rec.setTexto(texto);
                lista.add(rec);
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    private static String getFechaAnioMesDia() {
        Calendar hoy = new GregorianCalendar();
        hoy.add(Calendar.DAY_OF_MONTH, 7);
        String fecha = "" + hoy.get(Calendar.YEAR) + dosDigitos(hoy.get(Calendar.MONTH) + 1) + dosDigitos(hoy.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }

    private static String dosDigitos(int numero) {
        String numeroStr = null;
        if (numero < 10) {
            numeroStr = "0" + numero;
        } else {
            numeroStr = "" + numero;
        }
        return numeroStr;
    }
}
