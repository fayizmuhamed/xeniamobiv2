package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SaleService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.SaleSummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;

import vpos.apipackage.PosApiHelper;

public class SaleSummaryActivity extends AppCompatActivity {

    SaleService saleService=new SaleService();

    private Context mContext;

    private Button btnSearch;

    private EditText txtFromDateTime;

    private EditText txtToDateTime;

    String username;

    SessionManager sessionManager;

    SwipeMenuListView mLvSaleSummaryList;

    SwipeMenuCreator mSwipeMenuCreator;

    //private ListView mLvSaleSummaryList;

    private SaleSummaryListAdapter mSaleSummaryListAdapter;

    private TextView txtSaleSummaryFooterTotal;
    private TextView txtSaleSummaryFooterSaleCount;

    ArrayList<Sale> mSales;

    final Handler mHandler = new Handler();
    final Runnable mUpdateSaleItemList = new Runnable() {

        @Override
        public void run() {
            updateSaleItemList();
        }

    };

    ProgressDialog dlgProgress;

    PosApiHelper posApiHelper = PosApiHelper.getInstance();

    int ret = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_summary);
        this.mContext=getApplicationContext();
        this.mSales=new ArrayList<>();
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
                if(mSales!=null&& mSales.size()>0) {

                    String fromDateTime=txtFromDateTime.getText().toString();

                    String toDateTime=txtToDateTime.getText().toString();

                    if(fromDateTime==null||fromDateTime.isEmpty()) {
                        return false;
                    }

                    if(toDateTime==null||toDateTime.isEmpty()) {
                        return false;
                    }

                    PrintUtil.getInstance().printSaleSummaryReport(fromDateTime,toDateTime,mSales);
                    // printSalesReport(fromDateTime,toDateTime);
                }
                //sendFeedback();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configView(){
        mSaleSummaryListAdapter =new SaleSummaryListAdapter(mContext,mSales);

        mLvSaleSummaryList=(SwipeMenuListView) findViewById(R.id.lvSaleSummaryList);

        mSwipeMenuCreator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setWidth(getResources().getInteger(R.integer.order_delete_button_width));
                item1.setTitle(null);
                item1.setBackground(R.color.delete_button_bg);
                item1.setIcon(R.drawable.icon_delete);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setWidth(getResources().getInteger(R.integer.order_delete_button_width));
                item2.setTitle(null);
                item2.setIcon(R.drawable.icon_edit);
                item2.setBackground(R.color.edit_button_bg);
                menu.addMenuItem(item2);

                SwipeMenuItem item3 = new SwipeMenuItem(getApplicationContext());
                item3.setWidth(getResources().getInteger(R.integer.order_delete_button_width));
                item3.setTitle(null);
                item3.setIcon(R.drawable.icon_print);
                item3.setBackground(R.color.feedback_button_bg);
                menu.addMenuItem(item3);
            }
        };

        mLvSaleSummaryList.setAdapter(mSaleSummaryListAdapter);

        //set MenuCreator
        mLvSaleSummaryList.setMenuCreator(mSwipeMenuCreator);

        mLvSaleSummaryList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Sale sale = (Sale)mLvSaleSummaryList.getItemAtPosition(position);//.getItem(position);
                switch (index) {
                    case 0:
                        cancelSale(sale);
                        break;

                    case 1:
                        editSale(sale);
                        break;
                    case 2:
                        printSale(sale);
                        break;
                }
                return false;
            }
        });

        mLvSaleSummaryList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        txtSaleSummaryFooterTotal=(TextView)findViewById(R.id.txtSaleSummaryFooterTotal);

        txtSaleSummaryFooterSaleCount=(TextView)findViewById(R.id.txtSaleSummaryFooterSaleCount);

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

                fetchSaleItemList(fromDate,toDate);
            }
        });


        txtFromDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(GeneralMethods.getDayStart(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        txtToDateTime.setText(GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.DISPLAY_DATE_TIME_FORMAT));

        btnSearch.callOnClick();
    }


    /**
     * Fetch Category List From Database
     */
    protected void fetchSaleItemList(final String fromDateTime, final String toDateTime) {

        dlgProgress=new ProgressDialog(SaleSummaryActivity.this);

        dlgProgress.setMessage("Fetching sales...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mSales.clear();

                    mSales.addAll(saleService.findSaleSummaryList(fromDateTime,toDateTime));

                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateCategoryList runnable
                mHandler.post(mUpdateSaleItemList);
            }
        };
        t.start();
    }

    public void setHeader(){

        Double total=0.0;
        for(Sale sale:mSales){
            total+=sale.getNetAmount();
        }
        txtSaleSummaryFooterTotal.setText(GeneralMethods.formatNumber(total));
        txtSaleSummaryFooterSaleCount.setText(String.valueOf(mSales.size()));
    }

    /**
     * Update View With Category Items
     */
    protected  void updateSaleItemList(){

        mSaleSummaryListAdapter.notifyDataSetChanged();

        setHeader();

        dlgProgress.dismiss();

    }

    public void cancelSale(final Sale sale){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SaleSummaryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SaleSummaryActivity.this);
        }
        builder.setTitle("Delete Sale")
                .setMessage("Are you sure you want to delete ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        deleteSale(sale);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void deleteSale(Sale sale){

        ProgressDialog dialog=new ProgressDialog(SaleSummaryActivity.this);

        dialog.setMessage("Deleting sale...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


        try {

            if(saleService.deleteSale(sale)>0){
                Toast.makeText(getApplicationContext(), "Sale deleted successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Sale delete failed", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            btnSearch.callOnClick();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Sale delete failed", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }

    public void editSale(Sale sale){

        Intent intent = new Intent(getApplicationContext(),PointOfSaleActivity.class);
        sale.setSalesNo(sale.getId().toString());
        intent.putExtra(BillCreationActivity.SALE,sale);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void printSale(Sale sale){

        PrintUtil.getInstance().printSale(sale);
    }

}
