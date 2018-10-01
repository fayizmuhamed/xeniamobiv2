package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.CategoryService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.ProductService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.ProductListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements CartOperationCallback {

    CategoryService categoryService=new CategoryService();

    ProductService productService=new ProductService();

    private Context mContext;

    private View mRootView;

    private LayoutInflater mCurrentInflater;

    private TabLayout mTabCategory;

    private ListView mLvProducts;

    private EditText mEditProductSearch;

    ProgressDialog dlgProgress;

    //private Database mDatabase;
    SessionManager sessionManager;

    List<Product> mProducts;

    List<Category> mCategories;

    private ProductListAdapter mProductListAdapter;


    private OnFragmentInteractionListener mListener;

    private CartOperationCallback mCartOperationCallback;

    final Handler mHandler = new Handler();
    final Runnable mUpdateCategoryList = new Runnable() {

        @Override
        public void run() {
            updateCategoryList();
        }

    };

    final Runnable mUpdateProductList = new Runnable() {

        @Override
        public void run() {
            updateProductList();
        }

    };

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderType Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String orderType, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void configView(){


        mProductListAdapter=new ProductListAdapter(mContext,mProducts,this);

        mTabCategory=(TabLayout)mRootView.findViewById(R.id.tabCategory);

        mLvProducts=(ListView)mRootView.findViewById(R.id.lvProducts);

        mLvProducts.setAdapter(mProductListAdapter);

        mEditProductSearch=(EditText)mRootView.findViewById(R.id.editProductSearch);

        mEditProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(mTabCategory.getSelectedTabPosition()==0){
                    mProductListAdapter.getFilter().filter(s.toString());
                }else{
                    new Handler().postDelayed(
                            new Runnable(){
                                @Override
                                public void run() {
                                    mTabCategory.getTabAt(0).select();
                                }
                            }, 100);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTabCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Category category=(Category)tab.getTag();
                fetchProductList(category);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Fetch Category List From Database
     */
    protected void fetchCategoryList() {

        dlgProgress=new ProgressDialog(this.getActivity());

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

        mTabCategory.removeAllTabs();

        if(mCategories==null||mCategories.isEmpty()){

            dlgProgress.dismiss();
            return;
        }

        Category allCategory=new Category();
        allCategory.setUid(null);
        allCategory.setName("All");

        TabLayout.Tab tabAll=mTabCategory.newTab();
        tabAll.setText(allCategory.getName());
        tabAll.setTag(allCategory);
        mTabCategory.addTab(tabAll);

        for(Category category:mCategories){

            TabLayout.Tab tab=mTabCategory.newTab();
            tab.setText(category.getName());
            tab.setTag(category);
            mTabCategory.addTab(tab);

        }

        mTabCategory.setTabGravity(TabLayout.GRAVITY_FILL);

        mListener.setFooterCartTotal();

        dlgProgress.dismiss();

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

                    mProducts.addAll(productService.findByQuery(query,params,null,null,null));


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
        mProductListAdapter.getFilter().filter(mEditProductSearch.getText().toString());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Save the context
        this.mContext = getActivity();


        // Store the inflater reference
        this.mCurrentInflater = inflater;

        this.mRootView = inflater.inflate(R.layout.fragment_product, container, false);

        //this.mDatabase= Database.getInstance();
        sessionManager=new SessionManager(mContext);

        this.mProducts=new ArrayList<Product>();

        this.mCategories=new ArrayList<Category>();

        mListener.setFooter(OperationFragments.PRODUCT_FRAGMENT);

        configView();

        return mRootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCategoryList();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            Double rate = mListener.getProductPrice(product);
            Double outPutTax=product.getOutPutTax();
            Integer cartQty = 0;
            cartItem.setItemQty(cartQty);
            cartItem.setItemUnitPrice(rate);
            cartItem.setOutPutTax(outPutTax);
            cartItem.setSalesInc(product.getSalesInc());

        }
        if(qty<=0){
            sessionManager.removeProductFromCart(product);
            mListener.setFooterCartTotal();
            return true;
        }

        Integer cartQty = qty.intValue();
        Double rate = cartItem.getItemUnitPrice();
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;

        sessionManager.updateCart(cartItem);

        mListener.setFooterCartTotal();

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
            Double rate = mListener.getProductPrice(product);
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

        mListener.setFooterCartTotal();

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

        mListener.setFooterCartTotal();

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
            mListener.setFooterCartTotal();
            return true;
        }
        cartQty=cartQty>0?cartQty:0;
        cartItem.setItemQty(cartQty);
        Double amount = cartQty*rate;
        //mDatabase.updateCart(cartItem);
        sessionManager.updateCart(cartItem);

        mListener.setFooterCartTotal();

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

        mListener.setFooterCartTotal();

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void setFooterCartTotal();
        Double getProductPrice(Product product);
        void setFooter(OperationFragments uri);

    }
}
