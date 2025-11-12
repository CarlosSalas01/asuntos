/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import java.io.IOException;
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
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;

/**
 *
 * @author jacqueline
 */
public class ConsultaTurnoKEET extends HttpServlet {

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
         FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroConsulta");
         AreasConsulta areasConsulta = (AreasConsulta) sesion.getAttribute("areasConsulta");
         FachadaDAO fachada = new FachadaDAO(areasConsulta);
         FachadaUsuarioArea fu = new FachadaUsuarioArea();

         try {
           ListaPaginador p = (ListaPaginador) sesion.getAttribute("paginador");
           List<AsuntoBean> asuntos = null;
           String recarga = request.getParameter("recarga");
           int noregTotal=0;
           if (recarga == null) {
                filtro.setOffset(0); //Se consulta desde el primer registro
                asuntos = fachada.buscarAsuntosPorAreaKeet(filtro);
                noregTotal = fachada.cantidadAsuntosPorAreaxTipo(filtro, "K");
                p = Paginador.obtenerPaginadorL(noregTotal, 1);
                
           } else {
                //int totalReg  = Integer.parseInt(request.getParameter("totalReg"));
                String pagStr = request.getParameter("pagina");
                if (pagStr != null) {
                  p.setPaginaActual(Integer.parseInt(pagStr));
                }  
                p = Paginador.obtenerPaginadorL(p.getTotalR(), p.getPaginaActual());
                filtro.setOffset((p.getPaginaActual()-1)*50); //se obtiene la sig pag
                asuntos = fachada.buscarAsuntosPorAreaKeet(filtro);
           }
            
            sesion.setAttribute("paginador", p);
            request.setAttribute("asuntosConsulta", asuntos);
            request.setAttribute("NoAsuntos", asuntos.size());
           
            if (filtro.getIdarea() > 0) {
              request.setAttribute("areaFiltro", fu.buscaArea(filtro.getIdarea()));
            }
            
            RequestDispatcher salta = request.getRequestDispatcher("consultaSIA.jsp");
            salta.forward(request, response);
         } catch (Exception ex) {
            Logger.getLogger(ConsultaTurnoKEET.class.getName()).log(Level.SEVERE, null, ex);
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
