/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AsuntoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreasConsulta;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FiltroAsunto;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ReporteArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministraUsuariosAreas;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministradorReportes;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.DelegadoNegocio;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

/**
 *
 * @author RICARDO.SERNA
 */
public class ResumenInicio extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession sesion = request.getSession();
            UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
            response.setContentType("application/x-json;charset=ISO-8859-1");
            String json_data = "";
            String admin = usuario.getPermisoActual().getDatos().getRol();
            int esAdjunto = 0;
            try {
                esAdjunto = usuario.getPermisoActual().getAreaBean().getDatos().getNivel();
            } catch (Exception e) {
                esAdjunto = 0;
            }

            AdministraUsuariosAreas areasAll = new AdministraUsuariosAreas();
            //List<AreaBean> areas = areasAll.listaAreasResponsablesXNivel(2);
            String tipo = request.getParameter("tipo"); // por si se requiere otra acci�n en este servlet
            FachadaUsuarioArea fua = new FachadaUsuarioArea();
            AreaBean direccionGeneral = fua.buscaArea(1);
            fua.cargaPermisosUsuarios(direccionGeneral.getResponsable());
            DelegadoNegocio delegado = new DelegadoNegocio(null);
            AreasConsulta areasConsulta = delegado.obtenAreasCaptura(direccionGeneral.getResponsable());
            FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroReporte");
            if (filtro == null) {
                  filtro = new FiltroAsunto();
            }
            int vencidos=0, porvencer=0, activos=0;
            int idarea=0, totalPendientes=0;
            AdministradorReportes admonReporte = new AdministradorReportes(areasConsulta);
            if(tipo.equals("0")) {
                String fecha = Utiles.getFechaHoy(); // formato 20181001
                String fechaIni="", fechaFin="";
                String anioActual = fecha.substring(0,4);
                String anioConsulta = "";
                String fechaHra = Utiles.getFechaFlexible(Utiles.getFechaHora());
                String idAdjunta = request.getParameter("idAdjunta");
                boolean otroAn = false;
                if(request.getParameter("otroAnio") != null && request.getParameter("otroAnio").trim().length() > 0) {
                    anioConsulta = request.getParameter("otroAnio");
                    fechaIni = anioConsulta+"0101";
                    fechaFin = anioConsulta+"1231";
                    if(anioConsulta.equals(anioActual)) otroAn = false;
                    else otroAn = true;
                } else {
                    anioConsulta = fecha.substring(0,4);
                    fechaIni = fecha.substring(0,4)+"0101";
                    fechaFin = fecha;
                }
                fechaIni = fechaIni.substring(0,4)+"-"+fechaIni.substring(4,6)+"-"+fechaIni.substring(6,8);
                fechaFin = fechaFin.substring(0,4)+"-"+fechaFin.substring(4,6)+"-"+fechaFin.substring(6,8);

                //String url1 = "http://10.210.140.70:8082/ws_asuntosdggma/webresources/getdatos/%7Bresumen:%221%22%7D"; // anterior
                String url1 = "http://10.153.3.31:3002/asuntos_api/getdatos/%7B%22resumen%22%3A%22true%22%7D";
                JsonArray myJsonArray = obtenJsonArray(url1); // La API devuelve array directo, no objeto
                //String paramsFechas = "fAsTot1:'"+fechaIni+"',fAsTot2:'"+fechaFin+"'";
                String paramsFechas = "%7B%22fAsTot1%22%3A%22" + fechaIni + "%22%2C%22fAsTot2%22%3A%22" + fechaFin+ "%22%7D";
                //String url2 = "http://10.210.140.70:8082/ws_asuntosdggma/webresources/getdatos/%7B"+paramsFechas+"%7D"; // anterior
                String url2 = "http://10.153.3.31:3002/asuntos_api/getdatos/"+paramsFechas;
                JsonObject myJson2 = obtenJson(url2);
                JsonArray elementos0 = myJson2.get("resumenAsunto").getAsJsonArray();
                int atendidosTotal = 0;
                int pendientesTotal = 0;
                int reunionesSA = 0;
                for (JsonElement reg : elementos0) {
                    JsonObject rootObject = reg.getAsJsonObject();
                    atendidosTotal = rootObject.get("atendidos").getAsInt();
                    pendientesTotal = rootObject.get("pendientes").getAsInt();
                    // reunionesSA obtenido de la API ya no se usa, se calcula desde BD
                }
                
                // Calcular reuniones sin acuerdos desde la base de datos
                reunionesSA = obtenerReunionesSinAcuerdos();

                List<JSONObject> resultado = new ArrayList<JSONObject>();
                JSONObject noList = new JSONObject();
                noList.put("fechaHora",fechaHra);
                noList.put("atendidosTodos",atendidosTotal);
                noList.put("pendientesTodos",pendientesTotal);
                noList.put("reunionesSA",reunionesSA);
                resultado.add(noList);

                JsonArray elementos = myJsonArray; // Usar el array directamente
                idarea = 0;
                int idAdjuntaInt = 0;
                if(!idAdjunta.equals("0") && !idAdjunta.equals("1")) {
                    idAdjuntaInt = Integer.parseInt(idAdjunta);
                    for (JsonElement reg : elementos) {
                        JsonObject rootObject = reg.getAsJsonObject();
                        idarea = rootObject.get("idarea").getAsInt();
                        if(idarea == idAdjuntaInt) {
                            JSONObject simple = new JSONObject();
                            simple.put("atendidos", 0); // es la suma de atendidos de todas las áreas
                            simple.put("atendidosArea", rootObject.get("atendidos").getAsString());
                            simple.put("enviados", 0);
                            simple.put("idarea", rootObject.get("idarea").getAsString());
                            simple.put("nivel", 0);
                            simple.put("pendactivos", 0);
                            simple.put("pendientes", rootObject.get("pendientes").getAsInt());
                            simple.put("pendientesAntes", 0);
                            simple.put("reunionesSA", 0);
                            simple.put("siglas", rootObject.get("siglas").getAsString());
                            simple.put("vencidos", rootObject.get("vencidos").getAsInt());
                            simple.put("porvencer", rootObject.get("porVencer").getAsInt());
                            simple.put("sinvencer", rootObject.get("sinVencer").getAsInt());
                            resultado.add(simple);
                        }
                    }                        
                } else {
                    for (JsonElement reg : elementos) {
                        JsonObject rootObject = reg.getAsJsonObject();
                        JSONObject simple = new JSONObject();
                        simple.put("atendidos", 0); // es la suma de atendidos de todas las áreas
                        simple.put("atendidosArea", rootObject.get("atendidos").getAsString());
                        simple.put("enviados", 0);
                        simple.put("idarea", rootObject.get("idarea").getAsString());
                        simple.put("nivel", 0);
                        simple.put("pendactivos", 0);
                        simple.put("pendientes", rootObject.get("pendientes").getAsInt());
                        simple.put("pendientesAntes", 0);
                        simple.put("reunionesSA", 0);
                        simple.put("siglas", rootObject.get("siglas").getAsString());
                        simple.put("vencidos", rootObject.get("vencidos").getAsInt());
                        simple.put("porvencer", rootObject.get("porVencer").getAsInt());
                        simple.put("sinvencer", rootObject.get("sinVencer").getAsInt());
                        resultado.add(simple);
                    }
                }

                // Ordenar alfabéticamente por siglas (excepto el primer elemento que contiene los totales)
                if (resultado.size() > 1) {
                    JSONObject totales = resultado.get(0); // Guardar el primer elemento (totales)
                    List<JSONObject> areas = new ArrayList<JSONObject>(resultado.subList(1, resultado.size()));
                    
                    // Ordenar las áreas alfabéticamente por siglas
                    Collections.sort(areas, new Comparator<JSONObject>() {
                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String siglasA = (String) a.get("siglas");
                            String siglasB = (String) b.get("siglas");
                            return siglasA.compareToIgnoreCase(siglasB);
                        }
                    });
                    
                    // Reconstruir la lista con totales primero y luego áreas ordenadas
                    resultado.clear();
                    resultado.add(totales);
                    resultado.addAll(areas);
                }

                json_data = new Gson().toJson(resultado);
                response.getWriter().write(json_data);
