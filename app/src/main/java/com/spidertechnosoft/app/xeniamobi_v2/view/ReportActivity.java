package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;

import vpos.apipackage.PosApiHelper;

public class ReportActivity extends AppCompatActivity {


    private Context mContext;

    CardView cvDailySummary;

    CardView cvItemSummary;

    CardView cvCategorySummary;

    CardView cvWaiterSummary;

    CardView cvSaleSummary;


    ProgressDialog dlgProgress;

    PosApiHelper posApiHelper = PosApiHelper.getInstance();

    int ret = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        this.mContext=getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();

    }

    @Override
    public void onBackPressed() {

        this.finish();

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

    public void configView(){

        cvDailySummary=(CardView) findViewById(R.id.cvDailySummary);

        cvItemSummary=(CardView) findViewById(R.id.cvItemSummary);

        cvCategorySummary=(CardView)findViewById(R.id.cvCategorySummary);

        cvWaiterSummary=(CardView)findViewById(R.id.cvWaiterSummary);

        cvSaleSummary=(CardView)findViewById(R.id.cvSaleSummary);


        cvDailySummary.setOnClickListener(onDashMenuClick);

        cvItemSummary.setOnClickListener(onDashMenuClick);

        cvCategorySummary.setOnClickListener(onDashMenuClick);

        cvWaiterSummary.setOnClickListener(onDashMenuClick);

        cvSaleSummary.setOnClickListener(onDashMenuClick);

    }


    View.OnClickListener onDashMenuClick = new View.OnClickListener() {
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.cvDailySummary:
                    Intent intentUserList = new Intent(getApplicationContext(),DailySummaryActivity.class);
                    startActivity(intentUserList);
                    break;
                case R.id.cvItemSummary:
                    Intent intentItemSummary = new Intent(getApplicationContext(),ItemSummaryActivity.class);
                    startActivity(intentItemSummary);
                    break;
                case R.id.cvCategorySummary:
                    Intent intentCategory = new Intent(getApplicationContext(),CategorySummaryActivity.class);
                    startActivity(intentCategory);
                    break;
                case R.id.cvWaiterSummary:
                    Intent intentBill = new Intent(getApplicationContext(),StaffSummaryActivity.class);
                    startActivity(intentBill);
                    break;
                case R.id.cvSaleSummary:
                    Intent intentSaleReport = new Intent(getApplicationContext(),SaleSummaryActivity.class);
                    startActivity(intentSaleReport);
                    break;


            }

        }
    };

}
