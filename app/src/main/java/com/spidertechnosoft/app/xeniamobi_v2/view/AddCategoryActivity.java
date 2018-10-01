package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.CategoryNameComparator;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.CategoryService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.CategoryListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;


public class AddCategoryActivity extends AppCompatActivity implements CategoryListAdapter.OperationCallback {

    CategoryService categoryService=new CategoryService();

    ProductService productService=new ProductService();

    private Context mContext;

    private ListView mLvCategoryList;

//    private Database mDatabase;

    ArrayList<Category> mCategories;

    private CategoryListAdapter mCategoryListAdapter;

    EditText txtCategoryName;

    Button btnCategorySave,btnCategoryCancel;


    final Handler mHandler = new Handler();
    final Runnable mUpdateCategoryList = new Runnable() {

        @Override
        public void run() {
            updateCategoryList();
        }

    };

    ProgressDialog dlgProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Save the context
        this.mContext = getApplicationContext();
      //  this.mDatabase= Database.getInstance();



        this.mCategories=new ArrayList<Category>();
        configView();
        fetchCategoryList();
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
    public void onBackPressed() {

       this.finish();

    }


    public void configView(){

        txtCategoryName=(EditText) findViewById(R.id.txtCategoryName);

        btnCategorySave=(Button)findViewById(R.id.btnCategorySave);

        btnCategoryCancel=(Button)findViewById(R.id.btnCategoryCancel);

        btnCategoryCancel.setVisibility(View.GONE);
        btnCategorySave.setText("SAVE");

        btnCategorySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCategory();

            }
        });




        mCategoryListAdapter=new CategoryListAdapter(mContext,mCategories,this);

        mLvCategoryList=(ListView)findViewById(R.id.lvCategoryList);

        mLvCategoryList.setAdapter(mCategoryListAdapter);

        btnCategoryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearSaveCategory();
            }
        });

       /* mEditItemSearch=(EditText)findViewById(R.id.txtItemSearch);

        mEditItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mCategoryListAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchCategoryList() {

        dlgProgress=new ProgressDialog(AddCategoryActivity.this);

        dlgProgress.setMessage("Fetching categories...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mCategories.clear();

                    mCategories.addAll(categoryService.findActiveCategories());

                    Collections.sort(mCategories, new CategoryNameComparator());


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateCategoryList runnable
                mHandler.post(mUpdateCategoryList);
            }
        };
        t.start();
    }

    /**
     * Update View With Category Items
     */
    protected  void updateCategoryList(){

        mCategoryListAdapter.notifyDataSetChanged();

        mCategoryListAdapter.getFilter().filter("");



        dlgProgress.dismiss();

    }

    public void saveCategory(){

        if(validateCategory()) {

            ProgressDialog dialog=new ProgressDialog(AddCategoryActivity.this);

            dialog.setMessage("Saving category...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.show();


            String categoryUid=(txtCategoryName.getTag()==null||txtCategoryName.getTag().toString().isEmpty())? UUID.randomUUID().toString():txtCategoryName.getTag().toString();

            Category category=new Category();
            category.setUid(categoryUid);
            category.setName(txtCategoryName.getText().toString());

            try {

                if(categoryService.save(category)>0){
                    Toast.makeText(getApplicationContext(), "Category saved successfully", Toast.LENGTH_SHORT).show();
                    clearSaveCategory();
                    fetchCategoryList();
                }else{
                    Toast.makeText(getApplicationContext(), "Category save failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Category save failed", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }



        }

    }

    public void clearSaveCategory(){

        txtCategoryName.setText("");
        txtCategoryName.setTag(null);
        btnCategorySave.setText("SAVE");
        btnCategoryCancel.setVisibility(View.GONE);

    }

    public boolean validateCategory(){

        String categoryName=txtCategoryName.getText().toString();

        if(categoryName==null||categoryName.trim().isEmpty()){

            txtCategoryName.setError("Please Enter Category Name");
            return false;
        }

        return true;
    }



    @Override
    public void onEditClick(Category category) {

        txtCategoryName.setTag(category.getUid().toString());
        txtCategoryName.setText(category.getName());
        btnCategoryCancel.setVisibility(View.VISIBLE);
        btnCategorySave.setText("UPDATE");

    }

    @Override
    public void onDeleteClick(final Category category) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AddCategoryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(AddCategoryActivity.this);
        }
        builder.setTitle("Delete Category")
                .setMessage("Are you sure you want to delete ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        deleteCategory(category);
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


    public void deleteCategory(Category category){

        ProgressDialog dialog=new ProgressDialog(AddCategoryActivity.this);

        dialog.setMessage("Deleting category...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


        try {

            if(productService.isDeleteEnabled(category.getUid())) {
                category.setActive(IndicatorStatus.NO);

                if (categoryService.save(category) > 0) {
                    Toast.makeText(getApplicationContext(), "Category deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Category delete failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                fetchCategoryList();
            }else{
                Toast.makeText(getApplicationContext(), "Category contains items", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Category delete failed", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }
}
