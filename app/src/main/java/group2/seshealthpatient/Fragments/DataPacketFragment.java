package group2.seshealthpatient.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group2.seshealthpatient.ChooseFileDialog;
import group2.seshealthpatient.Model.DataPacket;
import group2.seshealthpatient.Model.Upload;
import group2.seshealthpatient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataPacketFragment extends Fragment {

    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String Filename = "";

    private int MAX_LENGTH = 200; // MAX NUMBER = 200
    private int Rest_Length = MAX_LENGTH;
    private EditText mEdt_view;
    private TextView mTv_num;


    @BindView(R.id.edt)
    EditText DPEditField;
    @BindView(R.id.DPTitle)
    EditText DPTitle;
    @BindView(R.id.DPTime)
    TextView DPtime;
    @BindView(R.id.choosedFileTv)
    TextView ChoosedFileTv;


    public DataPacketFragment() {
        // Required empty public constructor
        
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        getActivity().setTitle("Create DataPacket");
        }



        private String getTitle(){
        String title = DPTitle.getText().toString().trim();
        return title;
        }


    @OnClick(R.id.DPCreateBtn)
    public void CreateDataPacket(){
        DataPacket dataPacket = new DataPacket();
        String text = DPEditField.getText().toString().trim();

        //获取系统时间
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("dd/MM/yyyy HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String Time = formatter.format(curDate);
        //输入文本不为空时
        if(!TextUtils.isEmpty(getTitle())){
            dataPacket.setTime(Time);
            dataPacket.setTitle(getTitle());
            dataPacket.setDataText(text);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseUsers.child("Users").child(user.getUid()).child("Datapackets").child(dataPacket.getTitle()).child("Text").setValue(dataPacket.getDataText());
            databaseUsers.child("Users").child(user.getUid()).child("Datapackets").child(dataPacket.getTitle()).child("Time").setValue(dataPacket.getTime());
            DPtime.setText(Time);
            addfiletoDP(dataPacket);

            Toast.makeText(getContext(),"You have successfully created your datapacket!",Toast.LENGTH_LONG).show();
        }
        else
        {   Toast.makeText(getContext(),"Please input a title for the packet!",Toast.LENGTH_LONG).show();
        }

    }




    //attach files
    @OnClick(R.id.AttachFilesBtn)
    public void AttachFiles() {
        ChooseFileDialog myDialog = new ChooseFileDialog(getContext(),  new ChooseFileDialog.PriorityListener() {
            @Override
            public void setActivityText(String string) {
                ChoosedFileTv.setText(string);
                Filename = string;
            }
        });
        myDialog.show();


    }


    public void addfiletoDP(final DataPacket dataPacket){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds1 : dataSnapshot.child("Users").child(user.getUid()).child("UploadFiles").getChildren())
                {
                    Upload upload = new Upload();
                    upload.setName(ds1.getValue(Upload.class).getName());
                    upload.setType(ds1.getValue(Upload.class).getType());
                    upload.setUrl(ds1.getValue(Upload.class).getUrl());
                    if(upload.getName().equals(Filename)){
                        dataPacket.setUpload(upload);
                        databaseUsers.child("Users").child(user.getUid()).child("Datapackets").child(dataPacket.getTitle()).child("File").setValue(upload);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_data_packet, container, false);

        ButterKnife.bind(this, v);


        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality
        mEdt_view = view.findViewById(R.id.edt);
        mTv_num = view.findViewById(R.id.edt_tv_num);
        mEdt_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        setListener();
    }
    private void setListener() {
        mEdt_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (Rest_Length > 0) {
                    Rest_Length = MAX_LENGTH - mEdt_view.getText().length();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTv_num.setText(Rest_Length + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTv_num.setText(Rest_Length + "/200");
            }
        });
    }

}


