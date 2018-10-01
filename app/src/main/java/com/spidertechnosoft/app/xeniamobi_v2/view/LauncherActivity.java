package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterConnectionType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterModel;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrintingSize;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Setting;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.SettingConfig;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;

import java.util.Date;
import java.util.UUID;

public class LauncherActivity extends AppCompatActivity {

    Context mContext;

    ImageButton btnTapIcon;

    Animation shake;

    //private Database mDatabase;
    SessionManager sessionManager;

    UserService userService = new UserService();
    SettingService settingService=new SettingService();

    public final static int CODE_WRITE_SETTINGS_PERMISSION=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        this.mContext=getApplicationContext();
        sessionManager=new SessionManager(getApplicationContext());



        // Check the permissions
        checkPermissions();



    }

    /**
     * Method to check the permissions
     */
    protected void checkPermissions() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.requestPermissions(LauncherActivity.this,
                        new String[]{
                        Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_SETTINGS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WAKE_LOCK,
                                Manifest.permission.CHANGE_NETWORK_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        1);


            }
        });

        // Start the thread
        t.start();


    }

    /**
     * Result of the activity asking for the permission
     *
     * @param requestCode       : Specified request code
     * @param permissions       : The list of permissions
     * @param grantResults      : The grantResults
     */
    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions,
                                            int[] grantResults) {


        switch (requestCode) {

            case 1: {

                // Set the flag to false
                boolean isAllPermissionGranted = true;

                boolean isWriteSettingsGranted = true;

                // If the grantResults size is not equal to permissions size, return
                if ( grantResults == null || permissions.length != grantResults.length) {

                    // return
                    return;

                }

                // If the grantResults.length is 0, then set the flag to false
                if ( grantResults == null || grantResults.length == 0 ) {

                    isAllPermissionGranted = false;

                }


                // Iterate through the permissions and chekc the grantResults
                for (int i = 0; i < permissions.length ; i++) {

                    // check if the corresponding grantResult is allow
                    if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {


                        // Get the text
                        String text = "";

                        switch(i) {

                            // Set the text
                            case 3 :
                            case 4 :
                            case 12 :
                            case 13 :
                                isAllPermissionGranted=false;
                                text = "Please provide Bluetooth permission";
                                break;

                            case 5 :
                            case 8 :
                            case 9 :
                            case 10 :
                            case 11 :
                                isAllPermissionGranted=false;
                                text = "Please provide Internet permission";
                                break;

                            case 2 :
                            case 6 :
                                isAllPermissionGranted=false;
                                text = "Please provide Storage permission";
                                break;
                            case 1:
                                isWriteSettingsGranted=false;

                                break;


                        }
                        if(!text.isEmpty())
                            Toast.makeText(LauncherActivity.this, text, Toast.LENGTH_SHORT).show();


                    }

                }



                // Check the flag
                if ( isAllPermissionGranted )  {



                    if(!isWriteSettingsGranted){

                        settingPermission() ;
                    }else {
                        addUser("admin","admin");

                        loadSettings();

                        configView();
                    }


                } else {

                    // Kill the process
                    android.os.Process.killProcess(android.os.Process.myPid());

                    // Exit the application
                    System.exit(1);

                }

                return;
            }

        }

    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, LauncherActivity.CODE_WRITE_SETTINGS_PERMISSION);

            }else{
                addUser("admin","admin");

                loadSettings();

                configView();
            }
        }
    }

    public void configView(){

        btnTapIcon=(ImageButton)findViewById(R.id.btnTapIcon);

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //mDatabase=Database.getInstance();

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

               // if (mDatabase.getLoggedInUser() == null) {
                if (sessionManager.getLoggedInUser() == null) {

                    Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        btnTapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnTapIcon.startAnimation(shake);

            }
        });


        btnTapIcon.startAnimation(shake);
    }

    public void addUser(String username,String password){

        // Get the user
        User user = userService.findByUsername(username);

        // If the user is null, create the user
        if (user == null) {

            user = new User();
            user.setUid(UUID.randomUUID().toString());

        }
        // Set the user details
        user.setUsername(username);
        user.setName("SUPER ADMIN");
        user.setContact("+91 9995728888");
        user.setEmail("support@spidertechnosoft.com");
        user.setType(UserType.SUPER_ADMIN);
        user.setPassword(password);

        // save the user
        userService.save(user);
    }



    public void loadSettings(){

        Setting setting1 = new Setting();
        setting1.setKey(SettingConfig.COMPANY_NAME);
        setting1.setValue("Xenia Mobi");
        setting1.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting1, true);

        Setting setting2 = new Setting();
        setting2.setKey(SettingConfig.COMPANY_ADDRESS);
        setting2.setValue("1st Floor, DD Milestone Building");
        setting2.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting2, true);

        Setting setting3 = new Setting();
        setting3.setKey(SettingConfig.COMPANY_PLACE);
        setting3.setValue("Kadavantra,Kochi-20");
        setting3.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting3, true);

        Setting setting4 = new Setting();
        setting4.setKey(SettingConfig.COMPANY_CONTACT);
        setting4.setValue("Ph: +91 9995728888,+91 9745738888");
        setting4.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting4, true);

        Setting setting5 = new Setting();
        setting5.setKey(SettingConfig.COMPANY_TIN);
        setting5.setValue("xxxxxxxxxxxxxxxx");
        setting5.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting5, true);

        Setting setting6 = new Setting();
        setting6.setKey(SettingConfig.COMPANY_FOOTER);
        setting6.setValue("Thank You Visit Again");
        setting6.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting6, true);

        Setting setting7 = new Setting();
        setting7.setKey(SettingConfig.DECIMAL_POSITIONS);
        setting7.setValue("2");
        setting7.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting7, false);

        Setting setting8 = new Setting();
        setting8.setKey(SettingConfig.IS_STAFF_AUTH_REQUIRED);
        setting8.setValue(String.valueOf(IndicatorStatus.YES));
        setting8.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting8, false);

        Setting setting9 = new Setting();
        setting9.setKey(SettingConfig.TAX_TYPE);
        setting9.setValue(String.valueOf(TaxType.GST));
        setting9.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting9, false);

        Setting setting10 = new Setting();
        setting10.setKey(SettingConfig.LOCATION_TYPE);
        setting10.setValue(String.valueOf(LocationType.STATE));
        setting10.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting10, false);

        Setting setting11 = new Setting();
        setting11.setKey(SettingConfig.PRINTER_MODEL);
        setting11.setValue(String.valueOf(PrinterModel.OTHER));
        setting11.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting11, false);

        Setting setting12 = new Setting();
        setting12.setKey(SettingConfig.IS_STAFF_SELECTION_REQUIRED);
        setting12.setValue(String.valueOf(IndicatorStatus.YES));
        setting12.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting12, false);

        Setting setting13 = new Setting();
        setting13.setKey(SettingConfig.PRINTER_CONNECTION_TYPE);
        setting13.setValue(String.valueOf(PrinterConnectionType.BLUETOOTH_PRINTER));
        setting13.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting13, false);

        Setting setting14 = new Setting();
        setting14.setKey(SettingConfig.PRINTER_CONNECTION_INFO);
        setting14.setValue("");
        setting14.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting14, false);

        Setting setting15 = new Setting();
        setting15.setKey(SettingConfig.PRINTING_SIZE);
        setting15.setValue(String.valueOf(PrintingSize.FIFTY_EIGHT));
        setting15.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting15, false);

        Setting setting16 = new Setting();
        setting16.setKey(SettingConfig.NO_OF_PRINT);
        setting16.setValue("1");
        setting16.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting16, false);


    }
}
