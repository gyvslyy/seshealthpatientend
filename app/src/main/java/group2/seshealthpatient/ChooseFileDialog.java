package group2.seshealthpatient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group2.seshealthpatient.Model.Upload;

public class ChooseFileDialog extends Dialog{

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    private SimpleAdapter adapter;

    private TextView ChooseFileTextView;
    private ListView FileList;
    private Button submitBtn;

    String fileChoosed = "";
    String dateChoosed = "";

    private String string;

    private Context mcontext;


    public interface PriorityListener{
        void setActivityText(String str);
    }

    private PriorityListener listener;




    public ChooseFileDialog(Context context) {
        super(context);
        this.mcontext = context;
    }

    public ChooseFileDialog(Context context, PriorityListener listener){
        super(context);
        this.mcontext = context;
        this.listener = listener;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.choosefile_dialog, null);
        setContentView(view);

        ChooseFileTextView = (TextView)findViewById(R.id.FileChoosedTv);
        FileList = (ListView)findViewById(R.id.FileListView);
        submitBtn = (Button)findViewById(R.id.submitBtn);


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = database.getReference();
        firebaseUser = firebaseAuth.getCurrentUser();



    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Data(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            }
    });
    }




    private void Data(DataSnapshot dataSnapshot){
      final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
      for(DataSnapshot ds :dataSnapshot.child("Users").child(firebaseUser.getUid()).child("UploadFiles").getChildren()){

          String name = ds.getValue(Upload.class).getName();
          Log.v("E_VALUE","Name: "+name);
          Map map = new HashMap();
          map.put("Name",name);
          data.add(map); }



        adapter = new SimpleAdapter(getContext(),data,R.layout.filechoselist_layout,new String[]{"Name"},new int[]{R.id.ChooseFileTextView});
        FileList.setAdapter(adapter);

        FileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               fileChoosed = data.get(i).get("Name").toString();

               ChooseFileTextView.setText("Name: " + fileChoosed);

           submitBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listener.setActivityText(fileChoosed);
                   dismiss();


               }

           });
           }

    });




}
}
