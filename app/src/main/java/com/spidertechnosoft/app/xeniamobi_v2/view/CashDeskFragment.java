package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CashDeskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CashDeskFragment extends Fragment {

    private Context mContext;
    private Sale mSale;
    List<CartItem> cartItems=new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private View mRootView;

    private LayoutInflater mCurrentInflater;

    //Database mDatabase;
    SessionManager sessionManager;

    TextView lblCounterDeskTotalAmount ;

    TextView lblCounterDeskTotalTax;

    TextView lblCounterDeskGrossAmount ;

    EditText txtCounterDeskDiscount ;

    TextView lblCounterDeskRoundOff ;

    TextView lblCounterDeskNetAmount ;

    RadioGroup rbgCounterDeskPaymentMode ;

    EditText txtCounterDeskCashReceived ;

    EditText txtCounterDeskCardReceived ;

    EditText txtCounterDeskChange ;

    TextView txtCounterDeskBalance ;

    LinearLayout layCounterDeskChange;

    LinearLayout  layCounterDeskCardReceived;

    LinearLayout  layCounterDeskCashReceived;



    public CashDeskFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CashDeskFragment newInstance() {
        CashDeskFragment fragment = new CashDeskFragment();

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

        this.mRootView = inflater.inflate(R.layout.fragment_cash_desk, container, false);

        mListener.setFooter(OperationFragments.CASH_DESK);

        configView();

        mListener.setFooterCartTotal();

        return mRootView;
    }

    public void configView(){

        lblCounterDeskTotalAmount = (TextView) mRootView.findViewById(R.id.lblCounterDeskTotalAmount);

        lblCounterDeskTotalTax = (TextView) mRootView.findViewById(R.id.lblCounterDeskTotalTax);

        lblCounterDeskGrossAmount = (TextView) mRootView.findViewById(R.id.lblCounterDeskGrossAmount);

        txtCounterDeskDiscount = (EditText) mRootView.findViewById(R.id.txtCounterDeskDiscount);

        lblCounterDeskRoundOff = (TextView) mRootView.findViewById(R.id.lblCounterDeskRoundOff);

        lblCounterDeskNetAmount = (TextView) mRootView.findViewById(R.id.lblCounterDeskNetAmount);

        rbgCounterDeskPaymentMode = (RadioGroup) mRootView.findViewById(R.id.rbgCounterDeskPaymentMode);

        txtCounterDeskCashReceived = (EditText) mRootView.findViewById(R.id.txtCounterDeskCashReceived);

        txtCounterDeskCardReceived = (EditText) mRootView.findViewById(R.id.txtCounterDeskCardReceived);

        txtCounterDeskChange = (EditText) mRootView.findViewById(R.id.txtCounterDeskChange);

        // txtCounterDeskBalance = (TextView) mRootView.findViewById(R.id.txtCounterDeskBalance);


        layCounterDeskChange=(LinearLayout)mRootView.findViewById(R.id.layCounterDeskChange);

        layCounterDeskCardReceived=(LinearLayout)mRootView.findViewById(R.id.layCounterDeskCardReceived);

        layCounterDeskCashReceived=(LinearLayout)mRootView.findViewById(R.id.layCounterDeskCashReceived);

        rbgCounterDeskPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.rbtnCounterDeskPaymentModeCash){

                    ((BillCreationActivity)mContext).getSale().setPaymentType(PaymentType.CASH);

                    layCounterDeskCashReceived.setVisibility(View.VISIBLE);

                    layCounterDeskCardReceived.setVisibility(View.GONE);

                    layCounterDeskChange.setVisibility(View.VISIBLE);

                    if(((BillCreationActivity)mContext).getSale().getSaleUid()==null) {

                        txtCounterDeskCardReceived.setText("0.00");
                    }else {
                        Double cardReceivedAmount = ((BillCreationActivity)mContext).getSale().getCardReceived ();
                        txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));
                    }

                    /*String net = lblCounterDeskNetAmount.getText().toString();

                    Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : net);

                    String cashReceived = txtCounterDeskCashReceived.getText().toString();

                    Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                    if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){

                        if(cashReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));

                    }*/
                    txtCounterDeskCashReceived.setOnEditorActionListener(onEditorActionListener);


                    mListener.setFooterCartTotal();



                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeCard){

                    ((BillCreationActivity)mContext).getSale().setPaymentType(PaymentType.CARD);

                    txtCounterDeskCardReceived.setEnabled(false);

                    layCounterDeskCardReceived.setVisibility(View.VISIBLE);

                    layCounterDeskCashReceived.setVisibility(View.GONE);

                    layCounterDeskChange.setVisibility(View.GONE);

                    txtCounterDeskCashReceived.setText("0.00");

                    String net = lblCounterDeskNetAmount.getText().toString();

                    Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : net);

                    txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(netAmount));

                    txtCounterDeskChange.setText("0.00");

                    /*String cardReceived = txtCounterDeskCardReceived.getText().toString();

                    Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);



                    if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){

                        if(cardReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cardReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));

                    }*/
                    txtCounterDeskCashReceived.setOnEditorActionListener(null);

                    txtCounterDeskCardReceived.setOnEditorActionListener(onEditorActionListener);


                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeCredit){

                    ((BillCreationActivity)mContext).getSale().setPaymentType(PaymentType.CREDIT);

                    layCounterDeskCashReceived.setVisibility(View.GONE);

                    layCounterDeskCardReceived.setVisibility(View.GONE);

                    layCounterDeskChange.setVisibility(View.GONE);

                    txtCounterDeskCashReceived.setText("0.00");

                    txtCounterDeskCardReceived.setText("0.00");

                    txtCounterDeskChange.setText("0.00");

                   /* if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){

                        txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));

                    }*/

                }else if(checkedId==R.id.rbtnCounterDeskPaymentModeBoth){

                    ((BillCreationActivity)mContext).getSale().setPaymentType(PaymentType.BOTH);

                    txtCounterDeskCardReceived.setEnabled(true);

                    layCounterDeskCardReceived.setVisibility(View.VISIBLE);

                    layCounterDeskCashReceived.setVisibility(View.VISIBLE);

                    layCounterDeskChange.setVisibility(View.VISIBLE);

                    /*String net = lblCounterDeskNetAmount.getText().toString();

                    Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : net);

                    Integer paymentType=((BillCreationActivity)mContext).getSale().getPaymentType();

                    String cashReceived = txtCounterDeskCashReceived.getText().toString();

                    Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                    String cardReceived = txtCounterDeskCardReceived.getText().toString();

                    Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);

                    if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){
                        if(paymentType.equals(PaymentType.CASH)){
                            if(cashReceivedAmount>0)
                                txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount-netAmount));
                            else
                                txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                        }else if(paymentType.equals(PaymentType.CARD)){
                            txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(netAmount));
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

                    txtCounterDeskCashReceived.setOnEditorActionListener(null);

                    txtCounterDeskCardReceived.setOnEditorActionListener(onEditorActionListener);

                    // txtCounterDeskBalance.setText(GeneralMethods.formatNumber(balance));
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


                String discount = txtCounterDeskDiscount.getText().toString();

                Double discountAmount = Double.parseDouble((discount == null || discount.equals("")) ? "0" : discount);

                ((BillCreationActivity)mContext).getSale().setDiscount (discountAmount);

                //mSale.setDiscount(discountAmount);
                String gross = lblCounterDeskGrossAmount.getText().toString();

                Double grossAmount = Double.parseDouble((gross == null || gross.equals("")) ? "0" : gross);

                Double grandAmount=grossAmount-discountAmount;

                Double netAmount=Double.valueOf(Math.round(grandAmount));

                lblCounterDeskNetAmount.setText(GeneralMethods.formatNumber(netAmount));

                Double roundOff=netAmount-grandAmount;

                lblCounterDeskRoundOff.setText(GeneralMethods.formatNumber(roundOff));

                Integer paymentType=((BillCreationActivity)mContext).getSale().getPaymentType();

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);

                if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){
                    if(paymentType.equals(PaymentType.CASH)){
                        if(cashReceivedAmount>0)
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(cashReceivedAmount-netAmount));
                        else
                            txtCounterDeskChange.setText(GeneralMethods.formatNumber(0));
                    }else if(paymentType.equals(PaymentType.CARD)){
                        txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(netAmount));
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


                mListener.setFooterCartTotal();

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

                String net = lblCounterDeskNetAmount.getText().toString();

                Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : net);

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);

                ((BillCreationActivity)mContext).getSale().setCashReceived (cashReceivedAmount);

                Integer paymentType=((BillCreationActivity)mContext).getSale().getPaymentType();

                if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){
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


                mListener.setFooterCartTotal();


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

                String net = lblCounterDeskNetAmount.getText().toString();

                Double netAmount = Double.parseDouble((net == null || net.equals("")) ? "0" : net);

                String cashReceived = txtCounterDeskCashReceived.getText().toString();

                Double cashReceivedAmount = Double.parseDouble((cashReceived == null || cashReceived.equals("")) ? "0" : cashReceived);

                String cardReceived = txtCounterDeskCardReceived.getText().toString();

                Double cardReceivedAmount = Double.parseDouble((cardReceived == null || cardReceived.equals("")) ? "0" : cardReceived);

                ((BillCreationActivity)mContext).getSale().setCardReceived (cardReceivedAmount);

                Integer paymentType=((BillCreationActivity)mContext).getSale().getPaymentType();

                if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){
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


                mListener.setFooterCartTotal();

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

                ((BillCreationActivity)mContext).getSale().setReturnedAmount (returnedAmount);

                mListener.setFooterCartTotal();


            }
        });

        txtCounterDeskCashReceived.setOnEditorActionListener(onEditorActionListener);

        initializeView();

    }

    TextView.OnEditorActionListener onEditorActionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                mListener.submitBillCreation();
                //do here your stuff f
                return true;
            }
            return false;
        }
    };

    public void initializeView(){

        Double totalAmount=getTotalAmount();

        lblCounterDeskTotalAmount.setText(GeneralMethods.formatNumber(totalAmount));

        Double totalTaxAmount=getTotalTaxAmount();

        lblCounterDeskTotalTax.setText(GeneralMethods.formatNumber(totalTaxAmount));

        Double grossAmount=totalAmount+totalTaxAmount;

        lblCounterDeskGrossAmount.setText(GeneralMethods.formatNumber(grossAmount));

        Double discount=((BillCreationActivity)mContext).getSale().getDiscount ();

        txtCounterDeskDiscount.setText(GeneralMethods.formatNumber(discount));

        Double grandAmount=grossAmount-discount;

        Double netAmount=Double.valueOf(Math.round(grandAmount));

        lblCounterDeskNetAmount.setText(GeneralMethods.formatNumber(netAmount));

        Double roundOff=netAmount-grandAmount;

        lblCounterDeskRoundOff.setText(GeneralMethods.formatNumber(roundOff));

        Double cashReceivedAmount = ((BillCreationActivity)mContext).getSale().getCashReceived ();

        Double cardReceivedAmount = ((BillCreationActivity)mContext).getSale().getCardReceived ();

        Double returnedAmount = ((BillCreationActivity)mContext).getSale().getReturnedAmount ();

        txtCounterDeskCashReceived.setText(GeneralMethods.formatNumber(cashReceivedAmount));

        txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(cardReceivedAmount));

        Integer paymentType=((BillCreationActivity)mContext).getSale().getPaymentType();

        //txtCounterDeskCardReceived.setText(GeneralMethods.formatNumber(netAmount));

        if(((BillCreationActivity)mContext).getSale().getSaleUid()==null){
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

        if(((BillCreationActivity)mContext).getSale().getPaymentType()==null){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCash);

        }else if(((BillCreationActivity)mContext).getSale().getPaymentType().equals(PaymentType.CASH)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCash);


        }else if(((BillCreationActivity)mContext).getSale().getPaymentType().equals(PaymentType.CARD)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCard);

        }else if(((BillCreationActivity)mContext).getSale().getPaymentType().equals(PaymentType.CREDIT)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeCredit);

        }else if(((BillCreationActivity)mContext).getSale().getPaymentType().equals(PaymentType.BOTH)){

            rbgCounterDeskPaymentMode.check(R.id.rbtnCounterDeskPaymentModeBoth);

        }
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
        void submitBillCreation();
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
}
