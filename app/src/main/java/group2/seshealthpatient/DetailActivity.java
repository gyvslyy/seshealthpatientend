package group2.seshealthpatient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    public void btnMeasureClick(View view){
        Intent intent=new Intent(this,HeartbeatMainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
