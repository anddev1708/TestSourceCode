package com.qvqstudio.autopickupcall;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 1;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 2;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permission
        askPermissions();

        // Check bluetooth, earphone

        // Access to all contact

        // Purchase function
    }

    private void askPermissions() {

        int permissionCheckPhoneState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        int permissionCheckContact = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckPhoneState == PackageManager.PERMISSION_GRANTED && permissionCheckContact == PackageManager.PERMISSION_GRANTED) {

            launchCamera();

        } //asking permission for the first time
        else if (permissionCheckPhoneState != PackageManager.PERMISSION_GRANTED && permissionCheckContact != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS},
                    MULTIPLE_PERMISSIONS);

        } else {
            // Permission denied, so request permission

            // if camera request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to read phone state in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            }   // if storage request is denied
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to read contact in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                READ_CONTACT_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            }

        }
    }

    private void launchCamera(){
        Toast.makeText(this, "Lauch Camera !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_PHONE_STATE)) {
                    // check whether camera permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        launchCamera();
                    }
                }
                break;
            case READ_CONTACT_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_CONTACTS)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        launchCamera();
                    }
                }
                break;
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_PHONE_STATE) && permissions[1].equals(Manifest.permission.READ_CONTACTS)) {
                    // check whether All permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        launchCamera();

                    }
                }
                break;
            default:
                break;
        }
    }
}
