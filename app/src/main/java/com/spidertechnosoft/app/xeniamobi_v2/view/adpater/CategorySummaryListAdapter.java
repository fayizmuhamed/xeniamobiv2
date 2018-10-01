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
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;

import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class CategorySummaryListAdapter extends BaseAdapter {

    private final Context context;
    private List<CategorySummary> categorySummaries = null;
    private LayoutInflater mInflater;

    public CategorySummaryListAdapter(@NonNull Context context, @NonNull List<CategorySummary> data) {
        this.context=context;

        this.categorySummaries=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return categorySummaries.size();
    }

    public Object getItem(int position) {
        return categorySummaries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_category_summary_list_item, parent, false);


        TextView lblCategorySummaryName=(TextView) rowView.findViewById(R.id.lblCategorySummaryName);

        TextView lblCategorySummaryQty=(TextView) rowView.findViewById(R.id.lblCategorySummaryQty);

        TextView lblCategorySummaryAmount=(TextView) rowView.findViewById(R.id.lblCategorySummaryAmount);


        final CategorySummary categorySummary=categorySummaries.get(position);

        if(categorySummary!=null){

            lblCategorySummaryName.setText(categorySummary.getCategoryName());
            lblCategorySummaryQty.setText(categorySummary.getCategoryQty().toString());
            lblCategorySummaryAmount.setText(GeneralMethods.formatNumber(categorySummary.getCategoryAmount()));

        }


        return rowView;
    }




}
