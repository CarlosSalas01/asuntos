/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.datosglobales;

import mx.org.inegi.dggma.sistemas.asuntos.controlAcceso.FiltroAcceso;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class Autenticar {

    private static final String URL = "http://intranet.wapp2.inegi.gob.mx/sistemas/informaticos/ws/v2/ldap.asmx/Autenticar";

    public static boolean validaUsuario(String username, String password, String claveAplicacion) {
        boolean result = false;
        StringBuilder callResult = new StringBuilder();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httPost = new HttpPost(URL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("loginUsr", username));
            params.add(new BasicNameValuePair("passUsr", password));
            params.add(new BasicNameValuePair("claveAplicacion", claveAplicacion));
            httPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            response = httpclient.execute(httPost);

            if (response.getStatusLine().getStatusCode() != 200) {
                result = false;
            }

            // Get the response
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            try ( BufferedReader rd = new BufferedReader(isr)) {
                String line;
                while ((line = rd.readLine()) != null) {
                    callResult.append(line);
                }
            }

            result = callResult.toString().contains("True");
        } catch (IOException ex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }
}
