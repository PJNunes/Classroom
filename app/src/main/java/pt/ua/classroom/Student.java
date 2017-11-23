package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class Student implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title, id;
    private static ArrayList<Student> students = new ArrayList<>();

    private Student(String title, String id) {
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

    static ArrayList<Student> getStudents() {
        return students;
    }

    static void addStudent(String name, String id) {
        students.add(new Student(name, id));
    }

    static void removeStudent(String studentId) {
        Student obj;
        if ((obj=getObject(studentId))!=null)
            students.remove(obj);
    }

    private static Student getObject(String id){
        for (Student obj:students){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }

    static void purge() {
        Iterator<Student> iter = students.iterator();

        while (iter.hasNext()) {
            Student str = iter.next();
            iter.remove();
        }
    }
}