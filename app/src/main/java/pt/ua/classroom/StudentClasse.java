package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;

class StudentClasse implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title,id;
    private static ArrayList<StudentClasse> classes = new ArrayList<>();

    private StudentClasse(String title,String id) {
        this.title = title;
        this.id = id;
    }

    private String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    static ArrayList<StudentClasse> getClasses() {
        return classes;
    }

    static void addClasse(String name, String id) {
        classes.add(new StudentClasse(name,id));
    }
}