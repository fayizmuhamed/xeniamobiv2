package com.spidertechnosoft.app.xeniamobi_v2.view.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class ItemListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<Product> originalData = null;
    private List<Product> filteredData = null;
    private LayoutInflater mInflater;

    private ItemFilter mFilter = new ItemFilter();

    private OperationCallback mOperationCallback;

    public ItemListAdapter(@NonNull Context context, @NonNull List<Product> data, OperationCallback mOperationCallback) {
        this.context=context;

        this.mOperationCallback=mOperationCallback;

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

        View rowView = mInflater.inflate(R.layout.lay_items_list_item, parent, false);

        ImageView imgProductImage=(ImageView)rowView.findViewById(R.id.imgProductImage);

        TextView lblProductName=(TextView) rowView.findViewById(R.id.lblProductName);

        TextView lblProductPrice=(TextView) rowView.findViewById(R.id.lblProductPrice);

        final ImageView btnItemEdit=(ImageView) rowView.findViewById(R.id.btnItemEdit);

        final ImageView btnItemDelete=(ImageView) rowView.findViewById(R.id.btnItemDelete);




        final Product product=filteredData.get(position);

        if(product!=null){
           /* Picasso.with(context)
                    .load(GeneralMethods.getImageUrl(product.getImage()))
                    .placeholder(R.color.empty_image_bg)
                    .error(R.color.empty_image_bg)
                    .fit()
                    .into(imgProductImage);*/

            lblProductName.setText(product.getName());

            Double price=getProductPrice(product);

            lblProductPrice.setText(price+" INR");

            btnItemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOperationCallback.onEditClick(product);
                }
            });

            btnItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOperationCallback.onDeleteClick(product);
                }
            });

        }

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    public interface OperationCallback {

        void onEditClick(Product product);
        void onDeleteClick(Product product);
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
