package pt.ua.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

class TeacherPool implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title, id;
    private static ArrayList<TeacherPool> pools = new ArrayList<>();

    private TeacherPool(String title, String id) {
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

    static ArrayList<TeacherPool> getPools() {
        return pools;
    }

    static void addPool(String name, String id) {
        pools.add(new TeacherPool(name, id));
    }

    static void removePool(String poolId) {
        TeacherPool obj;
        if ((obj=getObject(poolId))!=null)
            pools.remove(obj);
    }

    private static TeacherPool getObject(String id){
        for (TeacherPool obj:pools){
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }

    static void purge() {
        Iterator<TeacherPool> iter = pools.iterator();

        while (iter.hasNext()) {
            TeacherPool str = iter.next();
            iter.remove();
        }
    }
}