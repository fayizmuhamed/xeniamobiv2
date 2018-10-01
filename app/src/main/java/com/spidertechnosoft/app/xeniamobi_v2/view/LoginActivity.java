package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    UserService userService=new UserService();

    private EditText mEditUsername;

    private EditText mEditPassword;

    private Button mBtnLogin;

    private ImageButton mBtnSync;

   // private Database mDatabase;
   SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set window without title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set window full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //mDatabase=Database.getInstance();
        sessionManager=new SessionManager(getApplicationContext());

        configView();

    }

   public void configView(){

       mEditUsername=(EditText)findViewById(R.id.editUsername);
       mEditPassword=(EditText)findViewById(R.id.editPassword);
       mBtnLogin=(Button)findViewById(R.id.btnLogin);
       mBtnSync=(ImageButton)findViewById(R.id.btnSync);
       mBtnLogin.setOnClickListener(this);


       mEditPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_DONE) {

                   authenticate();
                   //do here your stuff f
                   return true;
               }
               return false;
           }
       });

       mBtnSync.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Context wrapper = new ContextThemeWrapper(LoginActivity.this, R.style.Custom_popup);
               PopupMenu popup = new PopupMenu(wrapper, mBtnSync, Gravity.RIGHT);

               //Creating the instance of PopupMenu
               //PopupMenu popup = new PopupMenu(LoginActivity.this, mBtnSync);
               //Inflating the Popup using xml file
               popup.getMenuInflater().inflate(R.menu.setting, popup.getMenu());

               //registering popup with OnMenuItemClickListener
               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   public boolean onMenuItemClick(MenuItem item) {
                       int id = item.getItemId();
                       switch (id) {
                           case R.id.action_reset:
                               resetApp(getApplicationContext());
                               return true;
                       }
                       return false;
                   }
               });

               popup.show();//showing popup menu

            // resetApp(getApplicationContext());
           }
       });
   }

   public void authenticate(){

       String username=mEditUsername.getText().toString();

       String password=mEditPassword.getText().toString();

       if(validate()){

           User user=userService.findByUsername(username);

           if(user==null){

               Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show();

               return;
           }

           if(!user.getPassword().equals(password)){

               Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show();

               return;
           }

           //mDatabase.setLoggedInUser(user);
           sessionManager.setLoggedInUser(user);

           Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           finish();
           startActivity(intent);

       }

   }

   public boolean validate(){

       String username=mEditUsername.getText().toString();

       String password=mEditPassword.getText().toString();

       if(username==null||username.trim().isEmpty()){

           Toast.makeText(getApplicationContext(),"Please enter username", Toast.LENGTH_SHORT).show();

           return false;
       }

       if(password==null||password.trim().isEmpty()){

           Toast.makeText(getApplicationContext(),"Please enter password", Toast.LENGTH_SHORT).show();

           return false;
       }

       return true;
   }

    @Override
    public void onClick(View v) {

        authenticate();
    }

    public void resetApp(Context context){

        //mDatabase.empty();

        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);

//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
    }
}
