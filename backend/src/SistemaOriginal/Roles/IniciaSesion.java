/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.BitacoraDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.RegistroConveniosDAO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.BitacoraDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministraUsuariosAreas;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministradorCorreo;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.DelegadoNegocio;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.vista.PermisosAreasUsuario;

/**
 *
 * @author jacqueline
 */
public class IniciaSesion extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
        String idPermisoStr = request.getParameter("idPermiso");

        if (usuario == null) {
          RequestDispatcher salta = request.getRequestDispatcher("/reporteInicial.do");
          salta.forward(request, response);
        }
        try {
            DelegadoNegocio delegado = new DelegadoNegocio(null);
            
            int idPermiso = idPermisoStr != null ? Integer.parseInt(idPermisoStr) : usuario.getPermisoActual().getDatos().getIdpermiso();
            PermisosAreasUsuario vistaUsuario =  delegado.asignaRolPermisos(usuario, idPermiso);
            AreasConsulta areasConsulta = delegado.obtenAreasConsultas(usuario);
            AreasConsulta areasCaptura = delegado.obtenAreasCaptura(usuario);
            FachadaDAO fachada = new FachadaDAO(null);
            sesion.setAttribute("vistaUsuario", vistaUsuario);
            sesion.setAttribute("areasConsulta", areasConsulta);
            sesion.setAttribute("areasCaptura", areasCaptura);
                    
            DelegadoNegocio delegado2 = new DelegadoNegocio(areasConsulta);
     
            List<ReunionSAcuerdo> ids = delegado2.IdreunionesSinAcuerdos();
            sesion.setAttribute("reunionesSAcuerdos", ids);
            sesion.setAttribute("NoAsuntosSA", ids.size());
           
            ParamIniciales param = (ParamIniciales) sesion.getAttribute("paramIniciales");
            
            String admin = usuario.getPermisoActual().getDatos().getRol();
            String fechaHoy = Utiles.getFechaDMA(); //formato 02/09/2016
            
            RegistroConveniosDAO captura = new RegistroConveniosDAO();
            String periodoConvenios = captura.permiteCaptura(); 
            
            boolean dia1=true;
            boolean diaLast=true;
            if(!admin.equals("A")) { //no aplica para los admin.
                String mes=fechaHoy.substring(4,6);
//                if(mes.equals("01") || mes.equals("03") || mes.equals("06") || mes.equals("09")) {
//                    
//                }
                List<UsuarioDTO> destinatarios = fachada.usuariosNivel2();
                String[] permiteCaptura = Utiles.permiteCapturaAvanceMes2(Integer.parseInt(fechaHoy.substring(6,10)), Integer.parseInt(fechaHoy.substring(3,5)), Integer.parseInt(fechaHoy.substring(0,2)));
//                String[] permiteCaptura = Utiles.permiteCapturaAvanceMes2(2016, 9, 8);
//                periodoConvenios = permiteCaptura[0];
//                periodoConvenios = "false"; // por instrucciones de Osvaldo/Angelica se cierra desde hoy 5 de oct.
                AdministradorCorreo correos = new AdministradorCorreo();
                //if(permiteCaptura[4].equals("true")) { // mandar correo avisando 1er. d�a de captura de convenios
//                if(dia1) { // mandar correo avisando 1er. d�a de captura de convenios
//                    correos.enviaCorreoConveniosAviso(destinatarios, "primero");
//                }
                if(fechaHoy.substring(0,2).equals("5")) { // mandar correo avisando �ltimo d�a de captura de convenios
                    if(diaLast) { // mandar correo avisando �ltimo d�a de captura de convenios
                        correos.enviaCorreoConveniosAviso(destinatarios, "ultimo");
                    }
                }
            }
            sesion.setAttribute("fechahoyDMA", fechaHoy);
            sesion.setAttribute("captura", periodoConvenios);
            sesion.setAttribute("idPermiso", idPermisoStr);
            String salto = "";
            if(usuario.getPermisoActual().getDatos().getRol().equals("V")) {
                salto = "consultaConvenios.do?modu=V&param=inicio";
            } else {
                if (param != null && param.getReporte()!= null) {
                   if (param.getReporte().equals("D")) {
                     salto = "consultaAsuntosFiltro.do?areaFiltro="+param.getAreaFiltro()+"&modo="+param.getTipoConsulta()+"&estatusFechaA="+param.getEstatusFechaA()+"&estatusResponsable="+param.getEstatusResponsable();  
                   } else if (param.getReporte().endsWith("SM")) {
                     salto = "consultaAsuntosFiltro.do?areaFiltro="+param.getAreaFiltro()+"&modo="+param.getTipoConsulta()+"&estatusAsunto="+param.getEstatusAsunto()+"&fecha1="+param.getFecha1()+"&fecha2="+param.getFecha2()+"&fechas="+param.getTipoFechas();  
                   } else {
                     salto = "consultaAsunto.do?idasunto="+param.getIdAsunto()+"&tipo="+param.getTipoConsulta();  
                   }
                   sesion.setAttribute("paramIniciales",null);
                } else {
                   salto = "inicio.jsp";
                }
            }
            int idAreaSel = fachada.idAreaByPermiso(idPermiso);
            sesion.setAttribute("idAreaSel", String.valueOf(idAreaSel));
            
            BitacoraDAO bitacora = new BitacoraDAO();
            BitacoraDTO bit = new BitacoraDTO();
            bit.setId(0);
            bit.setFecha(Utiles.getFechaHora());
            bit.setObservaciones(usuario.getNombreCompleto());
            bit.setIdusuariomodificacion(usuario.getDatos().getIdusuario());
            bit.setAccion("Acceso al sistema");
            bit.setIdarea(0);
            bitacora.grabaBitacora(bit);

            //sesion.setAttribute("filtroConsulta", filtro);
            
            RequestDispatcher salta = request.getRequestDispatcher(salto);
            salta.forward(request, response);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } 
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
 }
