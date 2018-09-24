package group2.seshealthpatient.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group2.seshealthpatient.Activities.ViewData;
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.R;

/**
 * Class: PatientInformationFragment
 * Extends: {@link Fragment}
 * Author: Carlos Tirado < Carlos.TiradoCorts@uts.edu.au> and YOU!
 * Description:
 * <p>
 * This fragment's job will be that to display patients information, and be able to edit that
 * information (either edit it in this fragment or a new fragment, up to you!)
 * <p>

 */
public class PatientInformationFragment extends Fragment {


    // Note how Butter Knife also works on Fragments, but here it is a little different
    @BindView(R.id.EditViewFirstName)
    EditText EditViewFirstName;
    @BindView(R.id.EditViewLastName)
    EditText EditViewLastName;
    @BindView(R.id.EditViewAge)
    EditText EditViewAge;
    @BindView(R.id.SaveBtn)
    Button SaveBtn;
    @BindView(R.id.SpinnerGender)
    Spinner spinnerGender;
    @BindView(R.id.EditViewHeight)
    EditText EditViewHeight;
    @BindView(R.id.EditViewWeight)
    EditText EditViewWeight;
    @BindView(R.id.EditViewAdd)
    EditText EditViewAdd;
    @BindView(R.id.EditViewMC)
    EditText EditViewMC;
    @BindView(R.id.ViewInfBtn)
    Button ViewInfBtn;


    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;


    public PatientInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Instead of hardcoding the title perhaps take the user name from somewhere?
        // Note the use of getActivity() to reference the Activity holding this fragment
        getActivity().setTitle("Username Information");


        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference();}


       @OnClick(R.id.SaveBtn)
        public void saveData(){
            String firstname = EditViewFirstName.getText().toString().trim();
            String lastname = EditViewLastName.getText().toString().trim();
            String age = EditViewAge.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();
            String weight = EditViewWeight.getText().toString().trim();
            String height = EditViewHeight.getText().toString().trim();
            String add = EditViewAdd.getText().toString().trim();
            String mc = EditViewMC.getText().toString().trim();

            if(!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname)){

            Profile profile = new Profile();
            profile.setFirstname(firstname);
            profile.setLastname(lastname);
            profile.setAge(age);
            profile.setGender(gender);
            profile.setWeight(weight);
            profile.setHeight(height);
            profile.setAddress(add);
            profile.setMc(mc);


            FirebaseUser user = firebaseAuth.getCurrentUser();

            databaseUsers.child("Users").child(user.getUid()).setValue(profile);


                Toast.makeText(getContext(), "Saved the information successfully!", Toast.LENGTH_LONG).show();
        }
        else {
                Toast.makeText(getContext(),"You should enter your name!",Toast.LENGTH_LONG).show();}
       }

      @OnClick(R.id.ViewInfBtn)
      public  void viewInformation(){
      Intent intent = new Intent(getActivity(), ViewData.class);
      startActivity(intent);
      }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_information, container, false);

        // Note how we are telling butter knife to bind during the on create view method
        ButterKnife.bind(this, v);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality

    }
}
