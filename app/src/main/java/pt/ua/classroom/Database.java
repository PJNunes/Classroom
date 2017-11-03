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
    private static String id,role,name,email;
    private static Uri photoUrl;

    static void setId(final MainActivity classe, String displayName, String displayEmail, Uri displayPhotoUrl){
        Log.d(TAG, String.valueOf(database));
        name=displayName;
        email=displayEmail;
        photoUrl=displayPhotoUrl;
        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id="";
                int ctr=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ctr++;
                    String temp = ds.getKey();
                    if(ds.child("e-mail").getValue(String.class).equals(email)) {
                        id=temp;
                        Log.d(TAG, id);
                        role=ds.child("role").getValue(String.class);
                        break;
                    }
                }
                if(id.equals("")){
                    createUser(ctr+1);
                    id="user"+(ctr+1);
                    role=null;
                }

                classe.nextActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private static void createUser(int id){
        Map<String,Object> rm= new HashMap<>();
        rm.put("name",name);
        rm.put("e-mail",email);
        database.child("Users").child("user"+id).updateChildren(rm);
    }

    static void setRole(String role) {
        Database.role = role;
        Map<String,Object> rm= new HashMap<>();
        rm.put("role",role);
        database.child("Users").child(id).updateChildren(rm);
    }

    public static String getId() {
        return id;
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
                DataSnapshot teachingClasses = dataSnapshot.child("Users").child(id).child("teachingClasses");
                String classid;
                for(DataSnapshot d : teachingClasses.getChildren()) {
                    classid = d.getKey();
                    Classe.addClasse((String) dataSnapshot.child("Classes").child(classid).child("name").getValue());
                }
                activity.teacherActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getTeachingClasses(final SelectRole activity) {
        DatabaseReference classes = database.child("Users").child(id).child("teachingClasses");
        classes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren())
                    Classe.addClasse(d.getKey());
                activity.teacherActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
