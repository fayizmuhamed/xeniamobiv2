package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rtdriver.driver.Contants;
import com.rtdriver.driver.HsBluetoothPrintDriver;
import com.rtdriver.driver.HsUsbPrintDriver;
import com.rtdriver.driver.HsWifiPrintDriver;
import com.rtdriver.driver.LabelBluetoothPrintDriver;
import com.rtdriver.driver.LabelUsbPrintDriver;
import com.rtdriver.driver.LabelWifiPrintDriver;
import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterConnectionType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterInfo;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterModel;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrintingSize;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Setting;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.SettingConfig;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.utils.AidlUtil;
import com.spidertechnosoft.app.xeniamobi_v2.utils.ToastUtil;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.CustomSimpleSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.PrinterConnectionTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.PrinterModelSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.LocationTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.PrintingSizeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.TaxTypeSpinnerAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.dialog.BluetoothDeviceChooseDialog;
import com.spidertechnosoft.app.xeniamobi_v2.view.dialog.UsbDeviceChooseDialog;
import com.spidertechnosoft.app.xeniamobi_v2.view.dialog.WifiDeviceChooseDialog;
import com.spidertechnosoft.app.xeniamobi_v2.view.interfaces.CustomDialogInterface;
import com.spidertechnosoft.app.xeniamobi_v2.view.receiver.UsbDeviceReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class PrinterSettingActivity extends AppCompatActivity {

    private Context mContext;

    SettingService settingService=new SettingService();

    PrinterModelSpinnerAdapter mPrinterModelSpinnerAdapter;

    PrinterConnectionTypeSpinnerAdapter mPrinterConnectionTypeSpinnerAdapter;

    PrintingSizeSpinnerAdapter mPrintingSizeSpinnerAdapter;

    Spinner spnPrinterModel,spnConnectionType,spnPrintingSize;

    TextInputEditText txtNoOfPrint;

    LinearLayout  layPrintingSize,layConnectionType;
    RelativeLayout layConnectionInfo;

    TextView lblConnectSpinner;

    ArrayList<PrinterModel> mPrinterModels;

    ArrayList<PrinterConnectionType> mPrinterConnectionTypes;

    ArrayList<PrintingSize> mPrintingSizes;

    Button btnSave,btnTestPrint;

    private BluetoothAdapter mBluetoothAdapter;

    UsbDeviceReceiver usbDeviceReceiver;

    private static final int REQUEST_ENABLE_BT = 0xf0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Save the context
        this.mContext = getApplicationContext();
        this.mPrinterModels =new ArrayList<PrinterModel>();
        this.mPrinterConnectionTypes=new ArrayList<>();
        this.mPrintingSizes=new ArrayList<>();
        configView();
        initializeData();



    }

    public void configView(){

        lblConnectSpinner=(TextView)findViewById(R.id.lblConnectSpinner);
        btnTestPrint=(Button)findViewById(R.id.btnTestPrint);

        mPrinterModelSpinnerAdapter = new PrinterModelSpinnerAdapter(this, R.layout.simple_spinner_item, mPrinterModels);
        mPrinterModelSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spnPrinterModel =(Spinner) findViewById(R.id.spnPrinterModel);

        layPrintingSize=(LinearLayout)findViewById(R.id.layPrintingSize);

        txtNoOfPrint=(TextInputEditText)findViewById(R.id.txtNoOfPrint);

        layConnectionType=(LinearLayout)findViewById(R.id.layConnectionType);

        layConnectionInfo=(RelativeLayout)findViewById(R.id.layConnectionInfo);

        mPrinterConnectionTypeSpinnerAdapter = new PrinterConnectionTypeSpinnerAdapter(this, R.layout.simple_spinner_item, mPrinterConnectionTypes);
        mPrinterConnectionTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spnConnectionType =(Spinner) findViewById(R.id.spnConnectionType);
        spnConnectionType.setAdapter(mPrinterConnectionTypeSpinnerAdapter);

        mPrintingSizeSpinnerAdapter = new PrintingSizeSpinnerAdapter(this, R.layout.simple_spinner_item, mPrintingSizes);
        mPrintingSizeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spnPrintingSize =(Spinner) findViewById(R.id.spnPrintingSize);
        spnPrintingSize.setAdapter(mPrintingSizeSpinnerAdapter);

        spnPrinterModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrinterModel printerModel=(PrinterModel)parent.getItemAtPosition(position);
                if(printerModel.equals(PrinterModel.OTHER)){
                    layConnectionType.setVisibility(View.VISIBLE);
                    layConnectionInfo.setVisibility(View.VISIBLE);
                }else{
                    layConnectionType.setVisibility(View.GONE);
                    layConnectionInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnPrinterModel.setAdapter(mPrinterModelSpinnerAdapter);

        spnConnectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrinterConnectionType printerConnectionType=(PrinterConnectionType) parent.getItemAtPosition(position);
                if(printerConnectionType.equals(PrinterConnectionType.BLUETOOTH_PRINTER)){
                    lblConnectSpinner.setHint(R.string.choose_bluetooth_device);
                }else if(printerConnectionType.equals(PrinterConnectionType.USB_PRINTER)){
                    lblConnectSpinner.setHint(R.string.choose_usb_device);
                }else {
                    lblConnectSpinner.setHint(R.string.choose_wifi_device);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lblConnectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrinterConnectionType printerConnectionType=(PrinterConnectionType)spnConnectionType.getSelectedItem();

                if(printerConnectionType.equals(PrinterConnectionType.BLUETOOTH_PRINTER)){
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(mContext, R.string.device_does_not_support_bluetooth,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        return;
                    }
                    showBluetoothDeviceChooseDialog();
                }else if(printerConnectionType.equals(PrinterConnectionType.USB_PRINTER)){
                    showUSBDeviceChooseDialog();
                }else {
                    showWifiDeviceChooseDialog();
                }

            }
        });

        btnSave=(Button)findViewById(R.id.btnSave);
        btnTestPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printSelfTestPage();
            }
        });

        /*btnConnect=(Button)findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                *//*if (RTApplication.getConnState() != Contants.UNCONNECTED) {
                    disconnect();
                    connect();
                } else {
                    connect();
                }*//*
            }
        });*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSettings();
            }
        });

    }

    private void disconnect() {
        /*switch (RTApplication.getConnState()) {
            case Contants.CONNECTED_BY_BLUETOOTH:
                LabelBluetoothPrintDriver.getInstance().stop();
                break;
            case Contants.CONNECTED_BY_USB:
                LabelUsbPrintDriver.getInstance().stop();
                break;
            case Contants.CONNECTED_BY_WIFI:
                LabelWifiPrintDriver.getInstance().stop();
                break;
        }*/
    }

    private void connectBluetooth(BluetoothDevice mBluetoothDevice) {
        HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
        hsBluetoothPrintDriver.start();
        hsBluetoothPrintDriver.connect(mBluetoothDevice);
    }

    public boolean connectUsb(UsbDevice usbDevice) { //isReconnect：是否是重新连接
        boolean bIsSucc=false;
        UsbManager mUsbManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        HsUsbPrintDriver.getInstance().setUsbManager(mUsbManager);
        HsUsbPrintDriver.getInstance().stop();
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0);//
        if ( usbDevice!=null) { //点击按钮，直接连接usbDevice
            if (mUsbManager.hasPermission(usbDevice)) {

                bIsSucc = HsUsbPrintDriver.getInstance().connect(usbDevice, mContext, mPermissionIntent);
            }
            else{
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }

        }

        /*if (!bIsSucc) { //如果连接不成功能，要重新找设备,可能设备id已经变了
            HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            if (deviceList.size() > 0) {
                while (deviceIterator.hasNext()) {// 这里是if不是while，说明我只想支持一种device
                    final UsbDevice device = deviceIterator.next();
                    if (mUsbManager.hasPermission(device)) {
                        if (HsUsbPrintDriver.getInstance().connect(device, mContext, mPermissionIntent)) {
                            bIsSucc = true;
                            break;
                        }
                    } else {
                        mUsbManager.requestPermission(device, mPermissionIntent);
                    }
                }

            }
        }*/
        return  bIsSucc;

    }

    private void registerUsbReceiver() {//不要用此方法
        if (usbDeviceReceiver == null) {
            usbDeviceReceiver = new UsbDeviceReceiver(new UsbDeviceReceiver.CallBack() {
                @Override
                public void onPermissionGranted(UsbDevice usbDevice) {
                    //  connectUsb(usbDevice);
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0);//
                    HsUsbPrintDriver.getInstance().connect(usbDevice, mContext, mPermissionIntent);
                    unregisterReceiver(usbDeviceReceiver);
                }

                @Override
                public void onDeviceAttached(UsbDevice usbDevice) {

                }

                @Override
                public void onDeviceDetached(UsbDevice usbDevice) {

                }
            });
        }
        IntentFilter usbIntentFilter=null;
        if (usbIntentFilter == null) {
            usbIntentFilter = new IntentFilter();
            usbIntentFilter.addAction(UsbDeviceReceiver.ACTION_USB_PERMISSION);
        }
        registerReceiver(usbDeviceReceiver, usbIntentFilter);
    }

    private void connectWifi(String deviceAddress) {
        final String[] address = deviceAddress.trim().split(":");
        //连接wifi
        new Thread(new Runnable() {
            @Override
            public void run() {
                HsWifiPrintDriver hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
                //ip-----address[0],port----address[1]
                hsWifiPrintDriver.WIFISocket(address[0], Integer.valueOf(address[1]));
            }
        }).start();
        //pingIp
        new Thread(new Runnable() {
            @Override
            public void run() {
                HsWifiPrintDriver hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
                boolean isNoCon = true;
                do {
                    isNoCon = hsWifiPrintDriver.IsNoConnection(address[0]);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!isNoCon);
                if (hsWifiPrintDriver.mysocket != null) {
                    try {
                        hsWifiPrintDriver.mysocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hsWifiPrintDriver.mysocket = null;
                }
            }
        }).start();
    }

    public void initializeDataForEdit(){

        if(SettingService.getPrinterModel()!=null)
            selectSpinnerItemByValue(spnPrinterModel,SettingService.getPrinterModel());

        if(SettingService.getPrinterConnectionType()!=null)
            selectPrinterConnectionTypeSpinnerItemByValue(spnConnectionType,SettingService.getPrinterConnectionType());

        if(SettingService.getPrintingSize()!=null)
            selectPrintingSizeSpinnerItemByValue(spnPrintingSize,SettingService.getPrintingSize());

        txtNoOfPrint.setText(SettingService.getNoOfPrint());

        PrinterInfo printerInfo=SettingService.getPrinterConnectionInfo();

        if(printerInfo!=null){

            lblConnectSpinner.setText(printerInfo.getDeviceName());
            lblConnectSpinner.setTag(printerInfo);
        }

        btnSave.setText("UPDATE");
    }


    /**
     * Method to select the sector item by value
     * @param spnr                  : Spinner object
     * @param mPrinterModel     : The header id
     */
    public static void selectSpinnerItemByValue(Spinner spnr, PrinterModel mPrinterModel) {

        if(mPrinterModel ==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        PrinterModelSpinnerAdapter adapter = (PrinterModelSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            PrinterModel printerModel =(PrinterModel) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(printerModel.equals(mPrinterModel)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public static void selectPrinterConnectionTypeSpinnerItemByValue(Spinner spnr, PrinterConnectionType mPrinterConnectionType) {

        if(mPrinterConnectionType ==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        PrinterConnectionTypeSpinnerAdapter adapter = (PrinterConnectionTypeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            PrinterConnectionType printerConnectionType =(PrinterConnectionType) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(printerConnectionType.equals(mPrinterConnectionType)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }

    public static void selectPrintingSizeSpinnerItemByValue(Spinner spnr, PrintingSize mPrintingSize) {

        if(mPrintingSize ==null){

            // Set the selection
            spnr.setSelection(0);

            return;
        }

        // Create the adapter
        PrintingSizeSpinnerAdapter adapter = (PrintingSizeSpinnerAdapter) spnr.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            PrintingSize printingSize =(PrintingSize) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(printingSize.equals(mPrintingSize)) {

                // Set the selection
                spnr.setSelection(position);

                // return control
                return;
            }
        }
    }


    public void saveSettings(){

        String noOfPrint=txtNoOfPrint.getText().toString();

        if(noOfPrint==null||noOfPrint.isEmpty()){
            txtNoOfPrint.setError("Please enter no of prints");
            return;
        }else if(Integer.parseInt(noOfPrint)<1){
            txtNoOfPrint.setError("Please enter value which is higher than 1");
            return;
        }

        PrinterModel printerModel =(PrinterModel) spnPrinterModel.getSelectedItem();
        PrinterInfo printerInfo=(PrinterInfo)lblConnectSpinner.getTag();

        if(printerModel.equals(PrinterModel.OTHER)){
            if(printerInfo==null){
                lblConnectSpinner.setError("Please select printer device");
                return;
            }
        }

        Setting setting1 = new Setting();
        setting1.setKey(SettingConfig.PRINTER_MODEL);
        setting1.setValue(String.valueOf(printerModel));
        setting1.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting1, true);

        Setting setting2 = new Setting();
        setting2.setKey(SettingConfig.PRINTER_CONNECTION_TYPE);
        if(printerModel.equals(PrinterModel.OTHER)) {
            PrinterConnectionType printerConnectionType =(PrinterConnectionType) spnConnectionType.getSelectedItem();
            setting2.setValue(String.valueOf(printerConnectionType));
        }else
            setting2.setValue(null);
        setting2.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting2, true);

        Setting setting3 = new Setting();
        setting3.setKey(SettingConfig.PRINTER_CONNECTION_INFO);
        setting3.setValue(new Gson().toJson(printerInfo));
        setting3.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting3, true);

        PrintingSize printingSize=(PrintingSize)spnPrintingSize.getSelectedItem();
        Setting setting4 = new Setting();
        setting4.setKey(SettingConfig.PRINTING_SIZE);
        setting4.setValue(String.valueOf(printingSize));
        setting4.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting4, true);


        Setting setting5 = new Setting();
        setting5.setKey(SettingConfig.NO_OF_PRINT);
        setting5.setValue(String.valueOf(noOfPrint));
        setting5.setUpdatedAt(new Date().getTime());
        settingService.saveSetting(setting5, true);

        if(!printerModel.equals(PrinterModel.SUNMI_V1)){
            AidlUtil.getInstance().disconnectPrinterService(mContext);
        }

        finish();
    }


    public void initializeData(){


        mPrinterModels.clear();
        mPrinterModels.add(PrinterModel.OTHER);
        mPrinterModels.add(PrinterModel.CIONTEK);
        mPrinterModels.add(PrinterModel.SUNMI_V1);

        mPrinterModelSpinnerAdapter.notifyDataSetChanged();

        mPrinterConnectionTypes.clear();/*
        mPrinterConnectionTypes.add(PrinterConnectionType.BLUETOOTH_PRINTER);*/
        mPrinterConnectionTypes.add(PrinterConnectionType.USB_PRINTER);/*
        mPrinterConnectionTypes.add(PrinterConnectionType.WIFI_PRINTER);*/

        mPrinterConnectionTypeSpinnerAdapter.notifyDataSetChanged();

        mPrintingSizes.clear();
        mPrintingSizes.add(PrintingSize.FIFTY_EIGHT);
        mPrintingSizes.add(PrintingSize.EIGHTY);

        mPrintingSizeSpinnerAdapter.notifyDataSetChanged();

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


    private void showBluetoothDeviceChooseDialog() {


        BluetoothDeviceChooseDialog bluetoothDeviceChooseDialog = new BluetoothDeviceChooseDialog();
        bluetoothDeviceChooseDialog.setOnDeviceItemClickListener(new BluetoothDeviceChooseDialog.onDeviceItemClickListener() {
            @Override
            public void onDeviceItemClick(BluetoothDevice device) {
                connectBluetooth(device);
                if (TextUtils.isEmpty(device.getName())) {
                    lblConnectSpinner.setText(device.getAddress());
                } else {
                    lblConnectSpinner.setText(device.getName());
                }
                //mBluetoothDevice = device;
            }
        });
        bluetoothDeviceChooseDialog.show(getFragmentManager(), null);
    }

    private void showUSBDeviceChooseDialog() {

       /* if (!mUsbRegistered) {
            //  registerUsbReceiver();
            mUsbRegistered = true;
        }*/
        final UsbDeviceChooseDialog usbDeviceChooseDialog = new UsbDeviceChooseDialog();
        usbDeviceChooseDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsbDevice mUsbDevice = (UsbDevice) parent.getAdapter().getItem(position);

                PrinterInfo printerInfo=new PrinterInfo();
                printerInfo.setDeviceName(mUsbDevice.getDeviceName());
                printerInfo.setVendorId(mUsbDevice.getVendorId());
                printerInfo.setProductId(mUsbDevice.getProductId());
                printerInfo.setDeviceClass(mUsbDevice.getDeviceClass());
                printerInfo.setDeviceSubClass(mUsbDevice.getDeviceSubclass());
                printerInfo.setProtocol(mUsbDevice.getDeviceProtocol());
                lblConnectSpinner.setTag(printerInfo);
                lblConnectSpinner.setText(mUsbDevice.getDeviceName());

                usbDeviceChooseDialog.dismiss();
            }
        });
        usbDeviceChooseDialog.show(getFragmentManager(), null);
    }

    private void printSelfTestPage() {

        PrinterConnectionType printerConnectionType=(PrinterConnectionType) spnConnectionType.getSelectedItem();
        switch (printerConnectionType) {

            case BLUETOOTH_PRINTER:
                HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
                hsBluetoothPrintDriver.Begin();
                hsBluetoothPrintDriver.SetDefaultSetting();
                hsBluetoothPrintDriver.SelftestPrint();
                break;
            case USB_PRINTER:
                PrinterInfo printerInfo = (PrinterInfo) lblConnectSpinner.getTag();
                if (printerInfo == null) {
                    Toast.makeText(mContext, "Please select device", Toast.LENGTH_SHORT).show();
                    return;
                }
                UsbDevice usbDevice = getUsbDevice(printerInfo);
                if (usbDevice == null) {
                    Toast.makeText(mContext, "Please connect device using usb", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (connectUsb(usbDevice)) {
                    HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                    hsUsbPrintDriver.Begin();
                    hsUsbPrintDriver.SetDefaultSetting();
                    hsUsbPrintDriver.SelftestPrint();
                }
                break;
            case WIFI_PRINTER:
                HsWifiPrintDriver hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
                hsWifiPrintDriver.Begin();
                hsWifiPrintDriver.SetDefaultSetting();
                hsWifiPrintDriver.SelftestPrint();
                break;
        }

    }

    private void showWifiDeviceChooseDialog() {

        WifiDeviceChooseDialog wifiDeviceChooseDialog = new WifiDeviceChooseDialog();
        Bundle args = new Bundle();
        args.putString(WifiDeviceChooseDialog.BUNDLE_KEY_ADDRESS, lblConnectSpinner.getText().toString());
        wifiDeviceChooseDialog.setArguments(args);
        wifiDeviceChooseDialog.setOnClickListener(new CustomDialogInterface.onPositiveClickListener() {
            @Override
            public void onDialogPositiveClick(String text) {
                connectWifi(text);
                lblConnectSpinner.setText(text);
            }
        });
        wifiDeviceChooseDialog.show(getFragmentManager(), null);
    }

    public UsbDevice getUsbDevice(PrinterInfo printerInfo){
        UsbManager mUsbManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            if(device.getDeviceName().equals(printerInfo.getDeviceName())){
                return device;
            }


        }
        return null;
    }
}
