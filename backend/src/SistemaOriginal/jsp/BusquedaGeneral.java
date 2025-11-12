/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.DelegadoNegocio;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.tags.ElementoSuperSelect;
import mx.org.inegi.dggma.sistemas.asuntos.vista.PermisosAreasUsuario;

/**
 *
 * @author jacqueline
 */
public class BusquedaGeneral extends HttpServlet {

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
         AreasConsulta areasConsulta = (AreasConsulta) sesion.getAttribute("areasConsulta");
         

         if (usuario != null){
            FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroConsultaGeneral");
            
                // K=keet, C=correo, M=comisiones, R=reuniones
                String tipoFechas = request.getParameter("fechas"); 
                String fecha1 = request.getParameter("fecha1");
                String fecha2 = request.getParameter("fecha2");
                String idareaFiltro = request.getParameter("areaFiltro");
                String texto = request.getParameter("texto");


                if (tipoFechas != null){
                    filtro.setTipoFecha(tipoFechas);
                    filtro.setFechaInicioFormat(fecha1);
                    filtro.setFechaFinalFormat(fecha2);
                    filtro.setFechaInicio(Utiles.getSwapFecha(fecha1));            
                    filtro.setFechaFinal(Utiles.getSwapFecha(fecha2));
                }    

                if (idareaFiltro != null) {
                  filtro.setIdarea(Integer.parseInt(idareaFiltro));
                }
                
                if (texto != null) {
                   filtro.setTexto(texto);
                }
                   
               sesion.setAttribute("filtroConsulta", filtro);
                  
            try {
                
                DelegadoNegocio delegado = new DelegadoNegocio(areasConsulta);
                request.setAttribute("resultadosBusqueda", delegado.obtenerDatosBusqueda(filtro));

                RequestDispatcher salta = request.getRequestDispatcher("consultaAsuntosGeneral.jsp");
                
                salta.forward(request, response);

            } catch (Exception ex) {
                Logger.getLogger(ConsultaTurnoKEET.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
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
