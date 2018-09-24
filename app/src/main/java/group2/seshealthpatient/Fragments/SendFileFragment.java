package group2.seshealthpatient.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group2.seshealthpatient.Activities.ViewData;
import group2.seshealthpatient.Activities.ViewUploadActivity;
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.Model.Upload;
import group2.seshealthpatient.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendFileFragment extends Fragment {



    @BindView(R.id.FileNameTextView)
    TextView FileName;


    Uri Uri;

    ProgressDialog progressDialog;



    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    public SendFileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = database.getReference();
        }



    @OnClick(R.id.UploadBtn)
    public void UploadFile(){
        if(Uri != null)
            upload(Uri);
        else{
            Toast.makeText(getContext(), "Please selected a file.",Toast.LENGTH_LONG).show();
        }
    }




    public void upload(final Uri uri3){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //获得storage 的 reference
        firebaseUser = firebaseAuth.getCurrentUser();
        final StorageReference storageReference = storage.getReference();
        final UploadTask uploadTask = storageReference.child("Uploads").child(getFormat(uri3)).child(getFileName(uri3)).putFile(uri3);
               uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    //成功的情况
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return storageReference.child("Uploads").child(getFormat(uri3)).child(getFileName(uri3)).getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    String name = getFileName(uri3);
                                    Upload upload = new Upload(name,getFormat(uri3),downloadUrl);


                                    //储存在realtime database中
                                    reference.child("Users").child(firebaseUser.getUid()).child("UploadFiles").child(name).setValue(upload)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                        Toast.makeText(getContext(),"File is successfully uploaded",Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(getContext(),"Upload failed",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
            public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Upload failed",Toast.LENGTH_SHORT).show();


                    }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
            progressDialog.setProgress(currentProgress);

            }
        });

    }

    @OnClick(R.id.ChoosePDFBtn)
    public void ChoosePDF(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,99);
        }
    @OnClick(R.id.ChooseDocBtn)
    public void ChooseDoc(){
        Intent intent = new Intent();
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,88);
    }
    @OnClick(R.id.ChooseImageBtn)
    public void ChoosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,77);
    }
    @OnClick(R.id.ChooseVideoBtn)
    public void Choosevideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,66);
    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==99 && resultCode == RESULT_OK && data!=null){
            Uri=data.getData();
            String file = Uri.getLastPathSegment();

            FileName.setText(getString(R.string.selected_file,file));

            }
            else if(requestCode==88 && resultCode == RESULT_OK && data!=null){
                Uri=data.getData();
                String file = Uri.getLastPathSegment();

                FileName.setText(getString(R.string.selected_file,file));
                }
            else if(requestCode==77 && resultCode == RESULT_OK && data!=null){
                Uri=data.getData();
                String file = Uri.getLastPathSegment();

                FileName.setText(getString(R.string.selected_file,file));

            }
            else if(requestCode==66 && resultCode == RESULT_OK && data!=null){
                Uri=data.getData();
                String file = Uri.getLastPathSegment();

                FileName.setText(getString(R.string.selected_file,file));

            }

        else
        Toast.makeText(getContext(),"Please select a file",Toast.LENGTH_LONG).show();
        }


   private String getFileName(Uri uri1){

          String name = uri1.getLastPathSegment();
          return name.substring(0,name.lastIndexOf("."));
   }
   private String getFormat(Uri uri2){
        String name = uri2.getLastPathSegment();
        return name.substring(name.lastIndexOf(".")+1);}


    @OnClick(R.id.ViewUploadBtn)
    public void viewUploadFile(){
        Intent intent = new Intent(getActivity(), ViewUploadActivity.class);
        startActivity(intent);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_send_file, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality
    }


}
