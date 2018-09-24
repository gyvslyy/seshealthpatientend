package group2.seshealthpatient.Activities;

import android.icu.lang.UProperty;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.Model.Upload;
import group2.seshealthpatient.R;
import group2.seshealthpatient.UploadList;

public class ViewUploadActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userID;

    private ListView ListViewUploadfile;

    List<Upload> uploadlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload);
        mAuth = FirebaseAuth.getInstance();

        ListViewUploadfile = findViewById(R.id.UploadedListView);
        uploadlist = new ArrayList<>();

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds1 : dataSnapshot.child("Users").child(userID).child("UploadFiles").getChildren())
                {

                    Upload upload = new Upload();
                    upload.setName(ds1.getValue(Upload.class).getName());
                    upload.setType(ds1.getValue(Upload.class).getType());
                    upload.setUrl(ds1.getValue(Upload.class).getUrl());

                    uploadlist.add(upload);

                }

                UploadList adapter = new UploadList(ViewUploadActivity.this,uploadlist);
                ListViewUploadfile.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
