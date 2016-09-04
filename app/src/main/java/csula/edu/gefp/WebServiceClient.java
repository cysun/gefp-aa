package csula.edu.gefp;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import csula.edu.gefp.model.User;

public class WebServiceClient {

    // private static final String BASE_URL = "http://sun.calstatela.edu/gefp/api/";
    private static final String BASE_URL = "http://10.0.2.2:8080/gefp/api/";

    public static byte[] getBytes(String url, Map<String, String> params) throws IOException {
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (params != null)
            for (Map.Entry<String, String> param : params.entrySet())
                uriBuilder.appendQueryParameter(param.getKey(), param.getValue());

        HttpURLConnection connection = (HttpURLConnection) (new URL(uriBuilder.build().toString())).openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + url);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0)
                out.write(buffer, 0, bytesRead);
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getJson(String url, Map<String, String> params) throws IOException {
        return new String(getBytes(url, params));
    }

    public static User login(String username, String password) {
        String url = BASE_URL + "login.html";
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        User user = null;
        try {
            String json = getJson(url, params);
            Log.d("WebServiceClient:login", json);
        } catch (IOException e) {
            Log.e("WebServiceClient:login", "Login Error", e);
        }
        return user;
    }

}