/////////////////////
                    
                    /*List<ResumenAreaSM> resumen = fachada.obtenResumenInicio(fechaIni, fechaFin, areas);
                    List<ResumenAreaSM> resumenAC = fachada.obtenResumenAcInicio(fechaIni, fechaFin, areas);
                    List<ResumenAreaSM> resumenJoin = new ArrayList<ResumenAreaSM>();
                    for (int i = 0; i < resumen.size(); i++) {
                        ResumenAreaSM resJoin = new ResumenAreaSM();
                        ResumenAreaSM res1 = new ResumenAreaSM();
                        res1 = (ResumenAreaSM) resumen.get(i);
                        //System.out.println("Asuntos - Area "+res1.getIdarea()+"-"+res1.getSiglas()+", At. ,"+res1.getAtendidos()+", Pend., "+res1.getPendientes());
                        for (int j = 0; j < resumenAC.size(); j++) {
                            ResumenAreaSM res2 = new ResumenAreaSM();
                            res2 = (ResumenAreaSM) resumenAC.get(j);
                            //System.out.println("Acuerdos - Area: "+res2.getIdarea()+"-"+res2.getSiglas()+", At. ,"+res2.getAtendidos()+", Pend., "+res2.getPendientes());
                            if(res1.getIdarea() == (res2.getIdarea())) {
                                resJoin.setIdarea(res1.getIdarea());
                                resJoin.setSiglas(res1.getSiglas());
                                resJoin.setAtendidosArea(res1.getAtendidos());
                                //resJoin.setPendientes(res1.getPendientes());
                                //resJoin.setAtendidos(res1.getAtendidos() + res2.getAtendidos());
                                if(otroAn) {
                                    //System.out.println(res1.getIdarea() + " "+res1.getPendientes());
                                    resJoin.setPendientesAntes(res1.getPendientes() + res2.getPendientes());
                                }
                            }
                        }
                        resumenJoin.add(resJoin);
                    } //data[i].vencidos, data[i].porvencer, data[i].pendactivos,
                    int reunionSAc = fachada.reunionesSinAcuerdo(anioConsulta);
                    resumenJoin.get(0).setReunionesSA(reunionSAc);
                    resumenJoin.get(0).setFechaHora(fechaHra);

                    String fecha1 = DatosGlobales.anioBase();
                    filtro.setFechaInicio(fecha1);
                    filtro.setFechaFinal(Utiles.getFechaHoy());
                    List<ReporteArea> reporte2 = admonReporte.obtenReporteArea(areasConsulta.getAreasResponsables(), filtro);
                    
                    for (int i = 0; i < reporte2.size(); i++) {
                        AreaDTO res3 = new AreaDTO();
                        res3 = (AreaDTO) reporte2.get(i).getArea();
                        idarea = res3.getIdarea();
                        for (int j = 0; j < reporte2.get(i).getResumen().size(); j++) {
                            ResumenArea res4 = new ResumenArea();
                            res4 = (ResumenArea) reporte2.get(i).getResumen().get(j);
                            vencidos += res4.getVencidos_d();
                            porvencer += res4.getPorvencer_d();
                            activos += res4.getPendactivos_d();
                            
                        }
                        totalPendientes += vencidos+porvencer+activos;
                        for (int x = 0; x < resumenJoin.size(); x++) {
                            ResumenAreaSM unido = new ResumenAreaSM();
                            unido = (ResumenAreaSM) resumenJoin.get(x);
                            if(unido.getIdarea() == idarea) {
                                unido.setVencidos(vencidos);
                                unido.setPorvencer(porvencer);
                                unido.setPendactivos(activos);
                                if(otroAn) unido.setPendientes(totalPendientes);
                            }
                            //System.out.println("Unido - Area: "+unido.getIdarea()+"-"+unido.getSiglas()+", At. ,"+unido.getAtendidos()+", Pend. ,"+unido.getPendientes());
                        }
                        vencidos=0;
                        porvencer=0;
                        activos=0;
                        totalPendientes=0;
                    }
                    ResumenAreaSM porAnio = fachada.obtenResumenIni(fechaIni, fechaFin);
                    
                    if(!otroAn){
                        resumenJoin.get(0).setPendientes(porAnio.getPendientes());
                        resumenJoin.get(0).setAtendidos(porAnio.getAtendidos());
                    }
                    json_data = new Gson().toJson(resumenJoin);
                    response.getWriter().write(json_data);
*/
            }
            if(tipo.equals("1")) {
                String idareaStr = request.getParameter("idarea");
                String fecha1 = DatosGlobales.anioBase();
                filtro.setFechaInicio(fecha1);
                filtro.setFechaFinal(Utiles.getFechaHoy());
                filtro.setFechaIniAnioAct(Utiles.getFechaHoyAnio()+"0101");
                List<ReporteArea> reportePorArea = admonReporte.obtenReportePorArea(Integer.parseInt(idareaStr), filtro);
                FachadaUsuarioArea areaUsu = new FachadaUsuarioArea();
                AreaBean area = areaUsu.buscaArea(Integer.parseInt(idareaStr));
                //reportePorArea.get(0).getArea().setSiglas(area.getDatos().getSiglas());
                reportePorArea.get(0).setArea(area.getDatos());
                json_data = new Gson().toJson(reportePorArea);
                response.getWriter().write(json_data);
            }
