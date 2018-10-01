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
import android.widget.ListView;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.CategorySummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;

public class CategorySummaryActivity extends AppCompatActivity {

    SaleService saleService=new SaleService();

    ArrayList<CategorySummary> mCategorySummaries;

    private Context mContext;

    private Button btnSearch;

    private EditText txtFromDateTime;

    private EditText txtToDateTime;

    String username;

    SessionManager sessionManager;


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

    private ListView mLvCategorySummaryList;

    private CategorySummaryListAdapter mCategorySummaryListAdapter;

    private TextView txtCategorySummaryFooterTotal;

    final Handler mHandler = new Handler();
    final Runnable mUpdateCategorySummaryList = new Runnable() {

        @Override
        public void run() {
            updateCategorySummaryList();
        }

    };

    ProgressDialog dlgProgress;


    int ret = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_summary);
        this.mContext=getApplicationContext();
        this.mCategorySummaries=new ArrayList<>();
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
                if(mCategorySummaries!=null&& mCategorySummaries.size()>0) {

                    String fromDateTime=txtFromDateTime.getText().toString();

                    String toDateTime=txtToDateTime.getText().toString();

                    if(fromDateTime==null||fromDateTime.isEmpty()) {
                        return false;
                    }

                    if(toDateTime==null||toDateTime.isEmpty()) {
                        return false;
                    }
                    PrintUtil.getInstance().printCategorySummaryReport(fromDateTime,toDateTime,mCategorySummaries);
                    //printReport(fromDateTime,toDateTime);
                }
                //sendFeedback();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configView(){

        mCategorySummaryListAdapter=new CategorySummaryListAdapter(mContext,mCategorySummaries);

        mLvCategorySummaryList=(ListView)findViewById(R.id.lvCategorySummaryList);

        mLvCategorySummaryList.setAdapter(mCategorySummaryListAdapter);

        txtCategorySummaryFooterTotal=(TextView)findViewById(R.id.txtCategorySummaryFooterTotal);

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

                fetchCategorySummaryList(fromDate,toDate);
            }
        });

        txtFromDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(GeneralMethods.getDayStart(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        txtToDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        btnSearch.callOnClick();
    }


    /**
     * Fetch Category List From Database
     */
    protected void fetchCategorySummaryList(final String fromDateTime, final String toDateTime) {

        dlgProgress=new ProgressDialog(CategorySummaryActivity.this);

        dlgProgress.setMessage("Fetching data...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mCategorySummaries.clear();

                    mCategorySummaries.addAll(saleService.findCategorySummaryList(fromDateTime,toDateTime));

                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateCategoryList runnable
                mHandler.post(mUpdateCategorySummaryList);
            }
        };
        t.start();
    }

    public void setFooter(){

        Double total=0.0;
        for(CategorySummary categorySummary:mCategorySummaries){
            total+=categorySummary.getCategoryAmount();
        }
        txtCategorySummaryFooterTotal.setText(GeneralMethods.formatNumber(total));
    }

    /**
     * Update View With Category Items
     */
    protected  void updateCategorySummaryList(){

        mCategorySummaryListAdapter.notifyDataSetChanged();

        setFooter();

        dlgProgress.dismiss();

    }


}
