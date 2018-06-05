package com.qvqstudio.testhidefile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        // Create dir if not exist
        if(getExternalStorageState() == StorageState.WRITEABLE){
            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/xyz/");
            if(!dir.exists()) dir.mkdirs();

//            File hideDir = new File(Environment.getExternalStorageDirectory().getPath() + "/.xyz/");
//            if(!hideDir.exists()) hideDir.mkdirs();
        }

        Button btnSelectFile = findViewById(R.id.btn_select_file);
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);

                hideFile();
            }
        });

    }


    private void hideFile(){
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xyz";

        File file = new File(dir);
        String dirHide = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.xyz";
        File fileHide = new File(dirHide);
        if (file.exists() && !fileHide.exists()) {
            file.renameTo(fileHide);
        } else if(!file.exists()) {
            fileHide.mkdir();
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

                    Uri pickedImage = data.getData();
                    // Let's read picked image path using content resolver
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));


                    /*String pathHolder = data.getData().getPath();
                    Toast.makeText(MainActivity.this, pathHolder, Toast.LENGTH_LONG).show();

                    File oldDir = new File(pathHolder);
                    moveFile(oldDir, newDir);*/

                    // String pathHolder = data.getData().getPath();

                }
                break;
        }
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
