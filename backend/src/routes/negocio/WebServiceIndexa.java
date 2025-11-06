/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author RICARDO.SERNA
 */
public class WebServiceIndexa {

    public void ejecutaIndexacion() throws IOException{
        HttpGet request1 = new HttpGet("http://10.153.3.31:8983/solr/AsDGGMA/dataimport?command=full-import");
        HttpClient client = new DefaultHttpClient();
        client.execute(request1);
    }
    
    /*public void insertaRegistrosSolr(String params, List<ResponsableBean> responsables) throws IOException, MessagingException, EmailException {
        HttpClient client = null;
        HttpGet request1 = null;
        String paramIdarea="";
        String url = "http://10.210.140.70:8082/ws_asuntosdggma/webresources/setdatos/";
        //String url = "http://10.108.12.1:8080/ServicioRestAsDGGMA/webresources/setdatos/"; IP de Moni
        for(ResponsableBean resp:responsables){
            paramIdarea=",idarea:%22"+resp.getArea().getIdarea()+"%22";
            request1 = new HttpGet(url+params+paramIdarea+"%7D");
            //System.out.println(request1);
            client = new DefaultHttpClient();
            try {
                client.execute(request1);
            } catch (Exception e) {
                String mensajeHtml = "<html><body>"+e+"</body><html>"; 
                MimeMultipart mimemultipart = new MimeMultipart();
                mimemultipart.setSubType("alternative");
                MimeBodyPart msgText = new MimeBodyPart();
                msgText.setText(mensajeHtml);
                mimemultipart.addBodyPart(msgText);
                MimeMultipart htmlContent = new MimeMultipart();
                htmlContent.setSubType("related");
                MimeBodyPart mimebodypart = new MimeBodyPart();
                mimemultipart.addBodyPart(mimebodypart);
                mimebodypart.setContent(htmlContent);
                MimeBodyPart msgHtml = new MimeBodyPart();
                msgHtml.setContent(mensajeHtml, "text/html");
                htmlContent.addBodyPart(msgHtml);
                SimpleEmail email = new SimpleEmail();
                email.setHostName("10.1.32.15");
                email.addTo("ricardo.serna@inegi.org.mx","Ricardo S.");
                email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
                email.setSubject("%Reporte Diario "+Utiles.getFecha()+" a las "+Utiles.getHora() );
                email.setContent(mimemultipart);
                if (DatosGlobales.ESTADO_PRODUCCION){
                   email.send();
                }
            }
        }
    }*/
}
