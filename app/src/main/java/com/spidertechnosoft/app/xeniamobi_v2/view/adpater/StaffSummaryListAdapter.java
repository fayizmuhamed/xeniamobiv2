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
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.StaffSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;

import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class StaffSummaryListAdapter extends BaseAdapter {

    private final Context context;
    private List<StaffSummary> waiterSummaries = null;
    private LayoutInflater mInflater;

    public StaffSummaryListAdapter(@NonNull Context context, @NonNull List<StaffSummary> data) {
        this.context=context;

        this.waiterSummaries=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return waiterSummaries.size();
    }

    public Object getItem(int position) {
        return waiterSummaries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_waiter_summary_list_item, parent, false);


        TextView lblWaiterSummaryName=(TextView) rowView.findViewById(R.id.lblWaiterSummaryName);

        TextView lblWaiterSummaryAmount=(TextView) rowView.findViewById(R.id.lblWaiterSummaryAmount);


        final StaffSummary staffSummary =waiterSummaries.get(position);

        if(staffSummary !=null){

            lblWaiterSummaryName.setText(staffSummary.getUserName());
            lblWaiterSummaryAmount.setText(GeneralMethods.formatNumber(staffSummary.getAmount()));

        }


        return rowView;
    }




}
