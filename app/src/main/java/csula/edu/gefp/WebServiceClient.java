package csula.edu.gefp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csula.edu.gefp.model.Department;
import csula.edu.gefp.model.FlightPlan;
import csula.edu.gefp.model.User;

public class WebServiceClient {

    // private static final String BASE_URL = "http://sun.calstatela.edu/gefp/api/";
    private static final String BASE_URL = "http://10.0.2.2:8080/gefp/api/";

    public static final String ENDPOINT_LOGIN = "login.html";
    public static final String ENDPOINT_PLAN = "userplan.html";
    public static final String ENDPOINT_CELL = "mobile-user-plan.html";
    public static final String ENDPOINT_DEPARTMENTS = "departments.html";
    public static final String ENDPOINT_PROFILE = "updateprofile.html";

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

    public static JsonParser postJson(String endpoint, Map<String, String> params) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (params != null) {
            Uri.Builder builder = new Uri.Builder();
            for (Map.Entry<String, String> param : params.entrySet())
                builder.appendQueryParameter(param.getKey(), param.getValue());
            String query = builder.build().getEncodedQuery();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
        }

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

    public static List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        try {
            JsonParser json = getJson(ENDPOINT_DEPARTMENTS, null);
            departments = json.getDepartments();
        } catch (IOException e) {
            Log.d("WebServiceClient", "Departments Error", e);
        }
        return departments;
    }

    public static User updateProfile(User user) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user.getId().toString());
        params.put("accessKey", user.getAccessKey());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("cin", user.getCin());
        params.put("email", user.getEmail());
        params.put("dept_id", user.getMajor().getId() + "");

        User returnedUser = null;
        try {
            JsonParser json = postJson(ENDPOINT_PROFILE, params);
            returnedUser = json.getUser();
        } catch (IOException e) {
            Log.e("WebServiceClient", "Profile Update Error", e);
        }
        return returnedUser;
    }

}
