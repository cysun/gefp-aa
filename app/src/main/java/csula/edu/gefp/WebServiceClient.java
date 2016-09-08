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
    public static final String BASE_URL = "http://10.0.2.2:8080/gefp/api/";

    public static final String ENDPOINT_LOGIN = "login.html";
    public static final String ENDPOINT_PLAN = "userplan.html";
    public static final String ENDPOINT_CELL = "mobile-user-plan.html";

    public static URL buildUrl(String endpoint, Map<String, String> params) throws IOException {
        Uri.Builder uriBuilder = Uri.parse(BASE_URL + endpoint).buildUpon();
        if (params != null)
            for (Map.Entry<String, String> param : params.entrySet())
                uriBuilder.appendQueryParameter(param.getKey(), param.getValue());
        return new URL(uriBuilder.build().toString());
    }

    public static JsonParser getJson(String endpoint, Map<String, String> params) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (buildUrl(endpoint, params)).openConnection();
        return new JsonParser(connection.getInputStream());
    }

    public static User login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        User user = null;
        try {
            JsonParser json = getJson(ENDPOINT_LOGIN, params);
            user = json.getUser();
        } catch (IOException e) {
            Log.e("WebServiceClient", "Login Error", e);
        }
        return user;
    }

    public static FlightPlan getFlightPlan(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);

        FlightPlan flightPlan = null;
        try {
            JsonParser json = getJson(ENDPOINT_PLAN, params);
            flightPlan = json.getFlightPlan();
        } catch (IOException e) {
            Log.e("WebServiceClient", "Flight Plan Error", e);
        }
        return flightPlan;
    }

}
