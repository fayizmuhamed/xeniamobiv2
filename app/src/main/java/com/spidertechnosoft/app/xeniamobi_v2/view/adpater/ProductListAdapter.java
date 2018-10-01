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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.view.callback.CartOperationCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class ProductListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<Product> originalData = null;
    private List<Product> filteredData = null;
    private LayoutInflater mInflater;

    private ItemFilter mFilter = new ItemFilter();

    private CartOperationCallback mCartOperationCallback;

    public ProductListAdapter(@NonNull Context context, @NonNull List<Product> data, CartOperationCallback mCartOperationCallback) {
        this.context=context;

        this.mCartOperationCallback=mCartOperationCallback;

        this.originalData=data;
        this.filteredData=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_product_list_item, parent, false);

        ImageView imgProductImage=(ImageView)rowView.findViewById(R.id.imgProductImage);

        TextView lblProductName=(TextView) rowView.findViewById(R.id.lblProductName);

        TextView lblProductPrice=(TextView) rowView.findViewById(R.id.lblProductPrice);

        final TextView lblProductOrderedQty=(TextView) rowView.findViewById(R.id.lblProductOrderedQty);

        final ImageView btnProductAddToCart=(ImageView) rowView.findViewById(R.id.btnProductAddToCart);

        final ImageView btnProductReduceFromCart=(ImageView) rowView.findViewById(R.id.btnProductReduceFromCart);

        imgProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product=filteredData.get(position);

               /* Intent intent = new Intent(context.getApplicationContext(),ProductDetailsActivity.class);

                intent.putExtra(PlaceOrderActivity.TYPE,priceType);

                intent.putExtra(PlaceOrderActivity.PRODUCT,product);
                context.startActivity(intent);*/
            }
        });

        btnProductAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Product product=filteredData.get(position);

                String qnty = lblProductOrderedQty.getText().toString();

                Double qty = Double.parseDouble((qnty == null || qnty.equals("")) ? "0" : qnty);

                qty=qty+1;

                Integer cartQty=qty.intValue();


                //mCartOperationCallback.increaseCartItem(product);

                //Integer cartQty=mCartOperationCallback.getCartQty(product);

                lblProductOrderedQty.setText(cartQty.toString());

                if(cartQty>0){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }
            }
        });

        btnProductReduceFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Product product=filteredData.get(position);

                String qnty = lblProductOrderedQty.getText().toString();

                Double qty = Double.parseDouble((qnty == null || qnty.equals("")) ? "0" : qnty);

                qty=qty<=0?0:qty-1;

                Integer cartQty=qty.intValue();

                //mCartOperationCallback.reduceAndRemoveFromCart(product);

                //Integer cartQty=mCartOperationCallback.getCartQty(product);

                lblProductOrderedQty.setText(cartQty.toString());

                if(cartQty>0){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }
            }
        });

        lblProductOrderedQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Product product=filteredData.get(position);

                String qnty = lblProductOrderedQty.getText().toString();

                Double qty = Double.parseDouble((qnty == null || qnty.equals("")) ? "0" : qnty);

                mCartOperationCallback.changeQuantity(product,qty);

                if(qty>0){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }
            }
        });

        lblProductOrderedQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.setFocusable(false);
                    v.setFocusableInTouchMode(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    //do here your stuff f
                    return true;
                }
                return false;
            }
        });


       /* lblProductOrderedQty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });*/
        lblProductOrderedQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lblProductOrderedQty.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(lblProductOrderedQty, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        lblProductOrderedQty.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                return false;
            }
        });

        btnProductAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product=filteredData.get(position);

                mCartOperationCallback.increaseCartItem(product);

                Integer cartQty=mCartOperationCallback.getCartQty(product);

                lblProductOrderedQty.setText(cartQty.toString());

                if(cartQty>0){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }
            }
        });

        btnProductReduceFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product=filteredData.get(position);

                mCartOperationCallback.reduceAndRemoveFromCart(product);

                Integer cartQty=mCartOperationCallback.getCartQty(product);

                lblProductOrderedQty.setText(cartQty.toString());

                if(cartQty>0){

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(true);
                }else{

                    btnProductAddToCart.setEnabled(true);
                    btnProductReduceFromCart.setEnabled(false);
                }
            }
        });

        Product product=filteredData.get(position);

        if(product!=null){
           /* Picasso.with(context)
                    .load(GeneralMethods.getImageUrl(product.getImage()))
                    .placeholder(R.color.empty_image_bg)
                    .error(R.color.empty_image_bg)
                    .fit()
                    .into(imgProductImage);*/
//            if(product.getProductImage()==null){
//
//                imgProductImage.setBackgroundResource(R.color.empty_image_bg);
//            }else{
//
//
//            }

            lblProductName.setText(product.getName());

            Double price=getProductPrice(product);

            lblProductPrice.setText(price+" INR");

            Integer cartQty=mCartOperationCallback.getCartQty(product);

            lblProductOrderedQty.setText(cartQty.toString());

            if(cartQty>0){

                btnProductAddToCart.setEnabled(true);
                btnProductReduceFromCart.setEnabled(true);
            }else{

                btnProductAddToCart.setEnabled(true);
                btnProductReduceFromCart.setEnabled(false);
            }




        }

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Product> list = originalData;

            int count = list.size();
            final ArrayList<Product> nList = new ArrayList<Product>(count);

            Product filterableProduct ;

            for (int i = 0; i < count; i++) {
                filterableProduct = list.get(i);
                if (filterableProduct.getName()!=null&&filterableProduct.getName().toLowerCase().contains(filterString)) {
                    nList.add(filterableProduct);
                }
            }

            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }

    }

    public Double getProductPrice(Product product){

        return product.getRate();
    }
}
