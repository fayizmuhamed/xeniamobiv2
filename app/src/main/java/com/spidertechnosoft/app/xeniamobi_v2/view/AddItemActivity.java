package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.CategoryService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.CategorySpinnerAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    ProductService productService=new ProductService();

    CategoryService categoryService=new CategoryService();

    private Context mContext;

    //private Database mDatabase;

    CategorySpinnerAdapter mCategorySpinnerAdapter;

    Spinner spnCategories;

    ArrayList<Category> mCategories;

    ImageButton btnAddNewCategory;

    EditText txtItemCode,txtItemName,txtItemPrice,txtItemUnit,txtItemDescription,txtItemOutPutTax;

    CheckBox chkItemSaleInc;

    Button btnItemSave;

    final Handler mHandler = new Handler();
    final Runnable mUpdateCategoryList = new Runnable() {

        @Override
        public void run() {
            updateCategoryList();
        }

    };

    Product mProduct=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Save the context
        this.mContext = getApplicationContext();
        //this.mDatabase= Database.getInstance();
        this.mCategories=new ArrayList<Category>();
        configView();

        mProduct=(Product)getIntent().getSerializableExtra("ITEM");

        //fetchCategoryList();


    }

    public void configView(){

        mCategorySpinnerAdapter = new CategorySpinnerAdapter(this, R.layout.spinner_item, mCategories);
        mCategorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnCategories =(Spinner) findViewById(R.id.spnCategories);
        spnCategories.setAdapter(mCategorySpinnerAdapter);

        btnAddNewCategory=(ImageButton)findViewById(R.id.btnAddNewCategory);
        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        txtItemCode=(EditText)findViewById(R.id.txtItemCode);

        txtItemName=(EditText)findViewById(R.id.txtItemName);

        txtItemPrice=(EditText)findViewById(R.id.txtItemPrice);

        txtItemUnit=(EditText)findViewById(R.id.txtItemUnit);

        txtItemDescription=(EditText)findViewById(R.id.txtItemDescription);

        txtItemOutPutTax=(EditText)findViewById(R.id.txtItemOutPutTax);

        txtItemCode=(EditText)findViewById(R.id.txtItemCode);

        chkItemSaleInc=(CheckBox) findViewById(R.id.chkItemSaleInc);

        btnItemSave=(Button)findViewById(R.id.btnItemSave);


        btnItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProduct();
            }
        });

    }

    public void initializeDataForEdit(Product product){

        selectSpinnerItemByValue(spnCategories,product.getCategoryUid());
        txtItemCode.setText(product.getCode()==null?"":product.getCode());
        txtItemCode.setTag(product.getUid());
        txtItemName.setText(product.getName()==null?"":product.getName());
        txtItemPrice.setText(product.getRate()==null?"":product.getRate().toString());
        txtItemUnit.setText(product.getUnit()==null?"":product.getUnit());
        txtItemDescription.setText(product.getDescription()==null?"":product.getDescription());
        txtItemOutPutTax.setText(product.getOutPutTax()==null?"":product.getOutPutTax().toString());
        chkItemSaleInc.setChecked(product.getSalesInc()==null?false:product.getSalesInc());
        btnItemSave.setText("UPDATE");
    }

    /**
     * Method to select the sector item by value
     * @param spnr                  : Spinner object
     * @param uid     : The header id
     */
    public static void selectSpinnerItemByValue(Spinner spnr, String uid) {

        if(uid==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        CategorySpinnerAdapter adapter = (CategorySpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            Category category=(Category) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(category.getUid().equals(uid)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public void saveProduct(){

        if(validateItem()) {

            ProgressDialog dialog=new ProgressDialog(AddItemActivity.this);

            dialog.setMessage("Saving product...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.show();


            String itemUid=(txtItemCode.getTag()==null||txtItemCode.getTag().toString().isEmpty())? UUID.randomUUID().toString():txtItemCode.getTag().toString();

            Category category=(Category)spnCategories.getSelectedItem();

            Product product=new Product();
            product.setUid(itemUid);
            product.setCategoryUid(category.getUid());
            product.setCategoryName(category.getName());
            product.setCode(txtItemCode.getText().toString());
            product.setName(txtItemName.getText().toString());
            product.setUnit(txtItemUnit.getText().toString());
            product.setDescription(txtItemDescription.getText().toString());
            if(txtItemPrice.getText()!=null && !txtItemPrice.getText().toString().trim().isEmpty())
                product.setRate(Double.parseDouble(txtItemPrice.getText().toString()));
            else
                product.setRate(0.0);
            if(txtItemOutPutTax.getText()!=null && !txtItemOutPutTax.getText().toString().trim().isEmpty())
                product.setOutPutTax(Double.parseDouble(txtItemOutPutTax.getText().toString()));

            product.setSalesInc(chkItemSaleInc.isChecked());

            try {

                if(productService.save(product)>0){
                    Toast.makeText(getApplicationContext(), "Product saved successfully", Toast.LENGTH_SHORT).show();
                    clearSaveProduct();
                }else{
                    Toast.makeText(getApplicationContext(), "Product save failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Product save failed", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            dialog.dismiss();


        }
    }

    public void clearSaveProduct(){

        //spnCategories.setSelection(0);
        txtItemCode.setText("");
        txtItemName.setText("");
        txtItemPrice.setText("");
        txtItemUnit.setText("");
        txtItemDescription.setText("");
        txtItemOutPutTax.setText("");
        //chkItemSaleInc.setChecked(false);
        txtItemCode.setTag(null);
        btnItemSave.setText("SAVE");

    }

    public boolean validateItem(){

        boolean isValid=true;

        if(spnCategories.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_SHORT).show();

            isValid=false;
        }

        if(txtItemCode.getText().toString()==null||txtItemCode.getText().toString().trim().isEmpty()){

            txtItemCode.setError("Please enter item code");
            isValid=false;
        }

        if(txtItemName.getText().toString()==null||txtItemName.getText().toString().trim().isEmpty()){

            txtItemName.setError("Please enter item name");
            isValid=false;
        }

        return isValid;

    }
    /**
     * Fetch Category List From Database
     */
    protected void fetchCategoryList() {


        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mCategories.clear();

                    Category category=new Category();
                    category.setUid("0");
                    category.setName("- Choose Category -");
                    mCategories.add(category);

                    mCategories.addAll(categoryService.findActiveCategories());



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

        mCategorySpinnerAdapter.notifyDataSetChanged();

        if(mProduct!=null){
            initializeDataForEdit(mProduct);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_save:
                saveProduct();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCategoryList();
    }

    @Override
    public void onBackPressed() {

        this.finish();

    }

}
