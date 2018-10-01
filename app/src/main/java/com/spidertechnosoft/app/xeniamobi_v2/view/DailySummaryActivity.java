package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.DailySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;

import java.util.Date;

public class DailySummaryActivity extends AppCompatActivity {

    SaleService saleService=new SaleService();

    DailySummary mDailySummary;

    private Context mContext;

    private Button btnSearch;

    private EditText txtFromDateTime;

    private EditText txtToDateTime;

    String username;

    SessionManager sessionManager;

    LinearLayout layDailySummaryTaxVAT,layDailySummaryTaxSGST,layDailySummaryTaxCGST;

    TextView lblDailySummaryNoOfSales,lblDailySummaryCashTransactions,lblDailySummaryCardTransactions,lblDailySummaryCreditTransactions,lblDailySummaryMixedTransactions,
            lblDailySummaryTotalAmount,lblDailySummaryTaxVAT,lblDailySummaryTaxSGST,lblDailySummaryTaxCGST,lblDailySummaryGrossAmount,lblDailySummaryBillDiscount,lblDailySummaryRoundOff,
            lblDailySummaryNetAmount,lblDailySummaryCashReceived,lblDailySummaryCardReceived,lblDailySummaryTaxSGSTTitle;

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {

        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
        }
    };


    final Handler mHandler = new Handler();
    final Runnable mUpdateDailySummaryList = new Runnable() {

        @Override
        public void run() {
            updateDailySummary();
        }

    };

    ProgressDialog dlgProgress;


    int ret = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary);
        this.mContext=getApplicationContext();
        this.mDailySummary=new DailySummary();
        this.sessionManager=new SessionManager(mContext);
        this.username=sessionManager.getLoggedInUser().getUsername();
        PrintUtil.getInstance().initialize(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();
        clearDailySummary();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sale_report, menu);
        return true;
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
            case R.id.action_print:
                if(mDailySummary!=null) {

                    String fromDateTime=txtFromDateTime.getText().toString();

                    String toDateTime=txtToDateTime.getText().toString();

                    if(fromDateTime==null||fromDateTime.isEmpty()) {
                        return false;
                    }

                    if(toDateTime==null||toDateTime.isEmpty()) {
                        return false;
                    }
                    PrintUtil.getInstance().printDailySummaryReport(fromDateTime,toDateTime,mDailySummary);
                    //printReport(fromDateTime,toDateTime);
                }
                //sendFeedback();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configView(){

        layDailySummaryTaxVAT=(LinearLayout) findViewById(R.id.layDailySummaryTaxVAT);

        layDailySummaryTaxVAT.setVisibility(View.GONE);

        layDailySummaryTaxSGST=(LinearLayout) findViewById(R.id.layDailySummaryTaxSGST);

        layDailySummaryTaxSGST.setVisibility(View.GONE);

        layDailySummaryTaxCGST=(LinearLayout) findViewById(R.id.layDailySummaryTaxCGST);

        layDailySummaryTaxCGST.setVisibility(View.GONE);

        lblDailySummaryNoOfSales=(TextView) findViewById(R.id.lblDailySummaryNoOfSales);

        lblDailySummaryCashTransactions=(TextView) findViewById(R.id.lblDailySummaryCashTransactions);

        lblDailySummaryCardTransactions=(TextView) findViewById(R.id.lblDailySummaryCardTransactions);

        lblDailySummaryCreditTransactions=(TextView) findViewById(R.id.lblDailySummaryCreditTransactions);

        lblDailySummaryMixedTransactions=(TextView) findViewById(R.id.lblDailySummaryMixedTransactions);

        lblDailySummaryTotalAmount=(TextView) findViewById(R.id.lblDailySummaryTotalAmount);

        lblDailySummaryTaxVAT=(TextView) findViewById(R.id.lblDailySummaryTaxVAT);

        lblDailySummaryTaxSGSTTitle=(TextView) findViewById(R.id.lblDailySummaryTaxSGSTTitle);

        lblDailySummaryTaxSGSTTitle.setText("SGST");

        lblDailySummaryTaxSGST=(TextView) findViewById(R.id.lblDailySummaryTaxSGST);

        lblDailySummaryTaxCGST=(TextView) findViewById(R.id.lblDailySummaryTaxCGST);

        lblDailySummaryGrossAmount=(TextView) findViewById(R.id.lblDailySummaryGrossAmount);

        lblDailySummaryBillDiscount=(TextView) findViewById(R.id.lblDailySummaryBillDiscount);

        lblDailySummaryRoundOff=(TextView) findViewById(R.id.lblDailySummaryRoundOff);

        lblDailySummaryNetAmount=(TextView) findViewById(R.id.lblDailySummaryNetAmount);

        lblDailySummaryCashReceived=(TextView) findViewById(R.id.lblDailySummaryCashReceived);

        lblDailySummaryCardReceived=(TextView) findViewById(R.id.lblDailySummaryCardReceived);



        txtFromDateTime=(EditText) findViewById(R.id.txtFromDateTime);

        txtToDateTime=(EditText) findViewById(R.id.txtToDateTime);

        txtFromDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date fromDate=null;

                String fromDateTime=txtFromDateTime.getText().toString();
                if(fromDateTime==null||fromDateTime.isEmpty())
                    fromDate=GeneralMethods.getDayStart();
                else
                    fromDate=GeneralMethods.getDateFromSpecifiedFormatString(fromDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT);


                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {

                                txtFromDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(date,GeneralMethods.DISPLAY_DATE_TIME_FORMAT));
                            }
                        })
                        .setInitialDate(fromDate)
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });

        txtToDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date toDate=null;

                String toDateTime=txtToDateTime.getText().toString();
                if(toDateTime==null||toDateTime.isEmpty())
                    toDate=GeneralMethods.getDayStart();
                else
                    toDate=GeneralMethods.getDateFromSpecifiedFormatString(toDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT);

                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {

                                txtToDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(date,GeneralMethods.DISPLAY_DATE_TIME_FORMAT));
                            }
                        })
                        .setInitialDate(toDate)
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });

        btnSearch=(Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromDateTime=txtFromDateTime.getText().toString();

                String toDateTime=txtToDateTime.getText().toString();

                if(fromDateTime==null||fromDateTime.isEmpty()) {
                    txtFromDateTime.setError("Please choose from date");
                    return;
                }

                if(toDateTime==null||toDateTime.isEmpty()) {
                    txtToDateTime.setError("Please choose to date");
                    return;
                }

                if(GeneralMethods.getDateFromSpecifiedFormatString(fromDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT).after(GeneralMethods.getDateFromSpecifiedFormatString(toDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT))) {
                    txtToDateTime.setError("Please choose valid date");
                }

                String fromDate=GeneralMethods.convertDateFormat(fromDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.SERVER_DATE_TIME_FORMAT);

                String toDate=GeneralMethods.convertDateFormat(toDateTime,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.SERVER_DATE_TIME_FORMAT);

                fetchDailySummaryList(fromDate,toDate);
            }
        });

        txtFromDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(GeneralMethods.getDayStart(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        txtToDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        btnSearch.callOnClick();

    }


    /**
     * Fetch Category List From Database
     */
    protected void fetchDailySummaryList(final String fromDateTime, final String toDateTime) {

        dlgProgress=new ProgressDialog(DailySummaryActivity.this);

        dlgProgress.setMessage("Fetching data...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mDailySummary=saleService.findDailySummary(fromDateTime,toDateTime);


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateCategoryList runnable
                mHandler.post(mUpdateDailySummaryList);
            }
        };
        t.start();
    }


    /**
     * Update View With Category Items
     */
    protected  void updateDailySummary(){

        lblDailySummaryNoOfSales.setText(mDailySummary.getNoOfSales().toString());

        lblDailySummaryCashTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCashTransaction()));

        lblDailySummaryCardTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCardTransaction()));

        lblDailySummaryCreditTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCreditTransaction()));

        lblDailySummaryMixedTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getBothTransaction()));

        lblDailySummaryTotalAmount.setText(GeneralMethods.formatNumber(mDailySummary.getTotalAmount()));

        if(SettingService.getTaxType().equals(TaxType.VAT)){

            layDailySummaryTaxVAT.setVisibility(View.VISIBLE);
            layDailySummaryTaxCGST.setVisibility(View.GONE);
            layDailySummaryTaxSGST.setVisibility(View.GONE);
            lblDailySummaryTaxVAT.setText(GeneralMethods.formatNumber(mDailySummary.getTaxAmount()));
        }else{
            layDailySummaryTaxVAT.setVisibility(View.GONE);
            layDailySummaryTaxCGST.setVisibility(View.VISIBLE);
            layDailySummaryTaxSGST.setVisibility(View.VISIBLE);
            if(SettingService.getLocationType().equals(LocationType.STATE)){
               lblDailySummaryTaxSGSTTitle.setText("SGST");

            }else {
                lblDailySummaryTaxSGSTTitle.setText("UTGST");
            }
            Double taxSplitAmount1=mDailySummary.getTaxAmount()/2;
            Double taxSplitAmount2=mDailySummary.getTaxAmount()/2;
            lblDailySummaryTaxSGST.setText(GeneralMethods.formatNumber(taxSplitAmount1));
            lblDailySummaryTaxCGST.setText(GeneralMethods.formatNumber(taxSplitAmount2));
        }

        lblDailySummaryGrossAmount.setText(GeneralMethods.formatNumber(mDailySummary.getGrossAmount()));

        lblDailySummaryBillDiscount.setText(GeneralMethods.formatNumber(mDailySummary.getBillDiscount()));

        lblDailySummaryRoundOff.setText(GeneralMethods.formatNumber(mDailySummary.getRoundOff()));

        lblDailySummaryNetAmount.setText(GeneralMethods.formatNumber(mDailySummary.getNetAmount()));

        lblDailySummaryCashReceived.setText(GeneralMethods.formatNumber(mDailySummary.getCashReceived()));

        lblDailySummaryCardReceived.setText(GeneralMethods.formatNumber(mDailySummary.getCardReceived()));

        dlgProgress.dismiss();

    }

    protected  void clearDailySummary(){

        lblDailySummaryNoOfSales.setText(mDailySummary.getNoOfSales().toString());

        lblDailySummaryCashTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCashTransaction()));

        lblDailySummaryCardTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCardTransaction()));

        lblDailySummaryCreditTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getCreditTransaction()));

        lblDailySummaryMixedTransactions.setText(GeneralMethods.formatNumber(mDailySummary.getBothTransaction()));

        lblDailySummaryTotalAmount.setText(GeneralMethods.formatNumber(mDailySummary.getTotalAmount()));

        if(SettingService.getTaxType().equals(TaxType.VAT)){

            layDailySummaryTaxVAT.setVisibility(View.VISIBLE);
            layDailySummaryTaxCGST.setVisibility(View.GONE);
            layDailySummaryTaxSGST.setVisibility(View.GONE);
            lblDailySummaryTaxVAT.setText(GeneralMethods.formatNumber(mDailySummary.getTaxAmount()));
        }else{
            layDailySummaryTaxVAT.setVisibility(View.GONE);
            layDailySummaryTaxCGST.setVisibility(View.VISIBLE);
            layDailySummaryTaxSGST.setVisibility(View.VISIBLE);
            if(SettingService.getLocationType().equals(LocationType.STATE)){
                lblDailySummaryTaxSGSTTitle.setText("SGST");

            }else {
                lblDailySummaryTaxSGSTTitle.setText("UTGST");
            }
            Double taxSplitAmount1=mDailySummary.getTaxAmount()/2;
            Double taxSplitAmount2=mDailySummary.getTaxAmount()/2;
            lblDailySummaryTaxSGST.setText(GeneralMethods.formatNumber(taxSplitAmount1));
            lblDailySummaryTaxCGST.setText(GeneralMethods.formatNumber(taxSplitAmount2));
        }

        lblDailySummaryGrossAmount.setText(GeneralMethods.formatNumber(mDailySummary.getGrossAmount()));

        lblDailySummaryBillDiscount.setText(GeneralMethods.formatNumber(mDailySummary.getBillDiscount()));

        lblDailySummaryRoundOff.setText(GeneralMethods.formatNumber(mDailySummary.getRoundOff()));

        lblDailySummaryNetAmount.setText(GeneralMethods.formatNumber(mDailySummary.getNetAmount()));

        lblDailySummaryCashReceived.setText(GeneralMethods.formatNumber(mDailySummary.getCashReceived()));

        lblDailySummaryCardReceived.setText(GeneralMethods.formatNumber(mDailySummary.getCardReceived()));


    }


}
