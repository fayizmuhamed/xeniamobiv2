package com.spidertechnosoft.app.xeniamobi_v2.view.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;

import java.util.List;

/**
 * Created by fayiz-ci on 24/2/16.
 */
public class PaymentTypeSpinnerAdapter extends ArrayAdapter<Integer> {
    private final Context context;
    private List<Integer> objects;

    public PaymentTypeSpinnerAdapter(Context context, int textViewResourceId, List<Integer> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int xml_type;



        View rowView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView lblSpinnerText=(TextView)rowView.findViewById(R.id.lblSpinnerText);

        Integer taxType=objects.get(position);

        if(taxType.equals(PaymentType.CASH))
            lblSpinnerText.setText("CASH");
        else if(taxType.equals(PaymentType.CARD))
            lblSpinnerText.setText("CARD");
        else if(taxType.equals(PaymentType.CREDIT))
            lblSpinnerText.setText("CREDIT");
        else if(taxType.equals(PaymentType.BOTH))
            lblSpinnerText.setText("BOTH");

        lblSpinnerText.setTag(taxType);

        return rowView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView lblSpinnerText=(TextView)rowView.findViewById(R.id.lblSpinnerText);

        Integer taxType=objects.get(position);

        if(taxType.equals(PaymentType.CASH))
            lblSpinnerText.setText("CASH");
        else if(taxType.equals(PaymentType.CARD))
            lblSpinnerText.setText("CARD");
        else if(taxType.equals(PaymentType.CREDIT))
            lblSpinnerText.setText("CREDIT");
        else if(taxType.equals(PaymentType.BOTH))
            lblSpinnerText.setText("BOTH");

        lblSpinnerText.setTag(taxType);


        return rowView;
    }
}
