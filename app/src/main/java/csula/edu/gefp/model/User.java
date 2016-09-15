package csula.edu.gefp.model;

public class User {

    Long id;

    String accessKey, firstName, lastName, cin, email;

    Department major;

    boolean validLogin;

    public User() {
        validLogin = false;
        major = new Department();
    }

    public void copyTo(User another) {
        another.id = id;
        another.accessKey = accessKey;
        another.firstName = firstName;
        another.lastName = lastName;
        another.cin = cin;
        another.email = email;
        major.copyTo(another.major);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getMajor() {
        return major;
    }

    public void setMajor(Department major) {
        this.major = major;
    }

    public boolean isValidLogin() {
        return validLogin;
    }

    public void setValidLogin(boolean validLogin) {
        this.validLogin = validLogin;
    }

}
