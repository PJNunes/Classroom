package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;

class AttendingClass implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title,id;
    private static ArrayList<AttendingClass> classes = new ArrayList<>();

    private AttendingClass(String title, String id) {
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

    static ArrayList<AttendingClass> getClasses() {
        return classes;
    }

    static void addClasse(String name, String id) {
        classes.add(new AttendingClass(name,id));
    }

    static void removeClasse(String classId) {
        AttendingClass obj;
        if ((obj=getObject(classId))!=null)
            classes.remove(obj);
    }
    private static AttendingClass getObject(String id){
        for (AttendingClass obj:classes){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }

    static void purge() {
        for (AttendingClass obj:classes){
            classes.remove(obj);
        }
    }
}