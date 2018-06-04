package com.qvqstudio.testhidefile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private final static String HIDEEN_PATH = Environment.getExternalStorageDirectory().getPath() + "/xyz";
    private boolean isInitDirOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        // Create dir if not exist
        if(getExternalStorageState() == StorageState.WRITEABLE){
            /* Check hidden folder*/
            initDir();
        }

        Button btnSelectFile = findViewById(R.id.btn_select_file);
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);


            }
        });

    }

    private void initDir(){
        File hiddenFolder = new File(HIDEEN_PATH);
        if(hiddenFolder.exists()){
            isInitDirOk = true;
        } else {
            File dir = new File(HIDEEN_PATH);
            if(!dir.exists()){
                if(dir.mkdirs())
                    isInitDirOk = true;
            }
        }

        // Create noMedia file to hidden with gallery
        createNoMediaFile();
    }

    private void createNoMediaFile(){
        // Create .noMedia file
        File newMediaFile = new File(HIDEEN_PATH, ".nomedia");
        if(!newMediaFile.exists()) {
            try {
                newMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void hideFile(String filePath){

        File selectFile = new File(filePath);
        if(!isInitDirOk){
            Toast.makeText(this, "Init hidden dir is failed", Toast.LENGTH_LONG).show();
            return;
        }

        File hiddenFile = new File(HIDEEN_PATH, "abc.jpg");
        if(selectFile.renameTo(hiddenFile)){
            Toast.makeText(this, "Hidden file is success !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Hidden file is failed !", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {

                    String pathHolder = data.getData().getPath();
                    Toast.makeText(MainActivity.this, pathHolder, Toast.LENGTH_LONG).show();
                    hideFile(pathHolder);

                }
                break;
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private void checkPermissions() {
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
        }
    }

    public enum StorageState{
        NOT_AVAILABLE, WRITEABLE, READ_ONLY
    }

    public StorageState getExternalStorageState() {
        StorageState result = StorageState.NOT_AVAILABLE;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return StorageState.WRITEABLE;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return StorageState.READ_ONLY;
        }

        return result;
    }
}
