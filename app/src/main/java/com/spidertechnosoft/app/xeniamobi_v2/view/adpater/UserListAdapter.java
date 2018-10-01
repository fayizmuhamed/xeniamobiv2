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
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class UserListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<User> originalData = null;
    private List<User> filteredData = null;
    private LayoutInflater mInflater;

    private ItemFilter mFilter = new ItemFilter();

    private OperationCallback mOperationCallback;

    public UserListAdapter(@NonNull Context context, @NonNull List<User> data, OperationCallback mOperationCallback) {
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

        View rowView = mInflater.inflate(R.layout.lay_user_list_item, parent, false);

        ImageView imgUserImage=(ImageView)rowView.findViewById(R.id.imgUserImage);

        TextView lblUserName=(TextView) rowView.findViewById(R.id.lblUserName);

        TextView lblUserType=(TextView) rowView.findViewById(R.id.lblUserType);

        final ImageView btnUserEdit=(ImageView) rowView.findViewById(R.id.btnUserEdit);

        final ImageView btnUserDelete=(ImageView) rowView.findViewById(R.id.btnUserDelete);




        final User user=filteredData.get(position);

        if(user!=null){
           /* Picasso.with(context)
                    .load(GeneralMethods.getImageUrl(product.getImage()))
                    .placeholder(R.color.empty_image_bg)
                    .error(R.color.empty_image_bg)
                    .fit()
                    .into(imgProductImage);*/

            lblUserName.setText(user.getName());

            if(user.getType().equals(UserType.SUPER_ADMIN)){

                lblUserType.setText("SUPER ADMIN");

            }else  if(user.getType().equals(UserType.ADMIN)){
                lblUserType.setText("ADMIN");
            }else  if(user.getType().equals(UserType.USER)){
                lblUserType.setText("USER");
            }else{
                lblUserType.setText("STAFF");
            }


            btnUserEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOperationCallback.onEditClick(user);
                }
            });

            btnUserDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOperationCallback.onDeleteClick(user);
                }
            });

        }

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    public interface OperationCallback {

        void onEditClick(User user);
        void onDeleteClick(User user);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<User> list = originalData;

            int count = list.size();
            final ArrayList<User> nList = new ArrayList<User>(count);

            User filterableUser ;

            for (int i = 0; i < count; i++) {
                filterableUser = list.get(i);
                if (filterableUser.getName()!=null&&filterableUser.getName().toLowerCase().contains(filterString)) {
                    nList.add(filterableUser);
                }
            }

            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
    public Double getProductPrice(Product product){

        return product.getRate();
    }

}
