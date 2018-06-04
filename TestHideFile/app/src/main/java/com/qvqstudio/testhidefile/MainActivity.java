package com.qvqstudio.testhidefile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
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


        if (selectFile.exists()){
            Log.e("TAG", "File selected is exist");
        } else {
            Log.e("TAG", "File selected is not exist #");
        }

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(selectFile);
            out = new FileOutputStream(hiddenFile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*if(selectFile.renameTo(hiddenFile)){
            Toast.makeText(this, "Hidden file is success !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Hidden file is failed !", Toast.LENGTH_LONG).show();
        }*/
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
                    Uri fileUri = data.getData();
                    String fullPath = null;
                    if (fileUri != null) {
                        fullPath = fileUri.getPath();
                    }

                    Log.e("TAG", "Show path = "+ fullPath);
                    Toast.makeText(this, "Fie path selected = "+ fullPath, Toast.LENGTH_LONG).show();

                    File f = new File(fullPath);
                    if(f.exists()){
                        Log.e("TAG", "File ton tai");
                    } else {
                        Log.e("TAG", "File khong ton tai");
                    }
                    // hideFile(fullPath);

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

    /* Test move function in android */

}
