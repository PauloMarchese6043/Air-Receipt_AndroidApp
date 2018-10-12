package airreceipt.com.air_reeipt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class TestFirebase  extends AppCompatActivity{

    Button selectFile, upload, fetch;
    TextView notification;
    Uri pdfUri;
    ProgressDialog progressDialog;

    FirebaseStorage storage;//used for storing files

    FirebaseDatabase database;//used to store URL:s of uploaded files


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_firebase);

        fetch.findViewById(R.id.fetchFile);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TestFirebase.this, MainActivity.class));
            }
        });
        storage = FirebaseStorage.getInstance();//return an object of Firebase Storage
        database = FirebaseDatabase.getInstance();//return an object of Firebase Database

        selectFile=findViewById(R.id.selectFile);
        upload=findViewById(R.id.upload);
        notification=findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(TestFirebase.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(TestFirebase.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pdfUri!=null)//user has successfully selected the file
                    uploadFile(pdfUri);
                else
                    Toast.makeText(TestFirebase.this,"Please select a file",Toast.LENGTH_LONG).show();

            }

        });

    }

    private void uploadFile(Uri pdfUri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();//returns root path

        storageReference.child("Uploads").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url=taskSnapshot.getDownloadUrl().toString();//returns the url of your uploaded file
                //store the url in real time database
                DatabaseReference reference=database.getReference();//return the path to the root

                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                            Toast.makeText(TestFirebase.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(TestFirebase.this,"File NOT successfully uploaded",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(TestFirebase.this,"File is not successfully uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //track the progress of the upload
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
            Toast.makeText(TestFirebase.this, "Please provide permission..",Toast.LENGTH_LONG).show();

    }

    private void selectPdf(){

        //offer user to select a file using file manager
        //we will use an Intent

        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//fetch files
        startActivityForResult(intent, 86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //check if user has selected a file or not

        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();//return uri of the selected file
            notification.setText("A file is selected: " +data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(TestFirebase.this,"Please select a file", Toast.LENGTH_LONG).show();
        }
    }
}

