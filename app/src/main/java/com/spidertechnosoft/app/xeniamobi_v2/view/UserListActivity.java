package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserListAdapter.OperationCallback{

    UserService userService=new UserService();

    private Context mContext;

    private ListView mLvUserList;

    private EditText mEditUserSearch;

    ImageButton btnAddNewUser;

    //private Database mDatabase;

    List<User> mUsers =new ArrayList<>();

    final Handler mHandler = new Handler();
    final Runnable mUpdateUserList = new Runnable() {

        @Override
        public void run() {
            updateUserList();
        }

    };


    ProgressDialog dlgProgress;

    private UserListAdapter mUserListAdapter;

    //Get rest api manager
    //RestApiManager mRestApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mRestApiManager=new RestApiManager();
        // Save the context
        this.mContext = getApplicationContext();
        //this.mDatabase= Database.getInstance();

        this.mUsers =new ArrayList<User>();
        configView();
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
        fetchUserList();
    }

    public void configView(){

        btnAddNewUser =(ImageButton) findViewById(R.id.btnAddNewUser);

        btnAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(intent);
            }
        });

        mUserListAdapter=new UserListAdapter(mContext, mUsers,this);

        mLvUserList =(ListView)findViewById(R.id.lvUserList);

        mLvUserList.setAdapter(mUserListAdapter);

        mEditUserSearch =(EditText)findViewById(R.id.txtUserSearch);

        mEditUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mUserListAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchUserList() {

        dlgProgress=new ProgressDialog(UserListActivity.this);

        dlgProgress.setMessage("Fetching users...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mUsers.clear();

                    String query=null;

                    String[] params=null;

                    mUsers.addAll(userService.findActiveUsers());


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateProductList runnable
                mHandler.post(mUpdateUserList);
            }
        };
        t.start();
    }

    /**
     * Update View With Category Items
     */
    protected  void updateUserList(){

        //mItemListAdapter.notifyDataSetChanged();

        mUserListAdapter.getFilter().filter(mEditUserSearch.getText().toString());

        dlgProgress.dismiss();

    }


    @Override
    public void onEditClick(User user) {

        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
        intent.putExtra("USER",user);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(final User user) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(UserListActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(UserListActivity.this);
        }
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        deleteUser(user);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    public void deleteUser(User user){

        ProgressDialog dialog=new ProgressDialog(UserListActivity.this);

        dialog.setMessage("Deleting user...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


        try {

            user.setActive(false);
            if(userService.save(user)>0){
                Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "User delete failed", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            fetchUserList();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "User delete failed", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {

        this.finish();

    }
}
