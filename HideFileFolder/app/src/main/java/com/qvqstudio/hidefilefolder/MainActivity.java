package com.qvqstudio.hidefilefolder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

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

        // hide file
        Button btnHideFile = findViewById(R.id.btn_hide_file);
        btnHideFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String FILENAME = "hello_file";
                String string = "hello world!";

                File  path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME,  FILENAME );

                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(string.getBytes());
                    fos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // Click button to browse file or folder
        Button button = findViewById(R.id.btn_picker);
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {

            // Tao folder to store media file
            File  path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),  FOLDER_NAME );
            path.mkdirs();

            path.setExecutable(true);

            // createNoMedia();

           /* String NOMEDIA_FILE = ".nomedia";
            File file= new File(path,NOMEDIA_FILE);
            if (!file.exists())
            {
                try
                {
                    file.createNewFile();
                    Log.e("NOMEDIA_FILE"," Da tao file .nomedia file !");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }*/




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

    private void hideMyFile(String oldPath,String new_name)
    {
        try
        {
            File sdcard = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME + "/");
            File from = new File(oldPath);//abc.png
            File to = new File(sdcard,new_name);//abc


            Log.e("TAG", "Canon path = "+ from.getCanonicalPath());

            String newFilePath = from.getAbsolutePath().replace(from.getName(), "") + "xinchao.jpg";
            File newFile = new File(newFilePath);

            try {
                FileUtils.copyFile(from, newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }


           /* Log.e("TAG", "Rename from file path = "+ from.getAbsolutePath());
            Log.e("TAG", "Rename to file path = "+ to.getAbsolutePath());
            if(from.renameTo(to)){
                Log.e("TAG", "Da chuyen cho thanh cong !");
            } else {
                Log.e("TAG", "Chuyen cho that bai cmnr # !");
            }*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void createNoMedia(String oldPath) {
        final File nomedia = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME, ".nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
                Log.e("TAG", "Da tao file .nomedia thanh cong !");
            } catch (Exception e) {
                Log.d("TAG", "could not create nomedia file "+ e.getMessage());
            }
        } else {
            Log.e("TAG", "File da ton tai");
        }

        // Move file
        hideMyFile(oldPath, "hinhanh.jpg");

    }

    public static String getConversationsFileDirectory() {
        return  Environment.getExternalStorageDirectory().getAbsolutePath()+"/YourFolder/";
    }

    void copyFile_cat(){
        try {
            Process su;

            su = Runtime.getRuntime().exec("su");

            String cmd = "cat /mnt/sdcard/test.dat > /mnt/sdcard/test2.dat \n"+ "exit\n";
            su.getOutputStream().write(cmd.getBytes());

            if ((su.waitFor() != 0)) {
                throw new SecurityException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //throw new SecurityException();
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

                    /*String pathHolder = data.getData().getPath();
                    Toast.makeText(MainActivity.this, pathHolder, Toast.LENGTH_LONG).show();

                    File oldDir = new File(pathHolder);
                    moveFile(oldDir, newDir);*/

                    String pathHolder = data.getData().getPath();
                    createNoMedia(pathHolder);
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
