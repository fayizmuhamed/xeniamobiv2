package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orm.OrmTransactionHelper;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.SaleLineItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.CategoryNameComparator;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.CategoryService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;
import com.spidertechnosoft.app.xeniamobi_v2.utils.WordUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.BillDetailListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.CategoryListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.POSDetailListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.PaymentTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.ProductGridAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.ProductListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.TaxTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.UserSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;
import com.spidertechnosoft.app.xeniamobi_v2.view.util.SwipeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PointOfSaleActivity extends AppCompatActivity implements CartOperationCallback,BillDetailListAdapter.OrderDetailCallback{

    SaleService saleService=new SaleService();

    Context mContext;
    SessionManager sessionManager;
    private TextView mTitle;
    private Toolbar mToolbar;

    HashMap<String,CartItem> cartItemHashMap=new HashMap<>();

    CategoryService categoryService=new CategoryService();

    ProductService productService=new ProductService();

    UserService userService=new UserService();

    ProgressDialog dlgProgress;

    ArrayList<Category> mCategories;

    List<Product> mProducts;

    List<CartItem> cartItems=new ArrayList<>();

    ArrayList<TaxType> mTaxTypes;

    ArrayList<Integer> mPaymentTypes;

    ArrayList<User> mStaffs;

    String currentCategory = "";

    private GridView mGvPOSItemList;

    boolean isEdit=false;

    RadioGroup rgPOSCategory;

    private EditText txtPOSInvoiceNo,txtPOSInvoiceDate;

    private Spinner spnPOSModeOfPayment,spnPOSTaxType,spnPOSStaff;

    private Button btnPOSScanBarcode,btnPOSPrint,btnPOSRefresh,btnPOSDelete,btnPOSClose,btnPOSSave;

    ListView lvPOSDetailsList;

    //SwipeMenuListView lvPOSDetailsList;

    SwipeMenuCreator mSwipeMenuCreator;

    private EditText mTxtPOSItemSearch,txtPOSTotalTax,txtPOSNetValue,txtPOSDiscount,txtPOSRoundOff,txtPOSBillTotal;


    private ProductGridAdapter mProductGridAdapter;

    POSDetailListAdapter mBillDetailListAdapter;

    TaxTypeSpinnerAdapter mTaxTypeSpinnerAdapter;

    PaymentTypeSpinnerAdapter mPaymentTypeSpinnerAdapter;

    UserSpinnerAdapter mUserSpinnerAdapter;

    private Sale mSale;

    User mUser;

    final Handler mHandler = new Handler();
    final Runnable mUpdateCategoryList = new Runnable() {

        @Override
        public void run() {
            updateCategoryList();
        }

    };

    final Runnable mUpdateStaffList = new Runnable() {

        @Override
        public void run() {
            updateStaffList();
        }

    };

    final Runnable mUpdateProductList = new Runnable() {

        @Override
        public void run() {
            updateProductList();
        }

    };

    AsyncTask<Sale, String, Boolean> mSaleTask;

    Dialog dlgPrintingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_sale);
        this.mContext=getApplicationContext();
        //this.mDatabase= Database.getInstance();
        sessionManager=new SessionManager(mContext);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        PrintUtil.getInstance().initialize(mContext);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActivityTitle(0,R.string.title_activity_point_of_sale);
        this.mProducts=new ArrayList<Product>();
        this.mCategories=new ArrayList<Category>();

        this.mTaxTypes=new ArrayList<TaxType>();
        this.mTaxTypes.clear();
        if(SettingService.getTaxType().equals(TaxType.VAT)) {
            this.mTaxTypes.add(TaxType.VAT);
            this.mTaxTypes.add(TaxType.NON_TAX);
        }else if(SettingService.getTaxType().equals(TaxType.GST)){
            this.mTaxTypes.add(TaxType.GST);
            this.mTaxTypes.add(TaxType.NON_TAX);
        }else{
            this.mTaxTypes.add(TaxType.NON_TAX);
        }

        this.mPaymentTypes=new ArrayList<Integer>();
        this.mPaymentTypes.clear();
        this.mPaymentTypes.add(PaymentType.CASH);
        this.mPaymentTypes.add(PaymentType.CARD);
        this.mPaymentTypes.add(PaymentType.CREDIT);
        this.mPaymentTypes.add(PaymentType.BOTH);

        this.mStaffs=new ArrayList<>();

        //user=mDatabase.getLoggedInUser();
        mUser=sessionManager.getLoggedInUser();

        Sale saleIn=(Sale) getIntent().getSerializableExtra(BillCreationActivity.SALE);

        if(saleIn==null){
            saleIn=new Sale();

        }

        mSale=saleIn;

        configView();
        fetchStaffList();
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

        txtPOSInvoiceNo=(EditText)findViewById(R.id.txtPOSInvoiceNo);

        txtPOSInvoiceDate=(EditText)findViewById(R.id.txtPOSInvoiceDate);

        spnPOSModeOfPayment=(Spinner)findViewById(R.id.spnPOSModeOfPayment);

        spnPOSTaxType=(Spinner)findViewById(R.id.spnPOSTaxType);
        mTaxTypeSpinnerAdapter = new TaxTypeSpinnerAdapter(this, R.layout.spinner_item, mTaxTypes);
        mTaxTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnPOSTaxType.setAdapter(mTaxTypeSpinnerAdapter);

        mPaymentTypeSpinnerAdapter=new PaymentTypeSpinnerAdapter(this,R.layout.spinner_item,mPaymentTypes);
        mPaymentTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnPOSModeOfPayment.setAdapter(mPaymentTypeSpinnerAdapter);

        spnPOSModeOfPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer paymentType=(Integer) parent.getItemAtPosition(position);

                if(paymentType.equals(PaymentType.CASH))
                    mSale.setPaymentType(PaymentType.CASH);
                else  if(paymentType.equals(PaymentType.CARD))
                    mSale.setPaymentType(PaymentType.CARD);
                else  if(paymentType.equals(PaymentType.CREDIT))
                    mSale.setPaymentType(PaymentType.CREDIT);
                else  if(paymentType.equals(PaymentType.BOTH))
                    mSale.setPaymentType(PaymentType.BOTH);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnPOSStaff=(Spinner)findViewById(R.id.spnPOSStaff);
        mUserSpinnerAdapter=new UserSpinnerAdapter(this,R.layout.spinner_item,mStaffs);
        mUserSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnPOSStaff.setAdapter(mUserSpinnerAdapter);

        selectTaxTypeSpinnerItemByValue(spnPOSTaxType, SettingService.getTaxType());

        btnPOSScanBarcode=(Button)findViewById(R.id.btnPOSScanBarcode);

        btnPOSRefresh=(Button)findViewById(R.id.btnPOSRefresh);

        btnPOSPrint=(Button)findViewById(R.id.btnPOSPrint);

        btnPOSClose=(Button)findViewById(R.id.btnPOSClose);

        btnPOSDelete=(Button)findViewById(R.id.btnPOSDelete);

        btnPOSSave=(Button)findViewById(R.id.btnPOSSave);

        mSwipeMenuCreator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setWidth(getResources().getInteger(R.integer.order_delete_button_width));
                item1.setTitle(null);
                item1.setBackground(R.color.delete_button_bg);
                item1.setIcon(R.drawable.icon_delete);
                menu.addMenuItem(item1);

               /* SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setWidth(getResources().getInteger(R.integer.order_delete_button_width));
                item2.setTitle(null);
                item2.setIcon(R.drawable.icon_narration);
                item2.setBackground(R.color.narration_button_bg);
                menu.addMenuItem(item2);*/

            }
        };



        // lvPOSDetailsList=(SwipeMenuListView) findViewById(R.id.lvPOSDetailsList);
        lvPOSDetailsList=(ListView) findViewById(R.id.lvPOSDetailsList);


        mBillDetailListAdapter =new POSDetailListAdapter(mContext,cartItems,this);

        lvPOSDetailsList.setAdapter(mBillDetailListAdapter);


        spnPOSStaff=(Spinner)findViewById(R.id.spnPOSStaff);

        mProductGridAdapter=new ProductGridAdapter(mContext,mProducts);

        rgPOSCategory = (RadioGroup) findViewById(R.id.rgPOSCategory);

        mGvPOSItemList=(GridView)findViewById(R.id.gvPOSItemList) ;

        mGvPOSItemList.setAdapter(mProductGridAdapter);

        mGvPOSItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("START",new Date().toString());
                Product product=(Product)mProductGridAdapter.getItem(position);
                Log.e("INCREASE",new Date().toString());
                increaseCartItem(product);
                Log.e("UPDATE POS",new Date().toString());
                updatePOSItemDetails();
                Log.e("END",new Date().toString());
            }
        });

        mTxtPOSItemSearch=(EditText)findViewById(R.id.txtPOSItemSearch);

        mTxtPOSItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(rgPOSCategory.getCheckedRadioButtonId()==rgPOSCategory.getChildAt(0).getId()){
                    mProductGridAdapter.getFilter().filter(s.toString());
                }else{
                    new Handler().postDelayed(
                            new Runnable(){
                                @Override
                                public void run() {
                                    rgPOSCategory.check(rgPOSCategory.getChildAt(0).getId());
                                }
                            }, 100);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        txtPOSTotalTax=(EditText)findViewById(R.id.txtPOSTotalTax);

        txtPOSNetValue=(EditText)findViewById(R.id.txtPOSNetValue);

        txtPOSDiscount=(EditText)findViewById(R.id.txtPOSDiscount);

        txtPOSRoundOff=(EditText)findViewById(R.id.txtPOSRoundOff);

        txtPOSBillTotal=(EditText) findViewById(R.id.txtPOSBillTotal);

        txtPOSDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String discount = txtPOSDiscount.getText().toString();

                Double discountAmount = Double.parseDouble((discount == null || discount.equals("")) ? "0" :(discount.startsWith(".")?"0.": discount));

                mSale.setDiscount (discountAmount);

                //mSale.setDiscount(discountAmount);
                String gross = txtPOSNetValue.getText().toString();

                Double grossAmount = Double.parseDouble((gross == null || gross.equals("")) ? "0" : (gross.startsWith(".")?"0.": gross));

                Double grandAmount=grossAmount-discountAmount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                txtPOSBillTotal.setText(GeneralMethods.formatNumber(netAmount));

                Double roundOff=netAmount-grandAmount;

                txtPOSRoundOff.setText(GeneralMethods.formatNumber(roundOff));



            }
        });

        btnPOSRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDatabase.clearCart();
                mSale=new Sale();
                initializeData();
            }
        });

        btnPOSPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSale!=null)
                    PrintUtil.getInstance().printSale(mSale);
            }
        });

        btnPOSDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSale!=null)
                    cancelSale();
            }
        });

        btnPOSClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPOSSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSale.getPaymentType().equals(PaymentType.CASH)||mSale.getPaymentType().equals(PaymentType.BOTH)||SettingService.isStaffAuthenticationRequired().equals(IndicatorStatus.YES)){
                    showPaymentPointOfSaleDialog();
                }else {
                    postOrder();
                }


            }
        });



    }



    private void setActivityTitle(Integer type, Integer resourceId){

        if(type==null||type==0){

            mToolbar.setBackgroundResource(R.color.colorPrimary);

        }else{
            mToolbar.setBackgroundResource(R.drawable.order_action_bar_bg);
        }

        mTitle.setText(getResources().getString(resourceId));
    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchStaffList() {

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mStaffs.clear();

                    mStaffs.addAll(userService.findActiveStaffUsers(mUser));


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateProductList runnable
                mHandler.post(mUpdateStaffList);
            }
        };
        t.start();
    }

    /**
     * Update View With Category Items
     */
    protected  void updateStaffList(){

        mUserSpinnerAdapter.notifyDataSetChanged();

        initializeData();

       /* String staffUid=mSale.getUserId();

        staffUid=(staffUid==null?sessionManager.getLoggedInUser().getUid():staffUid);

        selectStaffSpinnerItemByValue(spnPOSStaff,staffUid);*/

    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchCategoryList() {

        dlgProgress=new ProgressDialog(PointOfSaleActivity.this);

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

        rgPOSCategory.clearCheck();

        //rbgCategory.removeAllViews();
        rgPOSCategory.removeAllViews();

        currentCategory="";

        if(mCategories==null||mCategories.isEmpty()){

            mProducts.clear();

            updateProductList();

            dlgProgress.dismiss();

            return;
        }

        /*if ( currentCategory.isEmpty()) {

            Category category = mCategories.get(0);

            currentCategory = category.getUid();

        }*/




        for (int i = 0; i < mCategories.size(); i++) {
            final View radioButtonView = getLayoutInflater().inflate(R.layout.layout_category_radio_button, null);
            RadioButton button = (RadioButton) radioButtonView.findViewById(R.id.radio_button);

            RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);
            params_soiled.setMargins(0, 0, 0, 5);
            button.setLayoutParams(params_soiled);

            Category category = mCategories.get(i);
            button.setId(10000 + i);
            button.setText(WordUtil.capitalize(category.getName().toLowerCase()));
            button.setTag(category.getUid());
            //button.setChecked(category.getId().longValue() == currentCategory);
            rgPOSCategory.addView(button);

            /*if (category.getId().longValue() == currentCategory) {


                currentCategoryColumn = i;

                //imgView.getParent().requestChildFocus(imgView, imgView);
                layCategoryScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int right = radioButtonView.getRight() * currentCategoryColumn;
                        //hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        layCategoryScrollView.smoothScrollBy(right, 0);
                    }
                }, 100);

            }*/

        }

        rgPOSCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rbtnCategory = (RadioButton)findViewById(checkedId);


                if(rbtnCategory==null){

                    return;
                }

                Boolean isChecked=rbtnCategory.isChecked();

                Category category = getCurrentCategory(rbtnCategory.getTag().toString());

                if (category != null) {

                    if(!currentCategory.equals(category.getUid())) {
                        currentCategory = category.getUid();
                        fetchProductList(category);
                    }
                    //updateProductList();
                    // fetchProductList();
                }
            }
        });

        selectCategoryRadioGroupByCategoryId(rgPOSCategory,currentCategory);

        dlgProgress.dismiss();

    }

    public Category getCurrentCategory(String uid) {

        for (Category category : mCategories) {

            if (category.getUid().equals(uid)) {
                return category;
            }
        }
        return null;
    }

    public void selectCategoryRadioGroupByCategoryId(RadioGroup radioGroup,String currentCategory){

        for(int i=0;i<radioGroup.getChildCount();i++){

            final int position=i;

            RadioButton radioButton=(RadioButton)radioGroup.getChildAt(i);

            if(radioButton==null){

                continue;
            }

            if(radioButton.getTag().equals(currentCategory) && !radioButton.isChecked()){

                radioGroup.check(radioButton.getId());


            }
        }
    }

    /**
     * Fetch Product List From Database
     */
    protected void fetchProductList(final Category category) {


        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {



                    mProducts.clear();

                    String query=null;

                    String[] params=null;

                    if(category!=null&&category.getUid()!=null){

                        query="category_Uid=?";
                        params=new String[]{category.getUid()};
                    }

                    List<Product> products=productService.findByQuery(query,params,null,null,null);

                    mProducts.addAll(products);


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

        //mProductListAdapter.notifyDataSetChanged();
        mProductGridAdapter.getFilter().filter(mTxtPOSItemSearch.getText().toString());

    }

    @Override
    public boolean changeQuantity(Product product, Double qty) {

        if(qty<=0){
            if(cartItemHashMap.containsKey(product.getUid()))
                cartItemHashMap.remove(product.getUid());
        }else{

            CartItem cartItem = null;//sessionManager.getCartContentForProduct(product);
            if(cartItemHashMap.containsKey(product.getUid()))
                cartItem=cartItemHashMap.get(product.getUid());
            if (cartItem == null) {

                cartItem=new CartItem();
                cartItem.setProduct(product);
                cartItem.setItemUid(product.getUid());
                cartItem.setItemCode(product.getCode());
                cartItem.setItemName(product.getName());
                cartItem.setCategoryUid(product.getCategoryUid());
                cartItem.setCategoryName(product.getCategoryName());
                Double rate = getProductPrice(product);
                Double outPutTax=product.getOutPutTax();
                Integer cartQty = 0;
                cartItem.setItemQty(cartQty);
                cartItem.setItemUnitPrice(rate);
                cartItem.setOutPutTax(outPutTax);
                cartItem.setSalesInc(product.getSalesInc());

            }

            Integer cartQty = qty.intValue();
            Double rate = cartItem.getItemUnitPrice()==null?0:cartItem.getItemUnitPrice();
            cartItem.setItemQty(cartQty);
            cartItemHashMap.put(product.getUid(),cartItem);
        }

        setFooterCartTotal();

        return true;
    }

    @Override
    public boolean reduceAndRemoveFromCart(Product product) {

        if(!cartItemHashMap.containsKey(product.getUid()))
            return true;

        CartItem cartItem = cartItemHashMap.get(product.getUid());

        Integer cartQty = cartItem.getItemQty();
        Double rate = cartItem.getItemUnitPrice()==null?0:cartItem.getItemUnitPrice();
        cartQty = cartQty - 1;
        if(cartQty<=0){

            cartItemHashMap.remove(product.getUid());

        }else{
            cartQty=cartQty>0?cartQty:0;
            cartItem.setItemQty(cartQty);
            cartItemHashMap.put(product.getUid(),cartItem);
        }


        updatePOSItemDetails();


        return true;
    }

    @Override
    public boolean removeFromCart(Product product) {

        if(!cartItemHashMap.containsKey(product.getUid()))
            return true;

        cartItemHashMap.remove(product.getUid());

        updatePOSItemDetails();

        return true;
    }

    @Override
    public Integer getCartQty(Product product) {

        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = null;//sessionManager.getCartContentForProduct(product);
        if(cartItemHashMap.containsKey(product.getUid()))
            cartItem=cartItemHashMap.get(product.getUid());

        return ((cartItem == null||cartItem.getItemQty()==null)?0:cartItem.getItemQty().intValue());
    }

    @Override
    public boolean changeItemPrice(Product product, Double price) {

        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = null;//sessionManager.getCartContentForProduct(product);
        if(cartItemHashMap.containsKey(product.getUid()))
            cartItem=cartItemHashMap.get(product.getUid());

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setItemUid(product.getUid());
            cartItem.setItemCode(product.getCode());
            cartItem.setItemName(product.getName());
            cartItem.setCategoryUid(product.getCategoryUid());
            cartItem.setCategoryName(product.getCategoryName());
            Double rate = getProductPrice(product);
            Double outPutTax=product.getOutPutTax();
            Integer cartQty = 0;
            cartItem.setItemQty(cartQty);
            cartItem.setItemUnitPrice(rate);
            cartItem.setOutPutTax(outPutTax);
            cartItem.setSalesInc(product.getSalesInc());

        }

        Integer cartQty = cartItem.getItemQty();
        Double rate = price;
        cartItem.setItemUnitPrice(rate);
        Double amount = cartQty*rate;

        //mDatabase.updateCart(cartItem);
        cartItemHashMap.put(product.getUid(),cartItem);

        setFooterCartTotal();

        // Return true;
        return true;
    }

    @Override
    public boolean increaseCartItem(Product product) {



        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = null;//sessionManager.getCartContentForProduct(product);
        if(cartItemHashMap.containsKey(product.getUid()))
            cartItem=cartItemHashMap.get(product.getUid());

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setItemUid(product.getUid());
            cartItem.setItemCode(product.getCode());
            cartItem.setItemName(product.getName());
            cartItem.setCategoryUid(product.getCategoryUid());
            cartItem.setCategoryName(product.getCategoryName());
            Double rate = getProductPrice(product);
            Double outPutTax=product.getOutPutTax();
            Integer cartQty = 0;
            cartItem.setItemQty(cartQty);
            cartItem.setItemUnitPrice(rate);
            cartItem.setOutPutTax(outPutTax);
            cartItem.setSalesInc(product.getSalesInc());

        }

        Integer cartQty = cartItem.getItemQty();
        Double rate = cartItem.getItemUnitPrice()==null?0:cartItem.getItemUnitPrice();
        cartQty = cartQty + 1;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        cartItemHashMap.put(product.getUid(),cartItem);

        //mDatabase.updateCart(cartItem);
        // sessionManager.updateCart(cartItem);

        setFooterCartTotal();

        // Return true;
        return true;
    }

    @Override
    public boolean reduceFromCart(Product product) {


        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = null;//sessionManager.getCartContentForProduct(product);
        if(cartItemHashMap.containsKey(product.getUid()))
            cartItem=cartItemHashMap.get(product.getUid());

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            return false;
        }

        Integer cartQty = cartItem.getItemQty();
        Double rate = cartItem.getItemUnitPrice()==null?0:cartItem.getItemUnitPrice();
        cartQty = cartQty - 1;
        cartQty=cartQty>0?cartQty:0;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        //mDatabase.updateCart(cartItem);
        cartItemHashMap.put(product.getUid(),cartItem);


        setFooterCartTotal();

        // Return true;
        return true;
    }

    public Double getProductPrice(Product product){


        return product.getRate();
    }

    public void setFooterCartTotal(){

        Double totalAmount=getTotalAmount();

        // lblCounterDeskTotalAmount.setText(GeneralMethods.formatNumber(totalAmount));

        Double totalTaxAmount=getTotalTaxAmount();

        txtPOSTotalTax.setText(GeneralMethods.formatNumber(totalTaxAmount));

        Double grossAmount=totalAmount+totalTaxAmount;

        txtPOSNetValue.setText(GeneralMethods.formatNumber(grossAmount));

        Double discount=mSale.getDiscount ();

        txtPOSDiscount.setText(GeneralMethods.formatNumber(discount));

        Double grandAmount=grossAmount-discount;

        Double netAmount=Double.valueOf(Math.round(grandAmount));

        txtPOSBillTotal.setText(GeneralMethods.formatNumber(netAmount));

        Double roundOff=netAmount-grandAmount;

        txtPOSRoundOff.setText(GeneralMethods.formatNumber(roundOff));

        Double cashReceivedAmount = mSale.getCashReceived ();

        Double cardReceivedAmount = mSale.getCardReceived ();

        Double returnedAmount = mSale.getReturnedAmount ();

        // txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

        //txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

        //txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(netAmount));

        /*if(mSale.getSaleUid()==null){
            if(paymentType.equals(PaymentType.CASH)){
                if(cashReceivedAmount>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }else if(paymentType.equals(PaymentType.CARD)){
                if(cardReceivedAmount>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }else if(paymentType.equals(PaymentType.BOTH)){
                if((cashReceivedAmount+cardReceivedAmount)>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount+cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }
        }*/

        //txtCounterDeskChange.setText(GeneralMethods.formatNumber(returnedAmount));


    }

    public Double getTotalAmount(){

        Double total=0.0;

        List<CartItem> cartItemList=new ArrayList<>(cartItemHashMap.values());

        for(CartItem cartItem:cartItemList){

            Double itemPrice=cartItem.getItemUnitPrice();
            Integer itemQty=cartItem.getItemQty();
            Double grossAmount=(itemPrice* itemQty);
            Double itemDiscount=0.0;
            Double amount=grossAmount-itemDiscount;
            Double taxableAmount= GeneralMethods.getBasePrice(amount,cartItem.getOutPutTax(),cartItem.getSalesInc());

            total=total+taxableAmount;
        }
        return total;
    }

    public Double getTotalTaxAmount(){

        Double totalTax=0.0;


        List<CartItem> cartItemList=new ArrayList<>(cartItemHashMap.values());

        for(CartItem cartItem:cartItemList){

            Double itemPrice=cartItem.getItemUnitPrice();
            Integer itemQty=cartItem.getItemQty();
            Double grossAmount=(itemPrice* itemQty);
            Double itemDiscount=0.0;
            Double amount=grossAmount-itemDiscount;
            Double taxAmount=GeneralMethods.getTaxAmount(amount,cartItem.getOutPutTax(),cartItem.getSalesInc());

            totalTax=totalTax+taxAmount;
        }
        return totalTax;
    }

    public static void selectTaxTypeSpinnerItemByValue(Spinner spnr, TaxType mTaxType) {

        if(mTaxType==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        TaxTypeSpinnerAdapter adapter = (TaxTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            TaxType taxType=(TaxType) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(taxType.equals(mTaxType)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public static void selectPaymentTypeSpinnerItemByValue(Spinner spnr, Integer mPaymentType) {

        if(mPaymentType==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        PaymentTypeSpinnerAdapter adapter = (PaymentTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            Integer paymentType=(Integer) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(paymentType.equals(mPaymentType)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public static void selectStaffSpinnerItemByValue(Spinner spnr, String mUser) {

        if(mUser==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        UserSpinnerAdapter adapter = (UserSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            User user=(User) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(user.getUid().equals(mUser)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    @Override
    public void setSelectedItem(int position, CartItem cartItem) {

    }

    public void updatePOSItemDetails(){

        cartItems.clear();

        //List<CartItem> cartItemList=mDatabase.getCartContent();
        List<CartItem> cartItemList=new ArrayList<>(cartItemHashMap.values());

        if(cartItemList!=null){

            cartItems.addAll(cartItemList);
        }

        mBillDetailListAdapter.notifyDataSetChanged();


        setFooterCartTotal();

    }

    public void cancelSale(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PointOfSaleActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PointOfSaleActivity.this);
        }
        builder.setTitle("Delete Sale")
                .setMessage("Are you sure you want to delete ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        deleteSale();
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

    public void deleteSale(){

        ProgressDialog dialog=new ProgressDialog(PointOfSaleActivity.this);

        dialog.setMessage("Deleting sale...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


        try {

            if(saleService.deleteSale(mSale)>0){
                Toast.makeText(getApplicationContext(), "Sale deleted successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Sale delete failed", Toast.LENGTH_SHORT).show();
            }
            mSale=new Sale();
            initializeData();
            dialog.dismiss();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Sale delete failed", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }

    public void postOrder(){

        if(validateCart()) {

            mSaleTask = new AsyncTask<Sale, String, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    // Declare the customer dialog
                    dlgPrintingProgress = new Dialog(PointOfSaleActivity.this);

                    // 	SEt no title for the dialog
                    dlgPrintingProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    // Set the content view to the customer_alert layout
                    dlgPrintingProgress.setContentView(R.layout.layout_custom_printing_progress);

                    ProgressBar progress = (ProgressBar) dlgPrintingProgress.findViewById(R.id.pbPrintingProgress);
                    progress.setIndeterminate(true);

                    // Cancel the dialog when touched outside.
                    dlgPrintingProgress.setCanceledOnTouchOutside(false);

                    dlgPrintingProgress.setCancelable(false);

                    dlgPrintingProgress.show();

                    if(mSale.getSaleUid()==null){
                        mSale.setSaleUid(UUID.randomUUID().toString());
                        mSale.setSalesDate(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.SERVER_DATE_TIME_FORMAT));
                    }

                    // List<CartItem> cartItems=mDatabase.getCartContent();
                    List<CartItem> cartItems=new ArrayList<>(cartItemHashMap.values());

                    List<SaleLineItem> items=new ArrayList<>();
                    Double total=0.0;
                    Double totalTax=0.0;
                    Double discount=mSale.getDiscount();


                    for(CartItem cartItem:cartItems){

                        SaleLineItem saleLineItem=new SaleLineItem();
                        saleLineItem.setSaleUid(mSale.getSaleUid());
                        saleLineItem.setItemUid(cartItem.getItemUid());
                        saleLineItem.setItemCode(cartItem.getItemCode());
                        saleLineItem.setItemName(cartItem.getItemName());
                        saleLineItem.setCategoryUid(cartItem.getCategoryUid());
                        saleLineItem.setCategoryName(cartItem.getCategoryName());
                        saleLineItem.setItemRate(cartItem.getItemUnitPrice());
                        saleLineItem.setQty(cartItem.getItemQty());
                        saleLineItem.setSalesInc(cartItem.getSalesInc());
                        Double itemOutPutTax=cartItem.getOutPutTax()==null?0:cartItem.getOutPutTax();
                        saleLineItem.setTaxPer(itemOutPutTax);
                        Double itemPrice=cartItem.getItemUnitPrice();
                        Integer itemQty=cartItem.getItemQty();
                        Double grossAmount=(itemPrice* itemQty);
                        saleLineItem.setGrossAmount(grossAmount);
                        Double itemDiscount=0.0;
                        saleLineItem.setDiscount(itemDiscount);
                        Double amount=grossAmount-itemDiscount;
                        saleLineItem.setAmount(amount);
                        Double taxableAmount=GeneralMethods.getBasePrice(amount,cartItem.getOutPutTax(),cartItem.getSalesInc());
                        Double taxAmount=GeneralMethods.getTaxAmount(amount,cartItem.getOutPutTax(),cartItem.getSalesInc());
                        saleLineItem.setTaxableAmount(taxableAmount);
                        saleLineItem.setTaxAmount(taxAmount);
                        saleLineItem.setNetAmount(taxableAmount+taxAmount);
                        saleLineItem.setNarration(cartItem.getItemNarration());

                        items.add(saleLineItem);
                        total=total+taxableAmount;
                        totalTax=totalTax+taxAmount;
                    }
                    mSale.setItems(items);
                    mSale.setTotalAmount(total);
                    mSale.setTaxAmount(totalTax);
                    Double grossAmount=total+totalTax;
                    mSale.setGrossAmount(grossAmount);
                    Double grandAmount=grossAmount-discount;
                    Double netAmount= Double.valueOf(Math.round(grandAmount));
                    mSale.setNetAmount(netAmount);
                    Double roundOff=grandAmount-netAmount;

                    if(mSale.getPaymentType().equals(PaymentType.CASH)) {
                        mSale.setCardReceived(0.0);
                    }else if(mSale.getPaymentType().equals(PaymentType.CARD))
                        mSale.setCashReceived(0.0);
                    else if(mSale.getPaymentType().equals(PaymentType.CREDIT)) {
                        mSale.setCashReceived(0.0);
                        mSale.setCardReceived(0.0);
                    }
                    mSale.setRoundOff(roundOff);

                }

                @Override
                protected Boolean doInBackground(Sale... params) {

                    final Sale saleToSave = (params == null || params.length == 0) ? null : params[0];


                    boolean isSaved = OrmTransactionHelper.doInTransaction(new OrmTransactionHelper.Callback() {

                        @Override
                        public void manipulateInTransaction() throws Exception {

                            try {



                                Long savedSale=saleService.save(saleToSave);

                                if(savedSale==null){

                                    throw new Exception("Save operation failed");
                                }

                                saleToSave.setId(savedSale);

                            } catch (Exception ex) {

                                publishProgress(ex.getMessage());

                                throw ex;
                            }


                        }

                    });

                    if (isSaved)
                        PrintUtil.getInstance().printSale(mSale);

                    // return true
                    return isSaved;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    Toast.makeText(getApplicationContext(), "Bill save failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void onPostExecute(Boolean isSaved) {
                    super.onPostExecute(isSaved);

                    if (isSaved) {
                        //mDatabase.clearCart();
                        mSale=new Sale();
                        initializeData();
                        dlgPrintingProgress.dismiss();

                    } else {

                        dlgPrintingProgress.dismiss();
                    }
                }
            };

            mSaleTask.execute(mSale);

        }

    }

    public boolean validateCart(){

        // List<CartItem> cartItems=mDatabase.getCartContent();
        List<CartItem> cartItems=new ArrayList<>(cartItemHashMap.values());

        if(cartItems==null||cartItems.isEmpty()){

            Toast.makeText(getApplicationContext(),"No Items in cart", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(CartItem cartItem:cartItems){

            if(cartItem.getItemQty()==null||cartItem.getItemQty()==0){

                Toast.makeText(getApplicationContext(),"Product "+cartItem.getItemName()+" with zero qty", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void initializeData(){

        cartItemHashMap.clear();

        if(mSale.getSaleUid()==null){

            isEdit=false;
            mSale.setUserId(mUser.getUid());
            mSale.setUserName(mUser.getName());
        }else{

            isEdit=true;
            List<SaleLineItem> saleLineItemList=mSale.getSaleLineItems();

            if(saleLineItemList!=null&& !saleLineItemList.isEmpty()){

                for(SaleLineItem saleLineItem: saleLineItemList){
                    CartItem cartItem=new CartItem();
                    cartItem.setItemUid(saleLineItem.getItemUid());
                    cartItem.setItemCode(saleLineItem.getItemCode());
                    cartItem.setItemName(saleLineItem.getItemName());
                    cartItem.setCategoryUid(saleLineItem.getCategoryUid());
                    cartItem.setCategoryName(saleLineItem.getCategoryName());
                    cartItem.setItemQty(saleLineItem.getQty());
                    cartItem.setItemUnitPrice(saleLineItem.getItemRate());
                    cartItem.setSalesInc(saleLineItem.getSalesInc());
                    cartItem.setOutPutTax(saleLineItem.getTaxPer());
                    cartItem.setItemNarration(saleLineItem.getNarration());
                    Product product=productService.findByUid(saleLineItem.getItemUid());

                    if(product!=null){

                        cartItem.setProduct(product);
                    }

                    cartItemHashMap.put(cartItem.getItemUid(),cartItem);

                }
            }

        }


        txtPOSInvoiceNo.setText((mSale.getSalesNo()==null||mSale.getSalesNo().trim().isEmpty())?"XXXXXXXX":mSale.getSalesNo());
        if(mSale.getSalesDate()==null||mSale.getSalesDate().trim().isEmpty()){

            txtPOSInvoiceDate.setText(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT));
        }else{
            txtPOSInvoiceDate.setText(GeneralMethods.convertDateFormat(mSale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT));
        }

        if(mSale.getPaymentType()==null){

            selectPaymentTypeSpinnerItemByValue(spnPOSModeOfPayment,PaymentType.CASH);

        }else if(mSale.getPaymentType().equals(PaymentType.CASH)){

            selectPaymentTypeSpinnerItemByValue(spnPOSModeOfPayment,PaymentType.CASH);


        }else if(mSale.getPaymentType().equals(PaymentType.CARD)){

            selectPaymentTypeSpinnerItemByValue(spnPOSModeOfPayment,PaymentType.CARD);

        }else if(mSale.getPaymentType().equals(PaymentType.CREDIT)){

            selectPaymentTypeSpinnerItemByValue(spnPOSModeOfPayment,PaymentType.CREDIT);

        }else if(mSale.getPaymentType().equals(PaymentType.BOTH)){

            selectPaymentTypeSpinnerItemByValue(spnPOSModeOfPayment,PaymentType.BOTH);

        }

        selectTaxTypeSpinnerItemByValue(spnPOSTaxType, SettingService.getTaxType());

        if(isEdit){
            btnPOSPrint.setEnabled(true);
            btnPOSRefresh.setEnabled(true);
            btnPOSClose.setEnabled(true);
            btnPOSDelete.setEnabled(true);
        }else{
            btnPOSPrint.setEnabled(false);
            btnPOSRefresh.setEnabled(false);
            btnPOSClose.setEnabled(false);
            btnPOSDelete.setEnabled(false);
        }


        updatePOSItemDetails();

        if(SettingService.isStaffSelectionRequired().equals(IndicatorStatus.YES))
            spnPOSStaff.setEnabled(true);
        else
            spnPOSStaff.setEnabled(false);

        String staffUid=mSale.getUserId();

        staffUid=(staffUid==null?sessionManager.getLoggedInUser().getUid():staffUid);

        selectStaffSpinnerItemByValue(spnPOSStaff,staffUid);

    }

    public void showPaymentPointOfSaleDialog(){

        // Declare the customer dialog
        final Dialog dlgPaymentPointOfSale = new Dialog(PointOfSaleActivity.this);

        // 	SEt no title for the dialog
        dlgPaymentPointOfSale.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Set the content view to the customer_alert layout
        dlgPaymentPointOfSale.setContentView(R.layout.layout_payment_point_of_sale);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        width=width>600?600:width;

        dlgPaymentPointOfSale.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView txtStaffName=(TextView) dlgPaymentPointOfSale.findViewById(R.id.txtStaffName);

        TextView txtCounterDeskBillTotal=(TextView)dlgPaymentPointOfSale.findViewById(R.id.txtCounterDeskBillTotal);

        TextInputLayout tilCounterDeskCashReceived=(TextInputLayout)dlgPaymentPointOfSale.findViewById(R.id.tilCounterDeskCashReceived);

        TextInputLayout tilCounterDeskCardReceived=(TextInputLayout)dlgPaymentPointOfSale.findViewById(R.id.tilCounterDeskCardReceived);

        TextInputLayout tilCounterDeskChange=(TextInputLayout)dlgPaymentPointOfSale.findViewById(R.id.tilCounterDeskChange);

        final EditText txtCounterDeskCashReceived=(EditText) dlgPaymentPointOfSale.findViewById(R.id.txtCounterDeskCashReceived);

        final EditText txtCounterDeskCardReceived=(EditText) dlgPaymentPointOfSale.findViewById(R.id.txtCounterDeskCardReceived);

        final EditText txtCounterDeskChange=(EditText) dlgPaymentPointOfSale.findViewById(R.id.txtCounterDeskChange);

        final EditText txtStaffPassword=(EditText) dlgPaymentPointOfSale.findViewById(R.id.txtStaffPassword);

        LinearLayout layAuthenticationContainer=(LinearLayout)dlgPaymentPointOfSale.findViewById(R.id.layAuthenticationContainer);

        Button btnStaffAuthenticationCancel=(Button) dlgPaymentPointOfSale.findViewById(R.id.btnStaffAuthenticationCancel);

        Button btnStaffAuthenticationDone=(Button) dlgPaymentPointOfSale.findViewById(R.id.btnStaffAuthenticationDone);

        txtStaffName.setText(mSale.getUserName()==null?"":mSale.getUserName());

        final Integer paymentType=(Integer) spnPOSModeOfPayment.getSelectedItem();

        txtCounterDeskCashReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String net = txtPOSBillTotal.getText().toString();

                Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : (net.startsWith(".")?"0.": net));

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : (cashReceived.startsWith(".")?"0.": cashReceived));

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : (cardReceived.startsWith(".")?"0.": cardReceived));

                mSale.setCashReceived (cashReceivedAmount);


                if(mSale.getSaleUid()==null){
                    if(paymentType.equals(PaymentType.CASH)){
                        if(cashReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }else if(paymentType.equals(PaymentType.CARD)){
                        if(cardReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }else if(paymentType.equals(PaymentType.BOTH)){
                        if((cashReceivedAmount+cardReceivedAmount)>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount+cardReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }
                }


            }
        });

        txtCounterDeskCardReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String net = txtPOSBillTotal.getText().toString();

                Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : (net.startsWith(".")?"0.": net));

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : (cashReceived.startsWith(".")?"0.": cashReceived));

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : (cardReceived.startsWith(".")?"0.": cardReceived));

                mSale.setCardReceived (cardReceivedAmount);

                if(mSale.getSaleUid()==null){
                    if(paymentType.equals(PaymentType.CASH)){
                        if(cashReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }else if(paymentType.equals(PaymentType.CARD)){
                        if(cardReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }else if(paymentType.equals(PaymentType.BOTH)){
                        if((cashReceivedAmount+cardReceivedAmount)>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount+cardReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }
                }


            }
        });

        txtCounterDeskChange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String change = txtCounterDeskChange.getText().toString();

                Double returnedAmount = Double.parseDouble((change == null || change.equals("")) ? "0" : (change.startsWith(".")?"0.": change));

                mSale.setReturnedAmount (returnedAmount);

            }
        });

        if(paymentType.equals(PaymentType.CASH)) {
            tilCounterDeskCashReceived.setVisibility(View.VISIBLE);
            tilCounterDeskCardReceived.setVisibility(View.GONE);
            tilCounterDeskChange.setVisibility(View.VISIBLE);
        }else if(paymentType.equals(PaymentType.BOTH)) {
            tilCounterDeskCashReceived.setVisibility(View.VISIBLE);
            tilCounterDeskCardReceived.setVisibility(View.VISIBLE);
            tilCounterDeskChange.setVisibility(View.VISIBLE);
        }else{
            tilCounterDeskCashReceived.setVisibility(View.GONE);
            tilCounterDeskCardReceived.setVisibility(View.GONE);
            tilCounterDeskChange.setVisibility(View.GONE);
        }

        if(SettingService.isStaffAuthenticationRequired().equals(IndicatorStatus.YES)) {
            layAuthenticationContainer.setVisibility(View.VISIBLE);
        }else {
            layAuthenticationContainer.setVisibility(View.GONE);
        }
        String net = txtPOSBillTotal.getText().toString();

        Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : (net.startsWith(".")?"0.": net));

        Double cashReceivedAmount = mSale.getCashReceived ();

        Double cardReceivedAmount = mSale.getCardReceived ();

        Double returnedAmount = mSale.getReturnedAmount ();

        txtCounterDeskBillTotal.setText(GeneralMethods.formatNumber(netAmount));

        txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

        txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

        if(mSale.getSaleUid()==null){
            if(paymentType.equals(PaymentType.CASH)){
                if(cashReceivedAmount>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }else if(paymentType.equals(PaymentType.CARD)){
                if(cardReceivedAmount>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }else if(paymentType.equals(PaymentType.BOTH)){
                if((cashReceivedAmount+cardReceivedAmount)>0)
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount+cardReceivedAmount-netAmount));
                else
                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
            }
        }

        txtCounterDeskChange.setText(GeneralMethods.formatNumber(returnedAmount));

        btnStaffAuthenticationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dlgPaymentPointOfSale.dismiss();
            }
        });

        btnStaffAuthenticationDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SettingService.isStaffAuthenticationRequired().equals(IndicatorStatus.YES)){

                    String password = txtStaffPassword.getText().toString();

                    String userUid = mSale.getUserId();

                    if (userUid == null || userUid.trim().isEmpty()) {

                        Toast.makeText(PointOfSaleActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password == null || password.trim().isEmpty()) {

                        txtStaffPassword.setError("Please enter password");
                        return;
                    }

                    User user = userService.findByUid(userUid);

                    if (user == null) {

                        Toast.makeText(PointOfSaleActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    if (!user.getPassword().equals(password)) {

                        txtStaffPassword.setError("Please enter valid credential");

                        return;
                    }

                }

                dlgPaymentPointOfSale.dismiss();

                postOrder();
            }
        });


        // Cancel the dialog when touched outside.
        dlgPaymentPointOfSale.setCanceledOnTouchOutside(false);

        dlgPaymentPointOfSale.setCancelable(false);

        dlgPaymentPointOfSale.show();


    }
}
