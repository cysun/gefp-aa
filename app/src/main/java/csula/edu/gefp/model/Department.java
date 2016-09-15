package csula.edu.gefp.model;

public class Department {

    int id;

    String name;

    public Department() {
    }

    public void copyTo(Department another) {
        another.id = id;
        another.name = name;
    }

    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
