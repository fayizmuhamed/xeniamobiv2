package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterModel;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Setting;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.SettingConfig;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.utils.AidlUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.PrinterModelSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.LocationTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.TaxTypeSpinnerAdapter;

import java.util.ArrayList;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;

    SettingService settingService=new SettingService();

    TaxTypeSpinnerAdapter mTaxTypeSpinnerAdapter;

    LocationTypeSpinnerAdapter mLocationTypeSpinnerAdapter;

    Spinner spnLocationTypes,spnTaxTypes;

    ArrayList<LocationType> mLocationTypes;

    ArrayList<TaxType> mTaxTypes;

    Button btnSave,btnPrinterSetting;

    Switch swStaffSelection,swStaffAuthentication;

    LinearLayout layLocationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Save the context
        this.mContext = getApplicationContext();
        this.mLocationTypes=new ArrayList<LocationType>();
        this.mTaxTypes=new ArrayList<TaxType>();
        configView();
        initializeData();



    }

    public void configView(){
        layLocationType=(LinearLayout)findViewById(R.id.layLocationType);
        swStaffSelection=(Switch)findViewById(R.id.swStaffSelection);
        swStaffAuthentication=(Switch)findViewById(R.id.swStaffAuthentication);
        mLocationTypeSpinnerAdapter = new LocationTypeSpinnerAdapter(this, R.layout.spinner_item, mLocationTypes);
        mLocationTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        mTaxTypeSpinnerAdapter = new TaxTypeSpinnerAdapter(this, R.layout.spinner_item, mTaxTypes);
        mTaxTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spnLocationTypes =(Spinner) findViewById(R.id.spnLocationTypes);
        spnLocationTypes.setAdapter(mLocationTypeSpinnerAdapter);

        spnTaxTypes =(Spinner) findViewById(R.id.spnTaxTypes);
        spnTaxTypes.setAdapter(mTaxTypeSpinnerAdapter);
        spnTaxTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaxType taxType=(TaxType)spnTaxTypes.getSelectedItem();
                if(taxType.equals(TaxType.GST))
                    layLocationType.setVisibility(View.VISIBLE);
                else
                    layLocationType.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPrinterSetting=(Button)findViewById(R.id.btnPrinterSetting);

        btnPrinterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),PrinterSettingActivity.class);
                startActivity(intent);
            }
        });

        btnSave=(Button)findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSettings();
            }
        });

    }

    public void initializeDataForEdit(){

        if(SettingService.getTaxType()!=null)
            selectTaxTypeSpinnerItemByValue(spnTaxTypes,SettingService.getTaxType());

        if(SettingService.getLocationType()!=null)
            selectLocationTypeSpinnerItemByValue(spnLocationTypes,SettingService.getLocationType());

        if(SettingService.isStaffSelectionRequired().equals(IndicatorStatus.YES))
            swStaffSelection.setChecked(true);
        else
            swStaffSelection.setChecked(false);

        if(SettingService.isStaffAuthenticationRequired().equals(IndicatorStatus.YES))
            swStaffAuthentication.setChecked(true);
        else
            swStaffAuthentication.setChecked(false);


        btnSave.setText("UPDATE");
    }


    public static void selectLocationTypeSpinnerItemByValue(Spinner spnr, LocationType mLocationType) {

        if(mLocationType==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        LocationTypeSpinnerAdapter adapter = (LocationTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            LocationType locationType=(LocationType) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(locationType.equals(mLocationType)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public static void selectTaxTypeSpinnerItemByValue(Spinner spnr, TaxType mTaxType) {

        if(mTaxType==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        TaxTypeSpinnerAdapter adapter = (TaxTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            TaxType taxType=(TaxType) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(taxType.equals(mTaxType)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public void saveSettings(){

        Integer isStaffAuthentication= swStaffAuthentication.isChecked()?IndicatorStatus.YES:IndicatorStatus.NO;

        Setting setting8 = new Setting();
        setting8.setKey(SettingConfig.IS_STAFF_AUTH_REQUIRED);
        setting8.setValue(String.valueOf(isStaffAuthentication));
        setting8.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting8, true);

        TaxType taxType=(TaxType) spnTaxTypes.getSelectedItem();
        Setting setting9 = new Setting();
        setting9.setKey(SettingConfig.TAX_TYPE);
        setting9.setValue(String.valueOf(taxType));
        setting9.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting9, true);

        LocationType locationType=(LocationType) spnLocationTypes.getSelectedItem();
        Setting setting10 = new Setting();
        setting10.setKey(SettingConfig.LOCATION_TYPE);

        if(taxType.equals(TaxType.VAT))
            locationType=LocationType.STATE;
        setting10.setValue(String.valueOf(locationType));
        setting10.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting10, true);


        Integer isStaffSelection= swStaffSelection.isChecked()?IndicatorStatus.YES:IndicatorStatus.NO;


        Setting setting12 = new Setting();
        setting12.setKey(SettingConfig.IS_STAFF_SELECTION_REQUIRED);
        setting12.setValue(String.valueOf(isStaffSelection));
        setting12.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting12, true);

        finish();
    }


    public void initializeData(){

        mLocationTypes.clear();
        mLocationTypes.add(LocationType.STATE);
        mLocationTypes.add(LocationType.UNION_TERRITORY);

        mLocationTypeSpinnerAdapter.notifyDataSetChanged();

        mTaxTypes.clear();
        mTaxTypes.add(TaxType.VAT);
        mTaxTypes.add(TaxType.GST);
        mTaxTypes.add(TaxType.NON_TAX);

        mTaxTypeSpinnerAdapter.notifyDataSetChanged();


        initializeDataForEdit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeData();
    }

    @Override
    public void onBackPressed() {

        this.finish();

    }

}
