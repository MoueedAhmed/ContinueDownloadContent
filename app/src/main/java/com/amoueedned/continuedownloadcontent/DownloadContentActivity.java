package com.amoueedned.continuedownloadcontent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class DownloadContentActivity extends AppCompatActivity {
    ContentIdentifier contentIdentifier = new ContentIdentifier();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_content);

    }

    public void buttonClikicked(View view) {
        downloadContent(contentIdentifier.CONTENT_IDENTIFIER);
    }

    private void downloadContent(String contentIdentifier) {
        //[Start] Downloading content from Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String dirLocationFirebase = "content/"+contentIdentifier;
        // Create a reference with an initial file path and name
        StorageReference dirReference = storageRef.child(dirLocationFirebase);

        //final File directory = getStorageDir(SuccessActivity.this, "content");
        final ProgressDialog dialog = new ProgressDialog(DownloadContentActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Downloading content, please wait.");
        dialog.show();

        dirReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            File file = null;
                            try {
                                file = new File(getFilesDir(), item.getName());
                            } catch (Exception e) {
                                Toast.makeText(DownloadContentActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }



                            item.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    Toast.makeText(MainActivity.this, "Downloading: "
//                                            +taskSnapshot.getStorage().getName(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(DownloadContentActivity.this, "Failed downloading", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DownloadContentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //[End] Downloading content from Firebase
    }

}


