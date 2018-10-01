package com.spidertechnosoft.app.xeniamobi_v2.view.adpater;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;

import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class POSDetailListAdapter extends ArrayAdapter<CartItem> {

    private final Context context;
    private final List<CartItem> cartItems;
    private String orderType;
    private CartOperationCallback mCartOperationCallback;


    public POSDetailListAdapter(@NonNull Context context, @NonNull List<CartItem> objects, CartOperationCallback mCartOperationCallback) {
        super(context,-1, objects);
        this.context=context;
        this.cartItems=objects;
        this.mCartOperationCallback=mCartOperationCallback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lay_bill_details_list_item, parent, false);

        TextView txtOrderDetailItemName=(TextView) rowView.findViewById(R.id.txtOrderDetailItemName);

        final TextView txtOrderDetailItemQty=(TextView) rowView.findViewById(R.id.txtOrderDetailItemQty);

        final EditText txtOrderDetailItemRate=(EditText) rowView.findViewById(R.id.txtOrderDetailItemRate);

        final TextView txtOrderDetailItemAmount=(TextView) rowView.findViewById(R.id.txtOrderDetailItemAmount);
        final ImageView btnProductAddToCart=(ImageView) rowView.findViewById(R.id.btnProductAddToCart);

        final ImageView btnProductReduceFromCart=(ImageView) rowView.findViewById(R.id.btnProductReduceFromCart);
        final ImageView btnProductRemoveFromCart=(ImageView) rowView.findViewById(R.id.btnProductRemoveFromCart);

        final CartItem cartItem=cartItems.get(position);

        final Product product=cartItem.getProduct();

        txtOrderDetailItemRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String sPrice=s.toString();

                Double price= Double.parseDouble(sPrice==null||sPrice.isEmpty()?"0.0":(sPrice.startsWith(".")?"0.": sPrice));

                cartItem.setItemUnitPrice(price);

                mCartOperationCallback.changeItemPrice(product,price);

                Integer cartQty=mCartOperationCallback.getCartQty(product);

                txtOrderDetailItemQty.setText(cartQty.toString());

                if(cartQty>1){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }

                Double cartItemPrice=cartItem.getItemUnitPrice();

                Double total=cartQty*cartItemPrice;
                txtOrderDetailItemAmount.setText(total.toString());

            }
        });


        btnProductAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mCartOperationCallback.increaseCartItem(product);

                Integer cartQty=mCartOperationCallback.getCartQty(product);

                txtOrderDetailItemQty.setText(cartQty.toString());

                if(cartQty>1){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }

                Double price=cartItem.getItemUnitPrice();

               /* if(orderType.equals(OrderType.DINE_IN_NORMAL)){

                    price=product.getsRate()==null?0.00:product.getsRate();
                }else if(orderType.equals(OrderType.DINE_IN_AC)){
                    price=product.getaCRate()==null?0.00:product.getaCRate();
                }else if(orderType.equals(OrderType.TAKE_AWAY)){
                    price=product.getParcelRate()==null?0.00:product.getParcelRate();
                }else if(orderType.equals(OrderType.DOOR_DELIVERY)){
                    price=product.getParcelRate()==null?0.00:product.getParcelRate();
                }*/


                Double total=cartQty*price;
                txtOrderDetailItemAmount.setText(total.toString());

            }
        });

        btnProductReduceFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCartOperationCallback.reduceFromCart(product);

                Integer cartQty=mCartOperationCallback.getCartQty(product);

                txtOrderDetailItemQty.setText(cartQty.toString());

                if(cartQty>1){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }

                Double price=cartItem.getItemUnitPrice();

                /*if(orderType.equals(OrderType.DINE_IN_NORMAL)){

                    price=product.getsRate()==null?0.00:product.getsRate();
                }else if(orderType.equals(OrderType.DINE_IN_AC)){
                    price=product.getaCRate()==null?0.00:product.getaCRate();
                }else if(orderType.equals(OrderType.TAKE_AWAY)){
                    price=product.getParcelRate()==null?0.00:product.getParcelRate();
                }else if(orderType.equals(OrderType.DOOR_DELIVERY)){
                    price=product.getParcelRate()==null?0.00:product.getParcelRate();
                }*/


                Double total=cartQty*price;
                txtOrderDetailItemAmount.setText(total.toString());
            }
        });

        btnProductRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCartOperationCallback.removeFromCart(product);

            }
        });


       /* if(mOrderDetailCallback.getSelectedItem()!=null){
            if(mOrderDetailCallback.getSelectedItem().getItemId()==cartItem.getItemId()){

            }
        }*/

        if(product!=null){


            txtOrderDetailItemName.setText(cartItem.getItemName());

            Integer qty=cartItem.getItemQty()==null?0:cartItem.getItemQty().intValue();
            Double price=cartItem.getItemUnitPrice();

            /*if(orderType.equals(OrderType.DINE_IN_NORMAL)){

                price=product.getsRate()==null?0.00:product.getsRate();
            }else if(orderType.equals(OrderType.DINE_IN_AC)){
                price=product.getaCRate()==null?0.00:product.getaCRate();
            }else if(orderType.equals(OrderType.TAKE_AWAY)){
                price=product.getParcelRate()==null?0.00:product.getParcelRate();
            }else if(orderType.equals(OrderType.DOOR_DELIVERY)){
                price=product.getParcelRate()==null?0.00:product.getParcelRate();
            }*/


            Double total=qty*price;

            txtOrderDetailItemQty.setText(qty.toString());
            txtOrderDetailItemRate.setText(GeneralMethods.formatNumber(cartItem.getItemUnitPrice()));
            txtOrderDetailItemAmount.setText(GeneralMethods.formatNumber(total));

            if(qty>1){

                btnProductAddToCart.setEnabled(true);
                btnProductReduceFromCart.setEnabled(true);
            }else{

                btnProductAddToCart.setEnabled(true);
                btnProductReduceFromCart.setEnabled(false);
            }

        }

        return rowView;
    }

    public Double getBasePrice(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double basePrice=0.0;

      /*  if(isTaxIncl){
            Double tax=(price*taxPercentage)/(100+taxPercentage);
            basePrice= price-tax;
        }else{
            basePrice=price;
        }*/

        return GeneralMethods.getTwoDigitDecimalFormat(price,2);
    }

    public Double getTaxAmount(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double taxAmount=0.0;

        if(isTaxIncl){
            taxAmount=0.0;// (price*taxPercentage)/(100+taxPercentage);
        }else{
            taxAmount=price*((taxPercentage==null?0.0:taxPercentage)/100);
        }

        return GeneralMethods.getTwoDigitDecimalFormat(taxAmount,2);
    }

    public Double getNetAmount(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double basePrice=0.0;
        Double tax=0.0;

        if(isTaxIncl){
            tax=0.0;//(price*taxPercentage)/(100+taxPercentage);
            basePrice=price;//-tax;
        }else{
            tax=price*((taxPercentage==null?0.0:taxPercentage)/100);
            basePrice=price;
        }

        return GeneralMethods.getTwoDigitDecimalFormat(basePrice+tax,2);
    }
}
