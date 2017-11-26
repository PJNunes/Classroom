package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class StudentPool implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title, id;
    private static ArrayList<StudentPool> pools = new ArrayList<>();

    private StudentPool(String title, String id) {
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

    static ArrayList<StudentPool> getPools() {
        return pools;
    }

    static void addPool(String name,String id) {
        pools.add(new StudentPool(name, id));
    }

    static void removePool(String poolId) {
        StudentPool obj;
        if ((obj=getObject(poolId))!=null)
            pools.remove(obj);
    }

    private static StudentPool getObject(String id){
        for (StudentPool obj:pools){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }

    static void purge() {
        Iterator<StudentPool> iter = pools.iterator();

        while (iter.hasNext()) {
            StudentPool str = iter.next();
            iter.remove();
        }
    }
}