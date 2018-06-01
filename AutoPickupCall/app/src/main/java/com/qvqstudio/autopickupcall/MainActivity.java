package com.qvqstudio.autopickupcall;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 1;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 2;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 3;

    private BillingClient mBillingClient;
    private static final String item_name = "com.abc.xyz";

    private Button btnBuyMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init view
        btnBuyMe = findViewById(R.id.btn_buy_me);
        btnBuyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Khi click vao button nay se thuc hien mua 1 item nao do

                /*List<String> skuList = new ArrayList<> ();
                skuList.add("premiumUpgrade");
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                mBillingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(SkuDetailsResult result) {
                                // Process the result.
                            }
                        });*/
            }
        });

        // Check permission
        askPermissions();

        // Check bluetooth, earphone
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isWiredHeadsetOn()) {
            // Play audio...
            Toast.makeText(this, "Headset is connected #", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No device is connected #", Toast.LENGTH_LONG).show();
        }

        // Access to all contact

        // Purchase function

        // Check client purchase key, if purchased no need request to server

        // else init in-app billing, and check item name is exist
        initBillingInApp();
    }

    PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
            Log.e("TAG", "On purchases updated !");

            if (responseCode == BillingClient.BillingResponse.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {

                    //handlePurchase(purchase);
                }
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                // Handle an error caused by a user canceling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };



    private void initBillingInApp(){
        mBillingClient = BillingClient.newBuilder(this).setListener(purchasesUpdatedListener).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    Log.e("TAG", "Ok co the build in app duoc !");


                    // Query, kiem tra xem item da duoc mua hay chua, neu da duoc mua roi thi update lai purchase button
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.e("TAG", "Khong the build in app duoc !");

                // Truong hop khong co internet khong the kiem tra duoc
            }
        });
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
