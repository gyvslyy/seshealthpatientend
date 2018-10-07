package group2.seshealthpatient.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.R;

public class ViewPatientDetails extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userID;

    private ListView ListViewUserInf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientdatapacketdetails);
        mAuth = FirebaseAuth.getInstance();

        ListViewUserInf = findViewById(R.id.DataListView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


    }
    @Override
    public void onStart(){
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Profile profile = new Profile();
                    profile.setFirstname(ds.child(userID).getValue(Profile.class).getFirstname());
                    profile.setLastname(ds.child(userID).getValue(Profile.class).getLastname());
                    profile.setAge(ds.child(userID).getValue(Profile.class).getAge());
                    profile.setGender(ds.child(userID).getValue(Profile.class).getGender());
                    profile.setWeight(ds.child(userID).getValue(Profile.class).getWeight());
                    profile.setHeight(ds.child(userID).getValue(Profile.class).getHeight());
                    profile.setAddress(ds.child(userID).getValue(Profile.class).getAddress());
                    profile.setMc(ds.child(userID).getValue(Profile.class).getMc());



                    ArrayList<String> array = new ArrayList<>();
                    array.add("Name: "+ profile.getFirstname() + profile.getLastname());

                    array.add("Age: "+ profile.getAge());
                    array.add("Gender: "+ profile.getGender());
                    array.add("Height(cm): "+ profile.getHeight());
                    array.add("Weight(kg): "+ profile.getWeight());
                    array.add("Medical condition: "+ profile.getMc());

                    ArrayAdapter adapter = new ArrayAdapter(ViewPatientDetails.this, android.R.layout.simple_list_item_1,array);
                    ListViewUserInf.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}











}
