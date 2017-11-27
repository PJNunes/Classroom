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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class Database{

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "Database";
    private static String userid,role,name,email,classeid,classename,poolQuestion,poolType,poolId;
    private static Uri photoUrl;
    private static boolean pool_recreate=false;

    static String getClasseName() {
        return classename;
    }

    static String getPoolQuestion() {
        return poolQuestion;
    }

    static String getPoolType() {
        return poolType;
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
                if(TeachingClass.getaClasses().size()==0) {
                    DataSnapshot teachingClasses = dataSnapshot.child("Users").child(userid).child("teachingClasses");
                    for (DataSnapshot d : teachingClasses.getChildren()) {
                        TeachingClass.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(), d.getKey());
                    }
                }
                Intent i = new Intent(activity, TeacherActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static <T extends AppCompatActivity> void getAttendingClasses(final T activity) {
        AttendingClass.purge();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(AttendingClass.getClasses().size()==0) {
                    DataSnapshot attendingClasses = dataSnapshot.child("Users").child(userid).child("attendingClasses");
                    for (DataSnapshot d : attendingClasses.getChildren()) {
                        AttendingClass.addClasse((String) dataSnapshot.child("Classes").child(d.getKey()).child("name").getValue(), d.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,StudentClassesListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getStudents(final TeacherMenuActivity activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Student.getStudents().size()==0) {
                    DataSnapshot students = dataSnapshot.child("Classes").child(classeid).child("students");
                    for (DataSnapshot s : students.getChildren()) {
                        Student.addStudent((String) dataSnapshot.child("Users").child(s.getKey()).child("name").getValue(), s.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,StudentListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static String getClasseID() {
        return classeid;
    }

    static void getAbsentees(final TeacherMenuActivity activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                if(AbsenteeStudent.getStudents().size()==0) {
                    DataSnapshot students = dataSnapshot.child("Classes").child(classeid).child("students");
                    for (DataSnapshot s : students.getChildren()) {
                        if(dataSnapshot.child("Users").child(s.getKey()).child("attendingClasses").child(classeid).child(currentDate).getValue()==null)
                            AbsenteeStudent.addStudent((String) dataSnapshot.child("Users").child(s.getKey()).child("name").getValue(), s.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,AbsenteeStudentsListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getPools(final TeacherMenuActivity activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(TeacherPool.getPools().size()==0) {
                    DataSnapshot pools = dataSnapshot.child("Classes").child(classeid).child("pools");
                    for (DataSnapshot s : pools.getChildren()) {
                        TeacherPool.addPool((String)s.child("question").getValue(), s.getKey());
                    }
                }
                activity.startActivity(new Intent(activity,TeacherPoolListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static boolean getPoolRecreate() {
        return pool_recreate;
    }

    static void getStudentPools(final StudentActivity activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(StudentPool.getPools().size()==0) {
                    DataSnapshot classes = dataSnapshot.child("Users").child(userid).child("attendingClasses");
                    for (DataSnapshot classe : classes.getChildren()) {

                        String className= (String) dataSnapshot.child("Classes").child(classe.getKey()).child("name").getValue();
                        String clId=classe.getKey();

                        DataSnapshot pools = dataSnapshot.child("Classes").child(classe.getKey()).child("pools");

                        for (DataSnapshot s : pools.getChildren()) {
                            String question = (String) s.child("question").getValue();
                            String id= clId+"_"+s.getKey();

                            if (checkAnswered(id,dataSnapshot.child("Users").child(userid).child("answered")))
                                StudentPool.addPool(className+" : "+question,id);
                        }
                    }
                }
                activity.startActivity(new Intent(activity,StudentPoolListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getToAnswerPool(final StudentPoolListActivity activity, final String poolid) {
        Choices.purge();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Choices.getChoices().size()==0) {
                    String[] parts = poolid.split("_");
                    classeid = parts[0];
                    poolId = parts[1];
                    DataSnapshot pools = dataSnapshot.child("Classes").child(classeid).child("pools").child(poolId);
                    poolQuestion= (String) pools.child("question").getValue();
                    poolType= (String) pools.child("type").getValue();
                    if(!poolType.equals("open_text"))
                        for (DataSnapshot s : pools.child("answers").getChildren()) {
                            Choices.addChoice(s.getKey());
                        }
                }
                activity.startActivity(new Intent(activity,AnswerPoolActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void getAnsweredPool(final TeacherPoolListActivity activity, final String poolid) {
        Choices.purge();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Choices.getChoices().size()==0) {
                    poolId=poolid;
                    DataSnapshot pools = dataSnapshot.child("Classes").child(classeid).child("pools").child(poolId);
                    poolQuestion= (String) pools.child("question").getValue();
                    poolType= (String) pools.child("type").getValue();
                    if(poolType.equals("open_text"))
                        for (DataSnapshot s : pools.child("answers").getChildren()) {
                            Choices.addChoice((String) s.getValue());
                        }
                    else
                        for (DataSnapshot s : pools.child("answers").getChildren()) {
                            Choices.addChoice(s.getKey()+" : "+s.getValue());
                        }
                }
                activity.startActivity(new Intent(activity,TeacherAnsweredActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void setPoolRecreate(boolean bool) {
        pool_recreate = bool;
    }

    static void setId(final MainActivity classe, String displayName, String displayEmail, final Uri displayPhotoUrl){
        Log.d(TAG, String.valueOf(database));
        name=displayName;
        email=displayEmail;
        photoUrl=displayPhotoUrl;
        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userid="";
                String temp="user0";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    temp = ds.getKey();
                    if(ds.child("e-mail").getValue(String.class).equals(email)) {
                        userid=temp;
                        Log.d(TAG, userid);
                        role=ds.child("role").getValue(String.class);
                        break;
                    }
                }
                if(userid.equals("")){
                    int number=Integer.parseInt(temp.substring(4));
                    createUser(number+1, displayPhotoUrl);
                    userid="user"+(number+1);
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
        Student.purge();
    }

    static void addClasse(final TeacherActivity activity, final String s){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classes = dataSnapshot.child("Classes");

                String temp="class0";
                for(DataSnapshot ds : classes.getChildren()) {
                    temp = ds.getKey();
                }

                int number=Integer.parseInt(temp.substring(5));
                String tempClassId ="class"+(number +1);

                //create TeachingClass
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
                TeachingClass.addClasse(s,tempClassId);
                activity.recreate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void addStudent(final StudentListActivity activity, final String name) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classes = dataSnapshot.child("Users");
                String id;
                String temp="user0";
                for(DataSnapshot ds : classes.getChildren()) {
                    temp = ds.getKey();
                }

                int number=Integer.parseInt(temp.substring(4));
                id="user"+(number +1);
                String tempClassId =id;

                //create User
                Map<String,Object> map= new HashMap<>();
                map.put("name",name);
                database.child("Users").child(id).updateChildren(map);

                //add user to class
                map= new HashMap<>();
                map.put(id,1);
                database.child("Classes").child(classeid).child("students").updateChildren(map);

                //add student to table
                Student.addStudent(name,id);
                activity.recreate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void addPool(final CreatePoolActivity activity, final String question, final String type) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classes = dataSnapshot.child("Classes").child(classeid).child("pools");
                String id;
                String temp="pool0";
                for(DataSnapshot ds : classes.getChildren()) {
                    temp = ds.getKey();
                }

                int number=Integer.parseInt(temp.substring(4));
                id="pool"+(number +1);
                String tempClassId =id;

                //create TeacherPool
                Map<String,Object> rm= new HashMap<>();
                rm.put("question",question);
                rm.put("type",type);

                database.child("Classes").child(classeid).child("pools").child(id).updateChildren(rm);

                if (!type.equals("open")){
                    rm= new HashMap<>();

                    ArrayList<Choices> choices = Choices.getChoices();
                    for (Choices choice:choices)
                        rm.put(choice.toString(),0);

                    database.child("Classes").child(classeid).child("pools").child(id).child("answers").updateChildren(rm);
                }

                Choices.purge();
                TeacherPool.addPool(question,id);
                pool_recreate=true;
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private static void createUser(int id, Uri displayPhotoUrl){
        Map<String,Object> rm= new HashMap<>();
        rm.put("name",name);
        rm.put("e-mail",email);
        rm.put("photo",displayPhotoUrl.toString());
        database.child("Users").child("user"+id).updateChildren(rm);
    }

    static <T extends AppCompatActivity> void startClasse(final T activity) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot classe = dataSnapshot.child("Classes").child(classeid).child("name");
                classename= (String) classe.getValue();

                activity.startActivity(new Intent(activity,TeacherMenuActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void deleteClasse(TeacherMenuActivity activity) {
        database.child("Users").child(userid).child("teachingClasses").child(classeid).removeValue();
        database.child("Classes").child(classeid).removeValue();

        //remove classe from table
        TeachingClass.removeClasse(classeid);
        AttendingClass.removeClasse(classeid);

        activity.startActivity(new Intent(activity,TeacherActivity.class));
        activity.finish();

        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    deleteAttendingClasse(ds.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private static void deleteAttendingClasse(String id) {
        database.child("Users").child(id).child("attendingClasses").child(classeid).removeValue();
    }

    static void removeStudent(StudentListActivity activity, String studentId) {
        database.child("Users").child(studentId).child("attendingClasses").child(classeid).removeValue();
        database.child("Classes").child(classeid).child("students").child(studentId).removeValue();
        Student.removeStudent(studentId);
        activity.recreate();
    }

    static void markPresence(String classId) {
        Map<String,Object> rm= new HashMap<>();
        rm.put(userid,1);
        database.child("Classes").child(classId).child("students").updateChildren(rm);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        rm= new HashMap<>();
        rm.put(currentDate,1);
        database.child("Users").child(userid).child("attendingClasses").child(classId).updateChildren(rm);
    }

    private static boolean checkAnswered(String id, DataSnapshot children){
        for (DataSnapshot s : children.getChildren()) {
            if (id.equals(s.getKey()))
                return false;
        }
        return true;
    }

    static void addAnswer(final AnswerPoolActivity activity, final String answer, final ArrayList<String> choices) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot pool = dataSnapshot.child("Classes").child(classeid).child("pools").child(poolId);
                switch (poolType) {
                    case "open_text": {
                        String op = "answer0";
                        for (DataSnapshot ds : pool.child("answers").getChildren()) {
                            op = ds.getKey();
                        }

                        int number = Integer.parseInt(op.substring(6));
                        String id = "answer" + (number + 1);


                        //add answer
                        Map<String, Object> rm = new HashMap<>();
                        rm.put(id, answer);

                        database.child("Classes").child(classeid).child("pools").child(poolId).child("answers").updateChildren(rm);
                        break;
                    }
                    case "single": {
                        long value = (long) pool.child("answers").child(answer).getValue();

                        //add answer
                        Map<String, Object> rm = new HashMap<>();
                        rm.put(answer, value + 1);

                        database.child("Classes").child(classeid).child("pools").child(poolId).child("answers").updateChildren(rm);
                        break;
                    }
                    case "multiple":
                        for (String answer : choices) {
                            long value = (long) pool.child("answers").child(answer).getValue();
                            //add answer
                            Map<String, Object> rm = new HashMap<>();
                            rm.put(answer, value + 1);

                            database.child("Classes").child(classeid).child("pools").child(poolId).child("answers").updateChildren(rm);
                        }
                        break;
                }

                //add answered
                Map<String,Object> rm= new HashMap<>();
                rm.put(classeid+"_"+poolId,1);

                database.child("Users").child(userid).child("answered").updateChildren(rm);

                Choices.purge();
                StudentPool.removePool(classeid+"_"+poolId);
                pool_recreate=true;
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
