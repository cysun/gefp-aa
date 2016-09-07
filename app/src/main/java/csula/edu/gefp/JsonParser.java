package csula.edu.gefp;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import csula.edu.gefp.model.Department;
import csula.edu.gefp.model.FlightPlan;
import csula.edu.gefp.model.Runway;
import csula.edu.gefp.model.Stage;
import csula.edu.gefp.model.User;

/**
 * Spring's JsonView wraps one or more model objects inside a "result" object. So in the parser
 * methods we need to unwrap the "result" object before parsing the actual object.
 */
public class JsonParser {

    private JsonReader reader;

    public JsonParser(InputStream in) throws IOException {
        reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
    }

    public User getUser() throws IOException {
        reader.beginObject(); // start of the result object
        reader.nextName();
        reader.beginObject(); // start of the user object
        User user = new User();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    user.setId(reader.nextLong());
                    break;
                case "username":
                    user.setUsername(reader.nextString());
                    break;
                case "accessKey":
                    user.setAccessKey(reader.nextString());
                    break;
                case "firstName":
                    user.setFirstName(reader.nextString());
                    break;
                case "lastName":
                    user.setLastName(reader.nextString());
                    break;
                case "middleName":
                    user.setMiddleName(reader.nextString());
                    break;
                case "cin":
                    user.setCin(reader.nextString());
                    break;
                case "email":
                    user.setEmail(reader.nextString());
                    break;
                case "major":
                    user.setMajor(getDepartment());
                    break;
                case "newAccount":
                    user.setNewAccount(reader.nextBoolean());
                    break;
                case "validLogin":
                    user.setValidLogin(reader.nextBoolean());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject(); // end of the user object
        reader.endObject(); // end of the result object
        return user.isValidLogin() ? user : null;
    }

    public FlightPlan getFlightPlan() throws IOException {
        reader.beginObject(); // start of the result object
        reader.nextName();
        reader.beginObject(); // start of the plan object
        FlightPlan plan = new FlightPlan();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    plan.setId(reader.nextLong());
                    break;
                case "name":
                    plan.setName(reader.nextString());
                    break;
                case "seasonYear":
                    plan.setSeasonYear(reader.nextString());
                    break;
                case "seasonName":
                    plan.setSeasonName(reader.nextString());
                    break;
                case "department":
                    plan.setDepartment(getDepartment());
                    break;
                case "runways":
                    reader.beginArray();
                    while (reader.hasNext())
                        plan.getRunways().add(getRunway());
                    reader.endArray();
                    break;
                case "stages":
                    reader.beginArray();
                    while (reader.hasNext())
                        plan.getStages().add(getStage());
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject(); // end of the flight plan object
        reader.endObject(); // end of the result object
        return plan.getId() != null ? plan : null;
    }

    private Department getDepartment() throws IOException {
        Department department = new Department();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    department.setId(reader.nextInt());
                    break;
                case "name":
                    department.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return department;
    }

    private Runway getRunway() throws IOException {
        Runway runway = new Runway();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    runway.setId(reader.nextLong());
                    break;
                case "name":
                    runway.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return runway;
    }

    private Stage getStage() throws IOException {
        Stage stage = new Stage();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    stage.setId(reader.nextLong());
                    break;
                case "name":
                    stage.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return stage;
    }

}
