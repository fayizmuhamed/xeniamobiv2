package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.OrmTransactionHelper;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.SaleLineItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.UUID;


public class BillCreationActivity extends AppCompatActivity implements
        ProductFragment.OnFragmentInteractionListener,
        BillDetailFragment.OnFragmentInteractionListener,
        CashDeskFragment.OnFragmentInteractionListener,
        StaffFragment.OnFragmentInteractionListener,
        CartOperationCallback {

    private final static int DISABLE_RG = 11;

    private final static int ENABLE_RG = 10;

    int IsWorking = 0;

    ProductService productService=new ProductService();
    SaleService saleService=new SaleService();
    UserService userService=new UserService();

    private Sale sale;

    public static final String SALE = "SALE";

    public static final String PRODUCT = "PRODUCT";

    public static final String CART_ITEM = "CART_ITEM";

    public static final String CART = "CART";

    private RelativeLayout mLayBillCreationFooterTotal;
    private RelativeLayout mLayBillCreationFooterTotalTax;
    private RelativeLayout mLayBillCreationFooterBalance;

    private TextView mTextBillCreationFooterTotal;
    private TextView mTextBillCreationFooterTotalTax;
    private TextView mTextBillCreationFooterBalance;


    private RelativeLayout mButtonBillCreationFooterProceed;

    private RelativeLayout mButtonBillCreationFooterSave;

    private RelativeLayout mFlContent;

    private LinearLayout layBillCreationFooter;

    private int mCurrentPage=0;

    //private Database mDatabase;
    SessionManager sessionManager;

    private TextView mTitle;
    private Toolbar mToolbar;


    Context mContext;

    //initialize fragment manager
    FragmentManager fragmentManager;

    //initialize fragment transaction object
    FragmentTransaction fragmentTransaction;

    private Stack<OperationFragments> fragmentStack=new Stack<>();

    AsyncTask<Sale, String, Boolean> mSaleTask;

    Dialog dlgPrintingProgress;


    User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bill_creation, menu);
        if(sale.getSaleUid()==null)
            menu.findItem(R.id.action_cancel).setVisible(false);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_creation);
        this.mContext=getApplicationContext();
        //this.mDatabase= Database.getInstance();
        sessionManager=new SessionManager(mContext);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        PrintUtil.getInstance().initialize(mContext);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActivityTitle(0,R.string.title_activity_product_selection);
        //user=mDatabase.getLoggedInUser();
        user=sessionManager.getLoggedInUser();

        Sale saleIn=(Sale) getIntent().getSerializableExtra(SALE);

        if(saleIn==null){
            saleIn=new Sale();



            /*Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);*/
        }

        sale=saleIn;

        configView();

        setFooterCartTotal();


        if(sale.getSaleUid()==null){

            sale.setUserId(user.getUid());
            sale.setUserName(user.getName());



            if(!user.getType().equals(UserType.STAFF)){

                if(SettingService.isStaffSelectionRequired().equals(IndicatorStatus.YES))
                    goToFragment(null, OperationFragments.STAFF_FRAGMENT);
                else
                    goToFragment(null, OperationFragments.PRODUCT_FRAGMENT);

            }else{


                goToFragment(null, OperationFragments.PRODUCT_FRAGMENT);
            }

        }else{

            List<SaleLineItem> saleLineItemList=sale.getSaleLineItems();

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

                    sessionManager.updateCart(cartItem);
                }
            }

            goToFragment(null, OperationFragments.DETAIL_FRAGMENT);
        }

    }

    private void setActivityTitle(Integer type, Integer resourceId){

        if(type==null||type==0){

            mToolbar.setBackgroundResource(R.color.colorPrimary);

        }else{
            mToolbar.setBackgroundResource(R.drawable.order_action_bar_bg);
        }

        mTitle.setText(getResources().getString(resourceId));
    }

    private void configView(){

        mFlContent=(RelativeLayout)findViewById(R.id.flContent);

        layBillCreationFooter=(LinearLayout)findViewById(R.id.layBillCreationFooter);

        mLayBillCreationFooterTotal=(RelativeLayout)findViewById(R.id.layBillCreationFooterTotal);

        mLayBillCreationFooterTotalTax=(RelativeLayout)findViewById(R.id.layBillCreationFooterTotalTax);

        mLayBillCreationFooterBalance=(RelativeLayout)findViewById(R.id.layBillCreationFooterBalance);

        layBillCreationFooter=(LinearLayout)findViewById(R.id.layBillCreationFooter);

        layBillCreationFooter=(LinearLayout)findViewById(R.id.layBillCreationFooter);


        mTextBillCreationFooterTotal=(TextView) findViewById(R.id.txtBillCreationFooterTotal);

        mTextBillCreationFooterTotalTax=(TextView) findViewById(R.id.txtBillCreationFooterTotalTax);

        mTextBillCreationFooterBalance=(TextView) findViewById(R.id.txtBillCreationFooterBalance);

        mButtonBillCreationFooterProceed=(RelativeLayout) findViewById(R.id.btnBillCreationFooterProceed);

        mButtonBillCreationFooterSave=(RelativeLayout) findViewById(R.id.btnBillCreationFooterSave);


        mButtonBillCreationFooterProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragmentStack.lastElement().equals(OperationFragments.STAFF_FRAGMENT)){

                    //goToFragment(null,PlaceOrderFragments.DETAIL_FRAGMENT);

                    if(validateStaffSelection()){

                        goToFragment(null, OperationFragments.PRODUCT_FRAGMENT);
                    }

                }else if(fragmentStack.lastElement().equals(OperationFragments.PRODUCT_FRAGMENT)){

                    //goToFragment(null,PlaceOrderFragments.DETAIL_FRAGMENT);

                    if(validateCart()){

                        goToFragment(null, OperationFragments.DETAIL_FRAGMENT);
                    }

                }else if (fragmentStack.lastElement().equals(OperationFragments.DETAIL_FRAGMENT)){

                    //goToFragment(null,PlaceOrderFragments.DETAIL_FRAGMENT);

                    if(validateCart()){

                        goToFragment(null, OperationFragments.CASH_DESK);
                    }

                }

            }
        });

        mButtonBillCreationFooterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SettingService.isStaffAuthenticationRequired().equals(IndicatorStatus.YES)){

                    showAuthenticationDialog();

                }else {
                    postOrder();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the state of item position
        outState.putSerializable(SALE, sale);

    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read the state of item position
        sale =(Sale) savedInstanceState.getSerializable(SALE);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        setFooterCartTotal();
    }

    @Override
    public void onBackPressed() {

        //pop last one from stack
        fragmentStack.pop();

        if (fragmentStack.size() > 0){

            goToFragment(null, fragmentStack.lastElement());

        }else{
            super.onBackPressed();
            this.finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                //pop last one from stack
                fragmentStack.pop();

                if (fragmentStack.size() > 0){

                    goToFragment(null, fragmentStack.lastElement());

                }else{
                    super.onBackPressed();
                }
            case R.id.action_cancel:
                finish();
                //sendFeedback();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    public void goToFragment(Bundle bundle, OperationFragments uri) {

        //Variable to hold fragment
        Fragment fragment;

        //set fragment manager
        fragmentManager=getSupportFragmentManager();


        //get fragment transaction
        fragmentTransaction=fragmentManager.beginTransaction();

        //set animation for change in fragment
        //  fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left);


        if(uri.equals(OperationFragments.STAFF_FRAGMENT)){


            //instantiate dashboard fragment
            fragment = new StaffFragment();

            if(bundle!=null) {

                fragment.setArguments(bundle);
            }
            if (fragment != null) {

                setActivityTitle(0,R.string.title_activity_staff_selection);

                //clearSeatMapFragment();

                //replace content
                fragmentTransaction.replace(R.id.flContent, fragment, OperationFragments.STAFF_FRAGMENT.toString());

                //clear fragment stack
                fragmentStack.clear();

                //push dashboard to stack
                fragmentStack.push(OperationFragments.STAFF_FRAGMENT);

                //commit fragment transaction
                fragmentTransaction.commit();


            }

        }else if(uri.equals(OperationFragments.PRODUCT_FRAGMENT)){


            //instantiate dashboard fragment
            fragment = new ProductFragment();

            if(bundle!=null) {

                fragment.setArguments(bundle);
            }
            if (fragment != null) {

                setActivityTitle(0,R.string.title_activity_product_selection);



                //clearSeatMapFragment();

                //replace content
                fragmentTransaction.replace(R.id.flContent, fragment, OperationFragments.PRODUCT_FRAGMENT.toString());

                //clear fragment stack
                fragmentStack.clear();

                if (!user.getType().equals(UserType.STAFF) ) {

                    if(SettingService.isStaffSelectionRequired().equals(IndicatorStatus.YES))
                        fragmentStack.push(OperationFragments.STAFF_FRAGMENT);
                }

                //push dashboard to stack
                fragmentStack.push(OperationFragments.PRODUCT_FRAGMENT);

                //commit fragment transaction
                fragmentTransaction.commit();


            }

        }else if(uri.equals(OperationFragments.DETAIL_FRAGMENT)) {

            //instantiate inventory check fragment
            fragment = new BillDetailFragment();
            if (bundle != null) {

                fragment.setArguments(bundle);
            }
            if (fragment != null) {

                setActivityTitle(0, R.string.title_fragment_bill_details);

                //clearSeatMapFragment();

                //replace content
                fragmentTransaction.replace(R.id.flContent, fragment, OperationFragments.DETAIL_FRAGMENT.toString());

                //clear fragment stack
                fragmentStack.clear();

                fragmentStack.push(OperationFragments.PRODUCT_FRAGMENT);


                //push dashboard to stack
                fragmentStack.push(OperationFragments.DETAIL_FRAGMENT);

                //commit fragment transaction
                fragmentTransaction.commit();

                layBillCreationFooter.setVisibility(View.VISIBLE);

                mButtonBillCreationFooterProceed.setVisibility(View.VISIBLE);

                mButtonBillCreationFooterSave.setVisibility(View.GONE);
            }

        }else if(uri.equals(OperationFragments.CASH_DESK)) {

           //instantiate inventory check fragment
           fragment = new CashDeskFragment();
           if (bundle != null) {

               fragment.setArguments(bundle);
           }
           if (fragment != null) {

               setActivityTitle(0, R.string.title_fragment_cash_desk);

               //clearSeatMapFragment();

               //replace content
               fragmentTransaction.replace(R.id.flContent, fragment, OperationFragments.CASH_DESK.toString());

               //clear fragment stack
               fragmentStack.clear();

               fragmentStack.push(OperationFragments.PRODUCT_FRAGMENT);

               fragmentStack.push(OperationFragments.DETAIL_FRAGMENT);

               //push dashboard to stack
               fragmentStack.push(OperationFragments.CASH_DESK);

               //commit fragment transaction
               fragmentTransaction.commit();

               layBillCreationFooter.setVisibility(View.VISIBLE);

               mButtonBillCreationFooterProceed.setVisibility(View.GONE);

               mButtonBillCreationFooterSave.setVisibility(View.VISIBLE);
           }

       }
    }

    public void setFooter(OperationFragments uri) {

        if(uri.equals(OperationFragments.PRODUCT_FRAGMENT)){

            layBillCreationFooter.setVisibility(View.VISIBLE);

            mButtonBillCreationFooterProceed.setVisibility(View.VISIBLE);

            mButtonBillCreationFooterSave.setVisibility(View.GONE);

            mLayBillCreationFooterTotal.setVisibility(View.VISIBLE);
            mLayBillCreationFooterTotalTax.setVisibility(View.VISIBLE);
            mLayBillCreationFooterBalance.setVisibility(View.GONE);

        }else if(uri.equals(OperationFragments.DETAIL_FRAGMENT)) {

            layBillCreationFooter.setVisibility(View.VISIBLE);

            mButtonBillCreationFooterProceed.setVisibility(View.VISIBLE);

            mButtonBillCreationFooterSave.setVisibility(View.GONE);
            mLayBillCreationFooterTotal.setVisibility(View.VISIBLE);
            mLayBillCreationFooterTotalTax.setVisibility(View.VISIBLE);
            mLayBillCreationFooterBalance.setVisibility(View.GONE);

        }else if(uri.equals(OperationFragments.CASH_DESK)) {

            layBillCreationFooter.setVisibility(View.VISIBLE);

            mButtonBillCreationFooterProceed.setVisibility(View.GONE);

            mButtonBillCreationFooterSave.setVisibility(View.VISIBLE);

            mLayBillCreationFooterTotal.setVisibility(View.GONE);
            mLayBillCreationFooterTotalTax.setVisibility(View.GONE);
            mLayBillCreationFooterBalance.setVisibility(View.VISIBLE);
        }
    }


    public Sale getSale() {
        return sale;
    }

    @Override
    public boolean changeQuantity(Product product, Double qty) {
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            if(qty<=0){
                return true;
            }

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
        if(qty<=0){
            sessionManager.removeProductFromCart(product);
            setFooterCartTotal();
            return true;
        }

        Integer cartQty = qty.intValue();
        Double rate = cartItem.getItemUnitPrice();
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        sessionManager.updateCart(cartItem);

        setFooterCartTotal();

        return true;
    }

    @Override
    public boolean increaseCartItem(Product product) {

        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

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
        Double rate = cartItem.getItemUnitPrice();
        cartQty = cartQty + 1;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        //mDatabase.updateCart(cartItem);
        sessionManager.updateCart(cartItem);

        setFooterCartTotal();

        // Return true;
        return true;
    }

    @Override
    public boolean reduceFromCart(Product product) {


        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            return false;
        }

        Integer cartQty = cartItem.getItemQty();
        Double rate = cartItem.getItemUnitPrice();
        cartQty = cartQty - 1;
        cartQty=cartQty>0?cartQty:0;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        //mDatabase.updateCart(cartItem);
        sessionManager.updateCart(cartItem);

        setFooterCartTotal();

        // Return true;
        return true;
    }

    @Override
    public boolean reduceAndRemoveFromCart(Product product) {


        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            return true;
        }

        Integer cartQty = cartItem.getItemQty();
        Double rate = cartItem.getItemUnitPrice();
        cartQty = cartQty - 1;
        if(cartQty<=0){
            //mDatabase.removeProductFromCart(product.getUid());
            sessionManager.removeProductFromCart(product);
            setFooterCartTotal();
            return true;
        }
        cartQty=cartQty>0?cartQty:0;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;
        //mDatabase.updateCart(cartItem);
        sessionManager.updateCart(cartItem);

        setFooterCartTotal();

        // Return true;
        return true;
    }

    @Override
    public boolean removeFromCart(Product product) {

        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

        // If the cartContent is null, then just add the list
        if (cartItem == null) {

            return true;
        }

        //mDatabase.removeProductFromCart(product.getUid());
        sessionManager.removeProductFromCart(product);

        setFooterCartTotal();

        return true;
    }

    @Override
    public Integer getCartQty(Product product) {

        //CartItem cartItem = mDatabase.getCartContentForProduct(product.getUid());
        CartItem cartItem = sessionManager.getCartContentForProduct(product);

        return ((cartItem == null||cartItem.getItemQty()==null)?0:cartItem.getItemQty().intValue());

    }

    @Override
    public boolean changeItemPrice(Product product, Double price) {
        return false;
    }

    public void setFooterCartTotal(){

       // List<CartItem> cartItems=mDatabase.getCartContent();
        List<CartItem> cartItems=sessionManager.getCartContent();

        if(cartItems==null){

            mTextBillCreationFooterTotal.setText(GeneralMethods.formatNumber(0.0));

            mTextBillCreationFooterTotalTax.setText(GeneralMethods.formatNumber(0.0));

            mTextBillCreationFooterBalance.setText(GeneralMethods.formatNumber(0.0));

            return;
        }

        Double totalTax=0.00;

        Double total=0.00;

        for(CartItem cartItem:cartItems){

            Double itemPrice=cartItem.getItemUnitPrice();

            Integer itemQty=cartItem.getItemQty();

            Double itemOutPutTax=cartItem.getOutPutTax()==null?0:cartItem.getOutPutTax();

            Double subTotal=(itemPrice* itemQty);

            Double basePrice=0.0;
            Double tax=GeneralMethods.getTaxAmount(subTotal,cartItem.getOutPutTax(),cartItem.getSalesInc());

            if(cartItem.getSalesInc()){

                basePrice=subTotal-tax;

            }else{

                basePrice=subTotal;
            }

            totalTax+=tax;

            total+=basePrice+tax;

        }
        Double discount = sale.getDiscount();

        Double totalAmount=getTotalAmount();

        Double totalTaxAmount=getTotalTaxAmount();

        Double grossAmount=totalAmount+totalTaxAmount;

        Double grandAmount=grossAmount-discount;

        Double netAmount=Double.valueOf(Math.round(grandAmount));

        Double roundOff=netAmount-grandAmount;

        Double cashReceivedAmount = sale.getCashReceived();

        Double cardReceivedAmount = sale.getCardReceived();

        Double returnedAmount = sale.getReturnedAmount();

        Integer paymentType=sale.getPaymentType();

        Double balance =0.0;

        if(paymentType.equals(PaymentType.CASH))
            balance=cashReceivedAmount-netAmount-returnedAmount;
        else if(paymentType.equals(PaymentType.CARD))
            balance=cardReceivedAmount-netAmount-returnedAmount;
        else if(paymentType.equals(PaymentType.CREDIT))
            balance=netAmount-returnedAmount;
        else if(paymentType.equals(PaymentType.BOTH))
            balance=cashReceivedAmount+cardReceivedAmount-netAmount-returnedAmount;

        mTextBillCreationFooterTotalTax.setText(GeneralMethods.formatNumber(totalTax));
        mTextBillCreationFooterTotal.setText(GeneralMethods.formatNumber(total));
        mTextBillCreationFooterBalance.setText(GeneralMethods.formatNumber(balance));
    }


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


    public void showAuthenticationDialog(){

        // Declare the customer dialog
        final Dialog dlgStaffAuthentication = new Dialog(BillCreationActivity.this);

        // 	SEt no title for the dialog
        dlgStaffAuthentication.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Set the content view to the customer_alert layout
        dlgStaffAuthentication.setContentView(R.layout.layout_staff_authentication);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        dlgStaffAuthentication.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView txtStaffName=(TextView) dlgStaffAuthentication.findViewById(R.id.txtStaffName);

        final EditText txtStaffPassword=(EditText) dlgStaffAuthentication.findViewById(R.id.txtStaffPassword);

        Button btnStaffAuthenticationCancel=(Button) dlgStaffAuthentication.findViewById(R.id.btnStaffAuthenticationCancel);

        Button btnStaffAuthenticationDone=(Button) dlgStaffAuthentication.findViewById(R.id.btnStaffAuthenticationDone);

        txtStaffName.setText(sale.getUserName()==null?"":sale.getUserName());

        btnStaffAuthenticationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dlgStaffAuthentication.dismiss();
            }
        });

        btnStaffAuthenticationDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password=txtStaffPassword.getText().toString();

                String userUid=sale.getUserId();

                if(userUid==null||userUid.trim().isEmpty()){

                    Toast.makeText(BillCreationActivity.this,"Invalid User",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(password==null||password.trim().isEmpty()){

                    txtStaffPassword.setError("Please enter password");
                    return ;
                }

                User user=userService.findByUid(userUid);

                if(user==null){

                    Toast.makeText(BillCreationActivity.this,"Invalid User",Toast.LENGTH_SHORT).show();

                    return;
                }

                if(!user.getPassword().equals(password)){

                    txtStaffPassword.setError("Please enter valid credential");

                    return;
                }

                dlgStaffAuthentication.dismiss();

                postOrder();
            }
        });


        // Cancel the dialog when touched outside.
        dlgStaffAuthentication.setCanceledOnTouchOutside(false);

        dlgStaffAuthentication.setCancelable(false);

        dlgStaffAuthentication.show();


    }


    public void postOrder(){

        if(validateCart()) {

            mSaleTask = new AsyncTask<Sale, String, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    // Declare the customer dialog
                    dlgPrintingProgress = new Dialog(BillCreationActivity.this);

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

                    if(sale.getSaleUid()==null){
                        sale.setSaleUid(UUID.randomUUID().toString());
                        sale.setSalesDate(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.SERVER_DATE_TIME_FORMAT));
                    }

                    // List<CartItem> cartItems=mDatabase.getCartContent();
                    List<CartItem> cartItems=sessionManager.getCartContent();

                    List<SaleLineItem> items=new ArrayList<>();
                    Double total=0.0;
                    Double totalTax=0.0;
                    Double discount=sale.getDiscount();


                    for(CartItem cartItem:cartItems){

                        SaleLineItem saleLineItem=new SaleLineItem();
                        saleLineItem.setSaleUid(sale.getSaleUid());
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
                    sale.setItems(items);
                    sale.setTotalAmount(total);
                    sale.setTaxAmount(totalTax);
                    Double grossAmount=total+totalTax;
                    sale.setGrossAmount(grossAmount);
                    Double grandAmount=grossAmount-discount;
                    Double netAmount= Double.valueOf(Math.round(grandAmount));
                    sale.setNetAmount(netAmount);
                    Double roundOff=grandAmount-netAmount;

                    if(sale.getPaymentType().equals(PaymentType.CASH)) {
                        sale.setCardReceived(0.0);
                    }else if(sale.getPaymentType().equals(PaymentType.CARD))
                        sale.setCashReceived(0.0);
                    else if(sale.getPaymentType().equals(PaymentType.CREDIT)) {
                        sale.setCashReceived(0.0);
                        sale.setCardReceived(0.0);
                    }
                    sale.setRoundOff(roundOff);

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
                        PrintUtil.getInstance().printSale(sale);

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
                        sessionManager.clearCart();
                        sale=null;
                        fragmentStack.clear();
                        dlgPrintingProgress.dismiss();
                        finish();

                    } else {

                        dlgPrintingProgress.dismiss();
                    }
                }
            };

            mSaleTask.execute(sale);

        }

    }

    public boolean validateStaffSelection(){

        String staffUid=sale.getUserId();

        return (staffUid==null||staffUid.equals(""))?false:true;
    }

    public boolean validateCart(){

        // List<CartItem> cartItems=mDatabase.getCartContent();
        List<CartItem> cartItems=sessionManager.getCartContent();

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

    public Double getProductPrice(Product product){


        return product.getRate();
    }


    public Double getTotalAmount(){

        // List<CartItem> cartItems=mDatabase.getCartContent();
        List<CartItem> cartItems=sessionManager.getCartContent();

        Double total=0.0;

        for(CartItem cartItem:cartItems){

            Double itemPrice=cartItem.getItemUnitPrice();
            Integer itemQty=cartItem.getItemQty();
            Double grossAmount=(itemPrice* itemQty);
            Double itemDiscount=0.0;
            Double amount=grossAmount-itemDiscount;
            Double taxableAmount=GeneralMethods.getBasePrice(amount,cartItem.getOutPutTax(),cartItem.getSalesInc());

            total=total+taxableAmount;
        }
        return total;
    }

    public Double getTotalTaxAmount(){

        // List<CartItem> cartItems=mDatabase.getCartContent();
        List<CartItem> cartItems=sessionManager.getCartContent();

        Double totalTax=0.0;


        for(CartItem cartItem:cartItems){

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

    public void submitBillCreation(){

        mButtonBillCreationFooterSave.callOnClick();
    }

    /*public void showPaymentDialog(){
        final Dialog dlgCounterDesk = new Dialog(BillCreationActivity.this);

        // SEt no title for the dialog
        dlgCounterDesk.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlgCounterDesk.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                mButtonBillCreationFooterSave.setEnabled(true);
            }
        });

        dlgCounterDesk.setCanceledOnTouchOutside(false);

        // Set the content view to the customer_alert layout
        dlgCounterDesk.setContentView(R.layout.layout_counter_desk_dlg);

        final TextView lblCounterDeskTotalAmount = (TextView) dlgCounterDesk.findViewById(R.id.lblCounterDeskTotalAmount);

        final TextView lblCounterDeskTotalTax = (TextView) dlgCounterDesk.findViewById(R.id.lblCounterDeskTotalTax);

        final TextView lblCounterDeskGrossAmount = (TextView) dlgCounterDesk.findViewById(R.id.lblCounterDeskGrossAmount);

        final EditText txtCounterDeskDiscount = (EditText) dlgCounterDesk.findViewById(R.id.txtCounterDeskDiscount);

        final TextView lblCounterDeskRoundOff = (TextView) dlgCounterDesk.findViewById(R.id.lblCounterDeskRoundOff);

        final TextView lblCounterDeskNetAmount = (TextView) dlgCounterDesk.findViewById(R.id.lblCounterDeskNetAmount);

        final RadioGroup rbgCounterDeskPaymentMode = (RadioGroup) dlgCounterDesk.findViewById(R.id.rbgCounterDeskPaymentMode);

        final EditText txtCounterDeskCashReceived = (EditText) dlgCounterDesk.findViewById(R.id.txtCounterDeskCashReceived);

        final EditText txtCounterDeskCardReceived = (EditText) dlgCounterDesk.findViewById(R.id.txtCounterDeskCardReceived);

        final EditText txtCounterDeskChange = (EditText) dlgCounterDesk.findViewById(R.id.txtCounterDeskChange);

        final TextView txtCounterDeskBalance = (TextView) dlgCounterDesk.findViewById(R.id.txtCounterDeskBalance);

        RelativeLayout btnCounterDeskSave=(RelativeLayout)dlgCounterDesk.findViewById(R.id.btnCounterDeskSave);

        final LinearLayout  layCounterDeskChange=(LinearLayout)dlgCounterDesk.findViewById(R.id.layCounterDeskChange);

        final LinearLayout  layCounterDeskCardReceived=(LinearLayout)dlgCounterDesk.findViewById(R.id.layCounterDeskCardReceived);

        final LinearLayout  layCounterDeskCashReceived=(LinearLayout)dlgCounterDesk.findViewById(R.id.layCounterDeskCashReceived);


        rbgCounterDeskPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.rbtnCounterDeskPaymentModeCash){

                    layCounterDeskCashReceived.setVisibility(View.VISIBLE);
                    layCounterDeskCardReceived.setVisibility(View.GONE);

                    layCounterDeskChange.setVisibility(View.VISIBLE);

                    sale.setCardReceived(0.0);

                    Double totalAmount=getTotalAmount();

                    Double totalTaxAmount=getTotalTaxAmount();

                    Double grossAmount=totalAmount+totalTaxAmount;

                    Double discount=sale.getDiscount();

                    Double grandAmount=grossAmount-discount;

                    Double netAmount=Double.valueOf(Math.round(grandAmount));

                    Double roundOff=grandAmount-netAmount;

                    Double cashReceivedAmount = sale.getCashReceived();

                    Double cardReceivedAmount = sale.getCardReceived();

                    txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

                    Double returnedAmount = sale.getReturnedAmount();

                    Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                    txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));


                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeCard){

                    layCounterDeskCashReceived.setVisibility(View.GONE);
                    layCounterDeskCardReceived.setVisibility(View.VISIBLE);

                    layCounterDeskChange.setVisibility(View.GONE);

                    sale.setCashReceived(0.0);

                    Double totalAmount=getTotalAmount();

                    Double totalTaxAmount=getTotalTaxAmount();

                    Double grossAmount=totalAmount+totalTaxAmount;

                    Double discount=sale.getDiscount();

                    Double grandAmount=grossAmount-discount;

                    Double netAmount=Double.valueOf(Math.round(grandAmount));

                    Double roundOff=grandAmount-netAmount;

                    Double cashReceivedAmount = sale.getCashReceived();

                    txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

                    Double cardReceivedAmount = sale.getCardReceived();

                    Double returnedAmount = sale.getReturnedAmount();

                    Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                    txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeCredit){

                    layCounterDeskCashReceived.setVisibility(View.GONE);
                    layCounterDeskCardReceived.setVisibility(View.GONE);

                    layCounterDeskChange.setVisibility(View.GONE);

                    sale.setCashReceived(0.0);
                    sale.setCardReceived(0.0);
                    sale.setReturnedAmount(0.0);

                    Double totalAmount=getTotalAmount();

                    Double totalTaxAmount=getTotalTaxAmount();

                    Double grossAmount=totalAmount+totalTaxAmount;

                    Double discount=sale.getDiscount();

                    Double grandAmount=grossAmount-discount;

                    Double netAmount=Double.valueOf(Math.round(grandAmount));

                    Double roundOff=grandAmount-netAmount;

                    Double cashReceivedAmount = sale.getCashReceived();

                    txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

                    Double cardReceivedAmount = sale.getCardReceived();

                    txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

                    Double returnedAmount = sale.getReturnedAmount();

                    txtCounterDeskChange.setText(GeneralMethods.formatNumber(returnedAmount));

                    Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                    txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeBoth){

                    layCounterDeskCashReceived.setVisibility(View.VISIBLE);
                    layCounterDeskCardReceived.setVisibility(View.VISIBLE);

                    layCounterDeskChange.setVisibility(View.VISIBLE);

                    Double totalAmount=getTotalAmount();

                    Double totalTaxAmount=getTotalTaxAmount();

                    Double grossAmount=totalAmount+totalTaxAmount;

                    Double discount=sale.getDiscount();

                    Double grandAmount=grossAmount-discount;

                    Double netAmount=Double.valueOf(Math.round(grandAmount));

                    Double roundOff=grandAmount-netAmount;

                    Double cashReceivedAmount = sale.getCashReceived();

                    Double cardReceivedAmount = sale.getCardReceived();

                    Double returnedAmount = sale.getReturnedAmount();

                    Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                    txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));
                }
            }
        });



        txtCounterDeskDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String discount = txtCounterDeskCashReceived.getText().toString();

                Double discountAmount = Double.parseDouble((discount == null || discount.equals("")) ? "0" : discount);

                sale.setDiscount(discountAmount);

                Double totalAmount=getTotalAmount();

                Double totalTaxAmount=getTotalTaxAmount();

                Double grossAmount=totalAmount+totalTaxAmount;

                Double grandAmount=grossAmount-discountAmount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                lblCounterDeskNetAmount.setText(GeneralMethods.formatNumber(netAmount));

                Double roundOff=grandAmount-netAmount;

                lblCounterDeskRoundOff.setText(GeneralMethods.formatNumber(roundOff));

                Double cashReceivedAmount = sale.getCashReceived();

                Double cardReceivedAmount = sale.getCardReceived();

                Double returnedAmount = sale.getReturnedAmount();

                Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

            }
        });

        txtCounterDeskCashReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                sale.setCashReceived(cashReceivedAmount);

                Double totalAmount=getTotalAmount();

                Double totalTaxAmount=getTotalTaxAmount();

                Double grossAmount=totalAmount+totalTaxAmount;

                Double discount=sale.getDiscount();

                Double grandAmount=grossAmount-discount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                Double roundOff=grandAmount-netAmount;

                Double cardReceivedAmount = sale.getCardReceived();

                Double returnedAmount = sale.getReturnedAmount();

                Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

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

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);

                sale.setCardReceived(cardReceivedAmount);

                Double totalAmount=getTotalAmount();

                Double totalTaxAmount=getTotalTaxAmount();

                Double grossAmount=totalAmount+totalTaxAmount;

                Double discount=sale.getDiscount();

                Double grandAmount=grossAmount-discount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                Double roundOff=grandAmount-netAmount;

                Double cashReceivedAmount = sale.getCashReceived();

                Double returnedAmount = sale.getReturnedAmount();

                //txtCounterDeskChange.setText(GeneralMethods.formatNumber(returnedAmount));

                Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

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

                Double returnedAmount = Double.parseDouble((change == null || change.equals("")) ? "0" : change);

                sale.setReturnedAmount(returnedAmount);

                Double totalAmount=getTotalAmount();


                Double totalTaxAmount=getTotalTaxAmount();


                Double grossAmount=totalAmount+totalTaxAmount;


                Double discount=sale.getDiscount();

                Double grandAmount=grossAmount-discount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                Double roundOff=grandAmount-netAmount;

                Double cashReceivedAmount = sale.getCashReceived();

                Double cardReceivedAmount = sale.getCardReceived();

                Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

                txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

            }
        });

        btnCounterDeskSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postOrder();
            }
        });

        Double totalAmount=getTotalAmount();

        lblCounterDeskTotalAmount.setText(GeneralMethods.formatNumber(totalAmount));

        Double totalTaxAmount=getTotalTaxAmount();

        lblCounterDeskTotalTax.setText(GeneralMethods.formatNumber(totalTaxAmount));

        Double grossAmount=totalAmount+totalTaxAmount;

        lblCounterDeskGrossAmount.setText(GeneralMethods.formatNumber(grossAmount));

        Double discount=sale.getDiscount();

        txtCounterDeskDiscount.setText(GeneralMethods.formatNumber(discount));

        Double grandAmount=grossAmount-discount;

        Double netAmount=Double.valueOf(Math.round(grandAmount));

        lblCounterDeskNetAmount.setText(GeneralMethods.formatNumber(netAmount));

        Double roundOff=grandAmount-netAmount;

        lblCounterDeskRoundOff.setText(GeneralMethods.formatNumber(roundOff));

        Double cashReceivedAmount = sale.getCashReceived();

        txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

        Double cardReceivedAmount = sale.getCardReceived();

        txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

        Double returnedAmount = sale.getReturnedAmount();

        txtCounterDeskChange.setText(GeneralMethods.formatNumber(returnedAmount));

        Double balance = cashReceivedAmount+cardReceivedAmount-netAmount -returnedAmount;

        txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));

        if(sale.getPaymentType()==null){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCash);

        }else if(sale.getPaymentType().equals(PaymentType.CASH)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCash);


        }else if(sale.getPaymentType().equals(PaymentType.CARD)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCard);

        }else if(sale.getPaymentType().equals(PaymentType.CREDIT)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCredit);

        }else if(sale.getPaymentType().equals(PaymentType.BOTH)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeBoth);

        }

        // Show the dialog
        dlgCounterDesk.show();
    }*/



}
