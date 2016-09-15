package csula.edu.gefp.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * We only persist immutable user fields such as cin, username, and access key. Other user fields
 * are dynamically loaded on the profile page.
 */
public class UserData {

    private static UserData userData;
    private User user;
    private Context context;

    private UserData(Context context) {
        this.context = context.getApplicationContext();
        fromPreferences();
    }

    public static UserData getInstance(Context context) {
        if (userData == null) userData = new UserData(context);
        return userData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) toPreferences();
    }

    public void clear() {
        user = null;

        SharedPreferences.Editor editor = context.getSharedPreferences("gefp",
                Context.MODE_PRIVATE).edit();
        editor.remove("user.id");
        editor.remove("user.access.key");
        editor.remove("user.first.name");
        editor.remove("user.last.name");
        editor.remove("user.cin");
        editor.remove("user.email");
        editor.remove("user.major.id");
        editor.remove("user.major.name");
        editor.apply();
    }

    public void fromPreferences() {
        SharedPreferences preferences = context.getSharedPreferences("gefp",
                Context.MODE_PRIVATE);

        if (!preferences.contains("user.id") || !preferences.contains("user.access.key")) {
            user = null;
            return;
        }

        user = new User();
        user.id = preferences.getLong("user.id", 0);
        user.accessKey = preferences.getString("user.access.key", null);
        user.firstName = preferences.getString("user.first.name", null);
        user.lastName = preferences.getString("user.last.name", null);
        user.cin = preferences.getString("user.cin", null);
        user.email = preferences.getString("user.email", null);
        user.major.id = preferences.getInt("user.major.id", 0);
        user.major.name = preferences.getString("user.major.name", null);
    }

    public void toPreferences() {
        SharedPreferences.Editor editor = context.getSharedPreferences("csns",
                Context.MODE_PRIVATE).edit();

        editor.putLong("user.id", user.id);
        editor.putString("user.access.key", user.accessKey);
        editor.putString("user.first.name", user.firstName);
        editor.putString("user.last.name", user.lastName);
        editor.putString("user.cin", user.cin);
        editor.putString("user.email", user.email);
        editor.putInt("user.major.id", user.major.id);
        editor.putString("user.major.name", user.major.name);
        editor.apply();
    }

}
