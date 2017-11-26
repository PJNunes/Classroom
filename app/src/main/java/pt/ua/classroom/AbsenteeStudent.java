package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class AbsenteeStudent implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title, id;
    private static ArrayList<AbsenteeStudent> students = new ArrayList<>();

    private AbsenteeStudent(String title, String id) {
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

    static ArrayList<AbsenteeStudent> getStudents() {
        return students;
    }

    static void addStudent(String name, String id) {
        students.add(new AbsenteeStudent(name, id));
    }

    static void removeStudent(String studentId) {
        AbsenteeStudent obj;
        if ((obj=getObject(studentId))!=null)
            students.remove(obj);
    }

    private static AbsenteeStudent getObject(String id){
        for (AbsenteeStudent obj:students){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }

    static void purge() {
        Iterator<AbsenteeStudent> iter = students.iterator();

        while (iter.hasNext()) {
            AbsenteeStudent str = iter.next();
            iter.remove();
        }
    }
}