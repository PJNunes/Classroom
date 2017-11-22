package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;

class TeachingClass implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title,id;
    private static ArrayList<TeachingClass> aClasses = new ArrayList<>();

    private TeachingClass(String title, String id) {
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

    static ArrayList<TeachingClass> getaClasses() {
        return aClasses;
    }

    static void addClasse(String name, String id) {
        aClasses.add(new TeachingClass(name,id));
    }

    static void removeClasse(String classId) {
        TeachingClass obj;
        if ((obj=getObject(classId))!=null)
            aClasses.remove(obj);
    }

    private static TeachingClass getObject(String id){
        for (TeachingClass obj: aClasses){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }
}
