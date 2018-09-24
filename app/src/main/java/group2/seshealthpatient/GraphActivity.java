package group2.seshealthpatient;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import group2.seshealthpatient.HeartbeatActivity;
import group2.seshealthpatient.R;

public class GraphActivity extends AppCompatActivity {

    public void btnReturnClick(View view){
        Intent intent=new Intent(this,HeartbeatActivity.class);
        startActivity(intent);
    }
    float value1 = 0;
    float value2 = 0;
    float value3 = 0;
    float value4 = 0;
    float value5 = 0;
    float value6 = 0;
    float value7 = 0;
    private String userID;

    //add Firebase Database stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    //private  String userID;

    private static final String TAG = "GraphActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Users");
        FirebaseUser user = mAuth.getCurrentUser(); //add sth in database unless login
        userID = user.getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                 // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                  toastMessage("Successfully signed out.");

                }
            }
        };
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //boolean a = dataSnapshot.hasChild();
                boolean b = dataSnapshot.hasChildren();

                getData(dataSnapshot);
                showData();
          }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void showData(){
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        //BarData data = new BarData(set);
        //barChart.setData(data);
        BarData data = new BarData(setData());
        barChart.setData(data);

        barChart.animateY(2300);
        barChart.getLegend().setForm(Legend.LegendForm.CIRCLE);

        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }

    private BarDataSet setData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, 88f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 61f));
        entries.add(new BarEntry(3f, 70f));
        entries.add(new BarEntry(4f, 72f));
        entries.add(new BarEntry(5f, 66f));
        entries.add(new BarEntry(6f, 89f));

        BarDataSet set = new BarDataSet(entries, "Day");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextColor(Color.rgb(155,155,155));

        return set;
    }

    private void getData(DataSnapshot dataSnapshot) {
        int i = 1;
        for (DataSnapshot ds : dataSnapshot.child(userID).child("Heartbeat").getChildren()) {

            String value = ds.getValue(String.class);

            if (i == 1) {
                value1 = Float.parseFloat(value);
                i++;
                continue;
            }

            if (i == 2) {
                value2 = Float.parseFloat(value);
                i++;
                 continue;

            }
            if (i == 3) {
                value3 = Float.parseFloat(value);
                i++;
                continue;
            }
            if (i == 4) {
                value4 = Float.parseFloat(value);
                i++;
                continue;
            }
            if (i == 5) {
                value5 = Float.parseFloat(value);
                i++;
                continue;
            }
            if (i == 6) {
                value6 = Float.parseFloat(value);
                i++;
                continue;
            }
            if (i == 7) {
                value7 = Float.parseFloat(value);
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
