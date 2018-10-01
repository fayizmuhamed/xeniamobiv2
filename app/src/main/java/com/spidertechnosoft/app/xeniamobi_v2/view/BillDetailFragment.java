package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.BillDetailListAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BillDetailFragment extends Fragment implements CartOperationCallback,BillDetailListAdapter.OrderDetailCallback {

    private Context mContext;
    private Sale mSale;
    List<CartItem> cartItems=new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private View mRootView;

    private LayoutInflater mCurrentInflater;

    //Database mDatabase;
    SessionManager sessionManager;

    TextView txtBillDetailBillNo;

    TextView txtBillDetailDate;

    EditText editBillDetailCustomer;

    ListView lvBillDetailsList;

    RelativeLayout btnBillDetailItemAdd;

    RelativeLayout btnBillDetailItemDelete;


    BillDetailListAdapter mBillDetailListAdapter;

    CartItem mCartItemSelected;


    public BillDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BillDetailFragment newInstance() {
        BillDetailFragment fragment = new BillDetailFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Save the context
        this.mContext = getActivity();

       // this.mDatabase=Database.getInstance();
        this.sessionManager=new SessionManager(mContext);

        this.mSale=((BillCreationActivity)mContext).getSale();

        //List<CartItem> cartItemList=mDatabase.getCartContent();
        List<CartItem> cartItemList=sessionManager.getCartContent();
        if(cartItemList!=null){

            cartItems.addAll(cartItemList);

        }

        // Store the inflater reference
        this.mCurrentInflater = inflater;

        this.mRootView = inflater.inflate(R.layout.fragment_bill_detail, container, false);

        mListener.setFooter(OperationFragments.DETAIL_FRAGMENT);

        configView();

        String customerName=((BillCreationActivity)mContext).getSale().getCustomerName();

        editBillDetailCustomer.setText(customerName==null?"":customerName);

        mListener.setFooterCartTotal();

        return mRootView;
    }

    public void configView(){

        txtBillDetailBillNo =(TextView)mRootView.findViewById(R.id.txtBillDetailBillNo);

        txtBillDetailDate =(TextView)mRootView.findViewById(R.id.txtBillDetailDate);

        editBillDetailCustomer =(EditText)mRootView.findViewById(R.id.editBillDetailCustomer);

        btnBillDetailItemAdd =(RelativeLayout)mRootView.findViewById(R.id.btnBillDetailItemAdd);

        btnBillDetailItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.goToFragment(null, OperationFragments.PRODUCT_FRAGMENT);
            }
        });

        editBillDetailCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ((BillCreationActivity)mContext).getSale().setCustomerName (s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnBillDetailItemDelete =(RelativeLayout)mRootView.findViewById(R.id.btnBillDetailItemDelete);

        btnBillDetailItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCartItemSelected==null){

                    return;
                }

                if(removeFromCart(mCartItemSelected.getProduct())){

                    mCartItemSelected=null;
                    lvBillDetailsList.clearChoices();
                    setButton();
                }

            }
        });

        txtBillDetailBillNo.setText((mSale.getSalesNo()==null||mSale.getSalesNo().trim().isEmpty())?"XXXXXXXX":mSale.getSalesNo());


        if(mSale.getSalesDate()==null||mSale.getSalesDate().trim().isEmpty()){

            txtBillDetailDate.setText(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT));
        }else{
            txtBillDetailDate.setText(GeneralMethods.convertDateFormat(mSale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT));
        }


        lvBillDetailsList =(ListView)mRootView.findViewById(R.id.lvBillDetailsList);

        lvBillDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCartItemSelected= mBillDetailListAdapter.getItem(position);

                setButton();
            }
        });

        mBillDetailListAdapter =new BillDetailListAdapter(mContext,cartItems,this,this);

        lvBillDetailsList.setAdapter(mBillDetailListAdapter);

        setButton();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void goToFragment(Bundle bundle, OperationFragments uri);
        void setFooter(OperationFragments uri);
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

        //List<CartItem> cartItemList=mDatabase.getCartContent();
        List<CartItem> cartItemList=sessionManager.getCartContent();
        cartItems.clear();

        if(cartItemList!=null){

            cartItems.addAll(cartItemList);
        }

        mBillDetailListAdapter =new BillDetailListAdapter(mContext,cartItems,this,this);

        lvBillDetailsList.setAdapter(mBillDetailListAdapter);

        mBillDetailListAdapter.notifyDataSetChanged();

        setButton();

        mListener.setFooterCartTotal();

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

        //List<CartItem> cartItemList=mDatabase.getCartContent();
        List<CartItem> cartItemList=sessionManager.getCartContent();

        cartItems.clear();

        if(cartItemList!=null){

            cartItems.addAll(cartItemList);
        }

        mBillDetailListAdapter =new BillDetailListAdapter(mContext,cartItems,this,this);

        lvBillDetailsList.setAdapter(mBillDetailListAdapter);

        mBillDetailListAdapter.notifyDataSetChanged();

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
        Double rate = price;
        cartItem.setItemUnitPrice(rate);
        Double amount = cartQty*rate;

        //mDatabase.updateCart(cartItem);
        sessionManager.updateCart(cartItem);

        mListener.setFooterCartTotal();

        // Return true;
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

    public void setButton(){

        if(lvBillDetailsList.getCheckedItemCount()==0) {
            btnBillDetailItemDelete.setEnabled(false);

        }else {
            btnBillDetailItemDelete.setEnabled(true);
        }
    }


    @Override
    public void setSelectedItem(int position,CartItem cartItem) {

        lvBillDetailsList.setItemChecked(position,true);
        mCartItemSelected=cartItem;

        setButton();
    }
}
