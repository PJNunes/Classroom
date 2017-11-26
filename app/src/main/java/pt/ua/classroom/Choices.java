package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class Choices implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title;
    private static ArrayList<Choices> choices = new ArrayList<>();

    private Choices(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    static ArrayList<Choices> getChoices() {
        return choices;
    }

    static void addChoice(String name) {
        choices.add(new Choices(name));
    }

    static void removeChoice(String text) {
        Choices obj;
        if ((obj=getObject(text))!=null)
            choices.remove(obj);
    }

    private static Choices getObject(String text){
        for (Choices obj:choices){
            if (obj.getTitle().equals(text))
                return obj;
        }
        return null;
    }

    static void purge() {
        Iterator<Choices> iter = choices.iterator();

        while (iter.hasNext()) {
            Choices str = iter.next();
            iter.remove();
        }
    }
}