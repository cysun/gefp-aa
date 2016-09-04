package csula.edu.gefp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import csula.edu.gefp.model.User;
import csula.edu.gefp.model.UserData;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User user = UserData.getInstance(getApplicationContext()).getUser();
        if (user != null) startDefaultActivity();

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginUsername = (EditText) findViewById(R.id.loginUsername);
                EditText loginPassword = (EditText) findViewById(R.id.loginPassword);
                (new LoginTask()).execute(loginUsername.getText().toString(),
                        loginPassword.getText().toString());
            }
        });
    }

    private void startDefaultActivity() {
        // startActivity( new Intent( getApplicationContext(),
        //         NewsListActivity.class ) );

        // Close this activity and remove it from the activity stack so
        // the user cannot get back to it by clicking the Back button.
        finish();
    }

    private class LoginTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            return WebServiceClient.login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            String msg = getResources().getString(R.string.toast_login_failure);
            if (user != null) {
                UserData.getInstance(getApplicationContext()).setUser(user);
                msg = getResources().getString(R.string.toast_login_success);
                startDefaultActivity();
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