//            } else {
//                json_data = new Gson().toJson("fail");
//                response.getWriter().write(json_data);
//            }
        } catch (Exception e) {
            Logger.getLogger(ResumenInicio.class.getName()).log(Level.SEVERE, null, e);
            
            throw new RuntimeException(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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

    private JsonObject obtenJson(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        // Configurar timeouts para evitar bloqueos
        client.getParams().setParameter("http.connection.timeout", 5000); // 5 segundos para conectar
        client.getParams().setParameter("http.socket.timeout", 10000); // 10 segundos para leer datos
        
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charset.forName("UTF-8")));
        String output = "";
        boolean keepGoing = true;
        while (keepGoing) {
            String currentLine = br.readLine();
            if (currentLine == null) {
                keepGoing = false;
            } else {
                output += currentLine;
            }
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject myJson = jsonParser.parse(output).getAsJsonObject();
        return myJson;
    }
    
    private JsonArray obtenJsonArray(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        // Configurar timeouts para evitar bloqueos
        client.getParams().setParameter("http.connection.timeout", 5000); // 5 segundos para conectar
        client.getParams().setParameter("http.socket.timeout", 10000); // 10 segundos para leer datos
        
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charset.forName("UTF-8")));
        String output = "";
        boolean keepGoing = true;
        while (keepGoing) {
            String currentLine = br.readLine();
            if (currentLine == null) {
                keepGoing = false;
            } else {
                output += currentLine;
            }
        }
        JsonParser jsonParser = new JsonParser();
        JsonArray myJsonArray = jsonParser.parse(output).getAsJsonArray();
        return myJsonArray;
    }
    
    /**
     * Obtiene el conteo de reuniones sin acuerdos desde la base de datos
     * @return cantidad de reuniones sin acuerdos
     */
    private int obtenerReunionesSinAcuerdos() {
        int cantidad = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Usar AsuntoDAO que tiene acceso al DataSource
            AsuntoDAO asuntoDAO = new AsuntoDAO(null);
            
            // Obtener conexión usando DriverManager directamente
            conn = java.sql.DriverManager.getConnection(
                DatosGlobales.getURLJDBC(),
                DatosGlobales.getUserName(),
                DatosGlobales.getPassword()
            );
            
            String sql = "SELECT COUNT(*) AS cantidad " +
                        "FROM controlasuntospendientesnew.asunto " +
                        "WHERE idarea = 2 " +
                        "AND tipoasunto = 'R' " +
                        "AND NOT (idasunto IN (SELECT idasunto FROM controlasuntospendientesnew.accion GROUP BY idasunto)) " +
                        "AND estatus = 'P'";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception e) {
            Logger.getLogger(ResumenInicio.class.getName()).log(Level.SEVERE, 
                "Error al obtener reuniones sin acuerdos", e);
            cantidad = 0; // En caso de error, retornar 0
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                Logger.getLogger(ResumenInicio.class.getName()).log(Level.WARNING, 
                    "Error al cerrar recursos de BD", e);
            }
        }
        
        return cantidad;
    }
}
