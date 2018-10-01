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
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.ItemSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;

import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class ItemSummaryListAdapter extends BaseAdapter {

    private final Context context;
    private List<ItemSummary> itemSummaries = null;
    private LayoutInflater mInflater;

    public ItemSummaryListAdapter(@NonNull Context context, @NonNull List<ItemSummary> data) {
        this.context=context;

        this.itemSummaries=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return itemSummaries.size();
    }

    public Object getItem(int position) {
        return itemSummaries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_item_summary_list_item, parent, false);


        TextView lblItemSummaryName=(TextView) rowView.findViewById(R.id.lblItemSummaryName);

        TextView lblItemSummaryPrice=(TextView) rowView.findViewById(R.id.lblItemSummaryPrice);

        TextView lblItemSummaryQty=(TextView) rowView.findViewById(R.id.lblItemSummaryQty);

        TextView lblItemSummaryAmount=(TextView) rowView.findViewById(R.id.lblItemSummaryAmount);


        final ItemSummary itemSummary=itemSummaries.get(position);

        if(itemSummary!=null){

            lblItemSummaryName.setText(itemSummary.getItemName());
            lblItemSummaryPrice.setText(GeneralMethods.formatNumber(itemSummary.getItemPrice()));
            lblItemSummaryQty.setText(itemSummary.getItemQty().toString());
            lblItemSummaryAmount.setText(GeneralMethods.formatNumber(itemSummary.getItemAmount()));

        }


        return rowView;
    }




}
