package com.qvqstudio.hidefilefolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG =   "MainActivity";
    private final static String FOLDER_NAME =   "HideFileFolder";
    private File newDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check store permission
        // running background thread to scan all file in the folder
        checkPermissions();

        // Click button to browse file or folder
        Button button = findViewById(R.id.btn_picker);
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {

            try {
                // Tao folder to store media file
                newDir = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
                if (!newDir.exists())
                    newDir.mkdirs();

                // Create nomedia file
                File noMedia = new File ( newDir.getParent(), ".nomedia" );
                FileOutputStream noMediaOutStream;
                noMediaOutStream = new FileOutputStream( noMedia );
                noMediaOutStream.write(0);
                noMediaOutStream.close ( );


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            /* Check avaiable media is mounted */
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);
                }
            });
        } else {
            Toast.makeText(this, "Stogare is not avaible !", Toast.LENGTH_LONG).show();
        }

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void threadScanFileFolder(){

    }

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {

                    String pathHolder = data.getData().getPath();
                    Toast.makeText(MainActivity.this, pathHolder, Toast.LENGTH_LONG).show();

                    File oldDir = new File(pathHolder);
                    moveFile(oldDir, newDir);
                }
                break;
        }
    }

    /*
    * Function to move file from old dir to new dir
    * */
    private void moveFile(File oldDir, File newDir){
        if(oldDir.renameTo(newDir)) {
            Log.v("Moving", "Moving file successful.");
        } else {
            Log.v("Moving", "Moving file failed.");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
}
