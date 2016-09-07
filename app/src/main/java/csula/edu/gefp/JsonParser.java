package csula.edu.gefp;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import csula.edu.gefp.model.Department;
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

}
