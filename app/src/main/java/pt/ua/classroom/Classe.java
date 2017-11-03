package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pedro Nunes.
 */

public class Classe implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title,id;
    private static ArrayList<Classe> classes = new ArrayList<>();

    private Classe(String title,String id) {
        this.title = title;
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    static ArrayList<Classe> getClasses() {
        return classes;
    }

    static void addClasse(String name, String id) {
        classes.add(new Classe(name,id));
    }
}
