package csula.edu.gefp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import csula.edu.gefp.model.FlightPlan;
import csula.edu.gefp.model.User;

public class WebServiceClient {

    // private static final String BASE_URL = "http://sun.calstatela.edu/gefp/api/";
    private static final String BASE_URL = "http://10.0.2.2:8080/gefp/api/";

    public static JsonParser getResult(String url, Map<String, String> params) throws IOException {
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (params != null)
            for (Map.Entry<String, String> param : params.entrySet())
                uriBuilder.appendQueryParameter(param.getKey(), param.getValue());

        HttpURLConnection connection = (HttpURLConnection) (new URL(uriBuilder.build().toString())).openConnection();
        return new JsonParser(connection.getInputStream());
    }

    public static User login(String username, String password) {
        String url = BASE_URL + "login.html";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        User user = null;
        try {
            JsonParser result = getResult(url, params);
            user = result.getUser();
        } catch (IOException e) {
            Log.e("WebServiceClient", "Login Error", e);
        }
        return user;
    }

    public static FlightPlan getFlightPlan(String userId) {
        String url = BASE_URL + "userplan.html";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);

        FlightPlan flightPlan = null;
        try {
            JsonParser result = getResult(url, params);
            flightPlan = result.getFlightPlan();
        } catch (IOException e) {
            Log.e("WebServiceClient", "Flight Plan Error", e);
        }
        return flightPlan;
    }

}
