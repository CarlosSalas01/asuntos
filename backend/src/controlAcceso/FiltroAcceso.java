/*
 * FiltroAcceso.java
 *
 * Created on 15 de octubre de 2006, 01:26 PM
 */
package mx.org.inegi.dggma.sistemas.asuntos.controlAcceso;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FiltroAsunto;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ParamIniciales;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;


/**
 *
 * @author  Jos Luis Mondragn
 * @version
 */
public class FiltroAcceso implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public FiltroAcceso() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("FiltroAcceso:DoBeforeProcessing");
        }
    //
    // Write code here to process the request and/or response before
    // the rest of the filter chain is invoked.
    //

    //
    // For example, a logging filter might log items on the request object,
    // such as the parameters.
    //
        /*
    for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
    String name = (String)en.nextElement();
    String values[] = request.getParameterValues(name);
    int n = values.length;
    StringBuffer buf = new StringBuffer();
    buf.append(name);
    buf.append("=");
    for(int i=0; i < n; i++) {
    buf.append(values[i]);
    if (i < n-1)
    buf.append(",");
    }
    log(buf.toString());
    }
     */

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("FiltroAcceso:DoAfterProcessing");
        }
    //
    // Write code here to process the request and/or response after
    // the rest of the filter chain is invoked.
    //

    //
    // For example, a logging filter might log the attributes on the
    // request object after the request has been processed.
    //
        /*
    for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
    String name = (String)en.nextElement();
    Object value = request.getAttribute(name);
    log("attribute: " + name + "=" + value.toString());

    }
     */
    //

    //
    // For example, a filter might append something to the response.
    //
        /*
    PrintWriter respOut = new PrintWriter(response.getWriter());
    respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
     */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("FiltroAcceso:doFilter()");
        }

        doBeforeProcessing(request, response);

        Throwable problem = null;

        try {
            HttpServletRequest _request = (HttpServletRequest) request;
            HttpServletResponse _response = (HttpServletResponse) response;
            HttpSession sesion = _request.getSession();

            UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
            boolean logueado = true;
            if (usuario == null || sesion.isNew() || sesion == null) {
                logueado = false;
            }
            
            
            
            if (logueado) {
                chain.doFilter(request, response);
            } else {
                
                String id = request.getParameter("idasunto");
                String reporte = request.getParameter("reporte");
                
                if ((id != null || reporte != null))
                {
                   String tipo = request.getParameter("modo");
                   String areaFiltro = request.getParameter("areaFiltro");
                   String estatusFechaA = request.getParameter("estatusFechaA");
                   String estatusResponsable = request.getParameter("estatusResponsable"); 
                   String estatusAsunto = request.getParameter("estatusA");
                   String fecha1 = request.getParameter("fecha1");
                   String fecha2 = request.getParameter("fecha2");
                   String tipoFechas = request.getParameter("fechas");

                   ParamIniciales param = new ParamIniciales();
                   param.setIdAsunto(id);
                   param.setTipoConsulta(tipo);
                   param.setEstatusAsunto(estatusAsunto);
                   param.setAreaFiltro(areaFiltro);
                   param.setReporte(reporte);
                   param.setEstatusFechaA(estatusFechaA);
                   param.setEstatusResponsable(estatusResponsable);
                   param.setFecha1(fecha1);
                   param.setFecha2(fecha2);
                   param.setTipoFechas(tipoFechas);
                   
                   sesion.setAttribute("paramIniciales", param);
                }
                RequestDispatcher salto = _request.getRequestDispatcher("/login.jsp");
                salto.forward(_request, _response);
            }
        } catch (Throwable t) {
            //
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            //
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

        //
        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        //
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {

        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {

        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("FiltroAcceso:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {

        if (filterConfig == null) {
            return ("FiltroAcceso()");
        }
        StringBuffer sb = new StringBuffer("FiltroAcceso(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());

    }

    private void sendProcessingError(Throwable t, ServletResponse response) {

        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {

            try {

                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
                ;
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
                ;
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {

        String stackTrace = null;

        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
    private static final boolean debug = false;
}
