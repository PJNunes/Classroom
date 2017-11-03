package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pedro Nunes.
 */

public class Classe implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title;
    private static ArrayList<Classe> classes = new ArrayList<>();

    private Classe(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    static ArrayList<Classe> getClasses() {
        return classes;
    }

    static void addClasse(String name) {
        classes.add(new Classe(name));
    }
}
