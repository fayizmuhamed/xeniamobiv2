package com.spidertechnosoft.app.xeniamobi_v2.view.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;

import java.util.List;

/**
 * Created by fayiz-ci on 25/2/16.
 */
public class StaffGridViewAdapter extends BaseAdapter {
    private final Context context;
    private List<User> objects;
    private LayoutInflater mInflater;


    public StaffGridViewAdapter(@NonNull Context context, @NonNull List<User> data) {
        this.context=context;

        this.objects=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return objects.size();
    }

    public Object getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.layout_staff_grid_item, parent, false);


        TextView lblStaffName=(TextView) rowView.findViewById(R.id.lblStaffName);

        final User user=(User)getItem(position);

        if(user!=null){
           /* Picasso.with(context)
                    .load(GeneralMethods.getImageUrl(product.getImage()))
                    .placeholder(R.color.empty_image_bg)
                    .error(R.color.empty_image_bg)
                    .fit()
                    .into(imgProductImage);*/

            lblStaffName.setText(user.getUsername());

        }

        return rowView;
    }


}
