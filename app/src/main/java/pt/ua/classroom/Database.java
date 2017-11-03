package pt.ua.classroom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Database {

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "Database";
    private static String userid,role,name,email,classeid;
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

    public static void setClasseid(String id) {
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

    static void getTeachingClasses(final MainActivity activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot teachingClasses = dataSnapshot.child("Users").child(userid).child("teachingClasses");
                String classid;
                for(DataSnapshot d : teachingClasses.getChildren()) {
                    Classe.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(),d.getKey());
                }
                activity.teacherActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getTeachingClasses(final SelectRole activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot teachingClasses = dataSnapshot.child("Users").child(userid).child("teachingClasses");
                String classid;
                for(DataSnapshot d : teachingClasses.getChildren()) {
                    Classe.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(),d.getKey());
                }
                activity.teacherActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
