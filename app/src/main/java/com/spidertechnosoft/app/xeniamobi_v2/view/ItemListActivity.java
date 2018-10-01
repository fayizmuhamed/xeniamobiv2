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
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.ItemListAdapter;

import java.util.ArrayList;
import java.util.List;


public class ItemListActivity extends AppCompatActivity implements ItemListAdapter.OperationCallback {

    ProductService productService=new ProductService();

    SaleService saleService=new SaleService();

    private Context mContext;

    private ListView mLvItemList;

    private EditText mEditItemSearch;

    ImageButton btnAddNewItem;

    //private Database mDatabase;

    List<Product> mProducts=new ArrayList<>();

    final Handler mHandler = new Handler();
    final Runnable mUpdateProductList = new Runnable() {

        @Override
        public void run() {
            updateProductList();
        }

    };


    ProgressDialog dlgProgress;

    private ItemListAdapter mItemListAdapter;

    //Get rest api manager
    //RestApiManager mRestApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

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

        this.mProducts=new ArrayList<Product>();
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
        fetchProductList();
    }

    public void configView(){

        btnAddNewItem=(ImageButton) findViewById(R.id.btnAddNewItem);

        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddItemActivity.class);
                startActivity(intent);
            }
        });

        mItemListAdapter=new ItemListAdapter(mContext,mProducts,this);

        mLvItemList=(ListView)findViewById(R.id.lvItemList);

        mLvItemList.setAdapter(mItemListAdapter);

        mEditItemSearch=(EditText)findViewById(R.id.txtItemSearch);

        mEditItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mItemListAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchProductList() {

        dlgProgress=new ProgressDialog(ItemListActivity.this);

        dlgProgress.setMessage("Fetching prodcuts...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mProducts.clear();

                    String query=null;

                    String[] params=null;

                    mProducts.addAll(productService.findActiveProducts());


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateProductList runnable
                mHandler.post(mUpdateProductList);
            }
        };
        t.start();
    }

    /**
     * Update View With Category Items
     */
    protected  void updateProductList(){

        //mItemListAdapter.notifyDataSetChanged();

        mItemListAdapter.getFilter().filter(mEditItemSearch.getText().toString());

        //Collections.sort(mProducts, new ProductNameComparator());

        dlgProgress.dismiss();

    }


    @Override
    public void onEditClick(Product product) {

        Intent intent = new Intent(getApplicationContext(),AddItemActivity.class);
        intent.putExtra("ITEM",product);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(final Product product) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ItemListActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ItemListActivity.this);
        }
        builder.setTitle("Delete Item")
                .setMessage("Are you sure you want to delete ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        deleteProduct(product);
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



    public void deleteProduct(Product product){

        ProgressDialog dialog=new ProgressDialog(ItemListActivity.this);

        dialog.setMessage("Deleting product...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


        try {

            if(saleService.isItemDeleteEnabled(product.getUid())) {
                product.setActive(IndicatorStatus.NO);
                if (productService.save(product) > 0) {
                    Toast.makeText(getApplicationContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Product delete failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                fetchProductList();
            }else {
                Toast.makeText(getApplicationContext(), "Product already used in transactions", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Product delete failed", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {

        this.finish();

    }
}
