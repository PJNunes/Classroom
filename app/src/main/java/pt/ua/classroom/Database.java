package pt.ua.classroom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

class Database{

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "Database";
    private static String userid,role,name,email,classeid,classename;
    private static Uri photoUrl;

    private static void createUser(int id){
        Map<String,Object> rm= new HashMap<>();
        rm.put("name",name);
        rm.put("e-mail",email);
        database.child("Users").child("user"+id).updateChildren(rm);
    }

    static void setId(final MainActivity classe, String displayName, String displayEmail, Uri displayPhotoUrl){
        Log.d(TAG, String.valueOf(database));
        name=displayName;
        email=displayEmail;
        photoUrl=displayPhotoUrl;
        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userid="";
                int ctr=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ctr++;
                    String temp = ds.getKey();
                    if(ds.child("e-mail").getValue(String.class).equals(email)) {
                        userid=temp;
                        Log.d(TAG, userid);
                        role=ds.child("role").getValue(String.class);
                        break;
                    }
                }
                if(userid.equals("")){
                    createUser(ctr+1);
                    userid="user"+(ctr+1);
                    role=null;
                }

                classe.nextActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    static void setRole(String role) {
        Database.role = role;
        Map<String,Object> rm= new HashMap<>();
        rm.put("role",role);
        database.child("Users").child(userid).updateChildren(rm);
    }

    static void setClasseid(String id) {
        classeid = id;
    }

    public static String getId() {
        return userid;
    }

    static String getRole() {
        return role;
    }

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    static Uri getPhoto() {
        return photoUrl;
    }

    static <T extends AppCompatActivity> void getTeachingClasses(final T activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Classe.getClasses().size()==0) {
                    DataSnapshot teachingClasses = dataSnapshot.child("Users").child(userid).child("teachingClasses");
                    for (DataSnapshot d : teachingClasses.getChildren()) {
                        Classe.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(), d.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,TeacherActivity.class));
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static <T extends AppCompatActivity> void getAttendingClasses(final T activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(StudentClasse.getClasses().size()==0) {
                    DataSnapshot attendingClasses = dataSnapshot.child("Users").child(userid).child("attendingClasses");
                    for (DataSnapshot d : attendingClasses.getChildren()) {
                        StudentClasse.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(), d.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,StudentClassesList.class));
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void addClasse(final TeacherActivity activity, final String s){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classes = dataSnapshot.child("Classes");

                int ctr= (int) classes.getChildrenCount();

                String tempClassId ="class"+(ctr+1);

                //create Classe
                Map<String,Object> map= new HashMap<>();
                map.put("name",s);
                database.child("Classes").child(tempClassId).updateChildren(map);

                map= new HashMap<>();
                map.put("teacher",userid);
                database.child("Classes").child(tempClassId).updateChildren(map);

                //add classe to teaching
                map= new HashMap<>();
                map.put(tempClassId,1);
                database.child("Users").child(userid).child("teachingClasses").updateChildren(map);

                //add classe to table
                Classe.addClasse(s,tempClassId);
                activity.recreate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static <T extends AppCompatActivity> void startClasse(final T activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classe = dataSnapshot.child("Classes").child(classeid).child("name");
                classename= (String) classe.getValue();

                activity.startActivity(new Intent(activity,TeacherActivityMenu.class));
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static String getClasseName() {
        return classename;
    }

    static void deleteAttendingClasse(StudentClassesList activity, String classId) {

        database.child("Users").child(userid).child("attendingClasses").child(classId).removeValue();
        database.child("Classes").child(classId).child("students").child(userid).removeValue();

        //remove classe from table
        StudentClasse.removeClasse(classId);
        activity.recreate();
    }
}
