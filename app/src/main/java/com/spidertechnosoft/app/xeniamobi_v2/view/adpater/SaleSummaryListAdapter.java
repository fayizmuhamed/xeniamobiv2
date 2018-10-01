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
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;

import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class SaleSummaryListAdapter extends BaseAdapter {

    private final Context context;
    private List<Sale> sales = null;
    private LayoutInflater mInflater;

    public SaleSummaryListAdapter(@NonNull Context context, @NonNull List<Sale> data) {
        this.context=context;

        this.sales=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return sales.size();
    }

    public Object getItem(int position) {
        return sales.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_sale_report_list_item, parent, false);


        TextView lblSaleReportItemInvoiceDate=(TextView) rowView.findViewById(R.id.lblSaleReportItemInvoiceDate);

        TextView lblSaleReportItemInvoiceNo=(TextView) rowView.findViewById(R.id.lblSaleReportItemInvoiceNo);

        TextView lblSaleReportItemAmount=(TextView) rowView.findViewById(R.id.lblSaleReportItemAmount);


        final Sale sale=sales.get(position);

        if(sale!=null){

            lblSaleReportItemInvoiceDate.setText(GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT));
            lblSaleReportItemInvoiceNo.setText(sale.getId().toString());
            lblSaleReportItemAmount.setText(GeneralMethods.formatNumber(sale.getNetAmount()));

        }


        return rowView;
    }




}
