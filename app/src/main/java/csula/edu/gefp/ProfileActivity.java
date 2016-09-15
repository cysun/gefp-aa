package csula.edu.gefp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import csula.edu.gefp.model.Department;
import csula.edu.gefp.model.User;
import csula.edu.gefp.model.UserData;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private boolean profileChanged;
    private List<Department> departments;

    private EditText profileFirstName, profileLastName, profileCin, profileEmail;

    private Spinner profileDepartment;
    private ArrayAdapter<Department> departmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = new User();
        UserData.getInstance(this).getUser().copyTo(user);
        profileChanged = false;

        profileFirstName = (EditText) findViewById(R.id.profileFirstName);
        profileFirstName.setText(user.getFirstName());
        profileFirstName.addTextChangedListener(new FieldTextWatcher("firstName"));
        profileLastName = (EditText) findViewById(R.id.profileLastName);
        profileLastName.setText(user.getLastName());
        profileLastName.addTextChangedListener(new FieldTextWatcher("lastName"));
        profileCin = (EditText) findViewById(R.id.profileCin);
        profileCin.setText(user.getCin());
        profileCin.addTextChangedListener(new FieldTextWatcher("cin"));
        profileEmail = (EditText) findViewById(R.id.profileEmail);
        profileEmail.setText(user.getEmail());
        profileEmail.addTextChangedListener(new FieldTextWatcher("email"));

        departments = new ArrayList<>();
        departments.add(user.getMajor());
        departmentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        departmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileDepartment = (Spinner) findViewById(R.id.profileDepartment);
        profileDepartment.setAdapter(departmentsAdapter);
        profileDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department department = departments.get(position);
                if (department.getId() != user.getMajor().getId()) {
                    user.setMajor(department);
                    profileChanged = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        (new DepartmentsTask()).execute();

        Button profileButton = (Button) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profileChanged) finish();
                else
                    (new ProfileTask()).execute();
            }
        });
    }

    private class FieldTextWatcher implements TextWatcher {

        private String fieldName;

        private FieldTextWatcher(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            profileChanged = true;
            switch (fieldName) {
                case "firstName":
                    user.setFirstName(s.toString());
                    break;
                case "lastName":
                    user.setLastName(s.toString());
                    break;
                case "cin":
                    user.setCin(s.toString());
                    break;
                case "email":
                    user.setEmail(s.toString());
                    break;
                default:
                    Log.w("ProfileActivity", "Unrecognized field: " + fieldName);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class DepartmentsTask extends AsyncTask<Void, Void, List<Department>> {

        @Override
        protected List<Department> doInBackground(Void... params) {
            return WebServiceClient.getDepartments();
        }

        @Override
        protected void onPostExecute(List<Department> departments) {
            ProfileActivity.this.departments = departments;
            departmentsAdapter.clear();
            departmentsAdapter.addAll(departments);
            departmentsAdapter.notifyDataSetChanged();
            for (int i = 0; i < departments.size(); ++i)
                if (departments.get(i).getId() == user.getMajor().getId()) {
                    profileDepartment.setSelection(i);
                    break;
                }
        }
    }

    private class ProfileTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            return WebServiceClient.updateProfile(user);
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            String msg = getResources().getString(R.string.toast_profile_failure);
            if (returnedUser != null) {
                UserData userData = UserData.getInstance(getApplicationContext());
                user.copyTo(userData.getUser());
                userData.toPreferences();
                profileChanged = false;
                msg = getResources().getString(R.string.toast_profile_success);
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

}
