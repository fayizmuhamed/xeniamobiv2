package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.CategoryService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.UserTypeSpinnerAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class UserActivity extends AppCompatActivity {

    UserService userService=new UserService();

    CategoryService categoryService=new CategoryService();

    private Context mContext;

    //private Database mDatabase;

    UserTypeSpinnerAdapter mUserTypeSpinnerAdapter;

    Spinner spnUserType;

    ArrayList<UserType> mUserTypes;

    EditText txtUserName,txtUserEmail,txtUserContactNo,txtUserUsername,txtUserPassword;

    CheckBox chkItemSaleInc;

    Button btnItemSave;

    final Handler mHandler = new Handler();

    User mUser=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Save the context
        this.mContext = getApplicationContext();
        //this.mDatabase= Database.getInstance();
        this.mUserTypes=new ArrayList<UserType>();

        this.mUserTypes.add(UserType.ADMIN);
        this.mUserTypes.add(UserType.USER);
        this.mUserTypes.add(UserType.STAFF);
        configView();

        mUser=(User) getIntent().getSerializableExtra("USER");

        if(mUser!=null){
            initializeDataForEdit(mUser);
        }

    }

    public void configView(){

        mUserTypeSpinnerAdapter = new UserTypeSpinnerAdapter(this, R.layout.spinner_item, mUserTypes);
        mUserTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnUserType =(Spinner) findViewById(R.id.spnUserType);
        spnUserType.setAdapter(mUserTypeSpinnerAdapter);

        txtUserName=(EditText)findViewById(R.id.txtUserName);

        txtUserEmail=(EditText)findViewById(R.id.txtUserEmail);

        txtUserContactNo=(EditText)findViewById(R.id.txtUserContactNo);

        txtUserUsername=(EditText)findViewById(R.id.txtUserUsername);

        txtUserPassword=(EditText)findViewById(R.id.txtUserPassword);

        btnItemSave=(Button)findViewById(R.id.btnItemSave);


        btnItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUser();
            }
        });

    }

    public void initializeDataForEdit(User user){

        selectSpinnerItemByValue(spnUserType,user.getType());
        txtUserName.setText(user.getName()==null?"":user.getName());
        txtUserName.setTag(user.getUid());
        txtUserEmail.setText(user.getEmail()==null?"":user.getEmail());
        txtUserContactNo.setText(user.getContact()==null?"":user.getContact().toString());
        txtUserUsername.setText(user.getUsername()==null?"":user.getUsername());
        txtUserPassword.setText(user.getPassword()==null?"":user.getPassword());
        btnItemSave.setText("UPDATE");
    }

    /**
     * Method to select the sector item by value
     * @param spnr                  : Spinner object
     */
    public static void selectSpinnerItemByValue(Spinner spnr, UserType userType) {

        if(userType==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        UserTypeSpinnerAdapter adapter = (UserTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            UserType userType1=(UserType) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(userType1.name().equals(userType.name())) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public void saveUser(){

        if(validateItem()) {

            ProgressDialog dialog=new ProgressDialog(UserActivity.this);

            dialog.setMessage("Saving user...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.show();


            String userUid=(txtUserName.getTag()==null||txtUserName.getTag().toString().isEmpty())? UUID.randomUUID().toString():txtUserName.getTag().toString();

            UserType userType=(UserType) spnUserType.getSelectedItem();

            User user=new User();
            user.setUid(userUid);
            user.setName(txtUserName.getText().toString());
            user.setEmail(txtUserEmail.getText().toString());
            user.setContact(txtUserContactNo.getText().toString());
            user.setUsername(txtUserUsername.getText().toString());
            user.setType(userType);
            user.setPassword(txtUserPassword.getText().toString());


            try {

                if(userService.save(user)>0){
                    Toast.makeText(getApplicationContext(), "User saved successfully", Toast.LENGTH_SHORT).show();
                    clearSaveProduct();
                }else{
                    Toast.makeText(getApplicationContext(), "User save failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "User save failed", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            dialog.dismiss();


        }
    }

    public void clearSaveProduct(){

        spnUserType.setSelection(0);
        txtUserName.setText("");
        txtUserEmail.setText("");
        txtUserContactNo.setText("");
        txtUserUsername.setText("");
        txtUserPassword.setText("");
        btnItemSave.setText("SAVE");

    }

    public boolean validateItem(){

        boolean isValid=true;

        if(spnUserType.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(), "Please select user type", Toast.LENGTH_SHORT).show();

            isValid=false;
        }

        if(txtUserName.getText().toString()==null||txtUserName.getText().toString().trim().isEmpty()){

            txtUserName.setError("Please enter name");
            isValid=false;
        }

        if(txtUserUsername.getText().toString()==null||txtUserUsername.getText().toString().trim().isEmpty()){

            txtUserUsername.setError("Please enter user name");
            isValid=false;
        }

        if(txtUserPassword.getText().toString()==null||txtUserPassword.getText().toString().trim().isEmpty()){

            txtUserPassword.setError("Please enter password");
            isValid=false;
        }

        return isValid;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        this.finish();

    }

}
