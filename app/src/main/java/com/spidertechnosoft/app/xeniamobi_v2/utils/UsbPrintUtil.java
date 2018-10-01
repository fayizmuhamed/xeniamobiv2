package com.spidertechnosoft.app.xeniamobi_v2.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;

import com.rtdriver.driver.HsBluetoothPrintDriver;
import com.rtdriver.driver.HsUsbPrintDriver;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.DailySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.ItemSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterInfo;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrintingSize;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.StaffSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.SaleLineItem;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.GeneralMethods;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.SettingConfig;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.StringAlign;
import com.spidertechnosoft.app.xeniamobi_v2.view.receiver.UsbDeviceReceiver;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UsbPrintUtil {

    private Context mContext;

    SessionManager sessionManager;
    UsbManager mUsbManager =null;
    int ret = -1;

    public UsbPrintUtil(Context mContext) {
        this.mContext = mContext;
        this.sessionManager=new SessionManager(mContext);
        mUsbManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        HsUsbPrintDriver.getInstance().setUsbManager(mUsbManager);


    }

    public Boolean connect(){
        UsbDevice usbDevice=getUsbDevice();
        if(usbDevice==null){
            Log.e(UsbPrintUtil.class.getSimpleName(),"USB Printer Not Connected");
            return false;
        }
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0);//
        if (mUsbManager.hasPermission(usbDevice)) {

            return HsUsbPrintDriver.getInstance().connect(usbDevice, mContext, mPermissionIntent);
        } else{
            mUsbManager.requestPermission(usbDevice,mPermissionIntent);
            Log.e(UsbPrintUtil.class.getSimpleName(),"USB Printer has no permission");
            return false;
        }
    }

    public UsbDevice getUsbDevice(){
        PrinterInfo printerInfo=SettingService.getPrinterConnectionInfo();
        if(printerInfo==null){
            return null;
        }
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


    public void printSale(final Sale sale) {




        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatterHeader = null;

                StringAlign formatterRight = null;
                StringAlign formatterLeftMiddle = null;
                StringAlign formatterRightMiddle = null;

                StringAlign formatItemName = null;

                StringAlign formatItemRate = null;

                StringAlign formatItemQty = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterQty = null;
                StringAlign formatFooterTotal = null;

                StringAlign formatFooterNetLabel = null;

                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);
                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);

                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);
                    formatterLeftMiddle = new StringAlign(24, StringAlign.JUST_LEFT);
                    formatterRightMiddle = new StringAlign(24, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(27, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(27, StringAlign.JUST_LEFT);

                    formatFooterQty = new StringAlign(5, StringAlign.JUST_CENTER);
                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                    formatFooterNetLabel = new StringAlign(32, StringAlign.JUST_LEFT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);
                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);

                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);
                    formatterLeftMiddle = new StringAlign(16, StringAlign.JUST_LEFT);
                    formatterRightMiddle = new StringAlign(16, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(11, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(11, StringAlign.JUST_LEFT);

                    formatFooterQty = new StringAlign(5, StringAlign.JUST_CENTER);
                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                    formatFooterNetLabel = new StringAlign(16, StringAlign.JUST_LEFT);
                }

                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_ADDRESS));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_PLACE));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_CONTACT));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_TIN).isEmpty()?"":((SettingService.getTaxType().equals(TaxType.GST)?"GST":"TIN")+SettingService.getSettingsString(SettingConfig.COMPANY_TIN)));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write("INVOICE");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                String invoiceSubHead=formatterLeftMiddle.format("Inv.No:"+sale.getId(),null)+formatterRightMiddle.format("Date:"+GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_INVOICE_FORMAT),null);
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(invoiceSubHead);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                String invoiceSubHead1=formatterRight.format(GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_TIME_INVOICE_FORMAT),null);
                hsUsbPrintDriver.USB_Write(invoiceSubHead1);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Customer:"+(sale.getCustomerName()==null?"":sale.getCustomerName()),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);



                String amount=formatItemName.format("ITEM",null)+formatItemQty.format("QTY",null)+formatItemRate.format("PRICE",null)+formatItemAmount.format("AMOUNT",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(amount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);
                Integer totalQty=0;

                HashMap<Double,Double> taxHashMap=new HashMap<>();

                for(SaleLineItem saleLineItem:sale.getSaleLineItems()){

                    if(saleLineItem.getTaxPer()!=null&&saleLineItem.getTaxPer().intValue()>0){

                        Double taxableAmount=0.0;
                        if(taxHashMap.containsKey(saleLineItem.getTaxPer()))
                            taxableAmount=taxHashMap.get(saleLineItem.getTaxPer());

                        taxableAmount+=(saleLineItem.getTaxableAmount()==null?0.0:saleLineItem.getTaxableAmount());

                        taxHashMap.put(saleLineItem.getTaxPer(),taxableAmount);
                    }

                    totalQty+=saleLineItem.getQty();
                    String itemPrice=GeneralMethods.formatNumber(saleLineItem.getItemRate());
                    String itemAmount=GeneralMethods.formatNumber(saleLineItem.getNetAmount());

                    String lineItem= formatItemName.format("",null)+formatItemQty.format(saleLineItem.getQty().toString(),null)+formatItemRate.format(itemPrice,null)+formatItemAmount.format(itemAmount,null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(formatterLeft.format(saleLineItem.getItemName(),null));
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem);


                }

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);


                String total= formatFooterLabel.format("Total",null)+formatFooterQty.format(totalQty.toString(),null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getGrossAmount()),null);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(total);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                String taxHeader= " "+formatItemAmount.format("Gross",null)+formatItemAmount.format("Tax",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x02);
                hsUsbPrintDriver.USB_Write(taxHeader);


                for(HashMap.Entry entry:taxHashMap.entrySet()){

                    Double taxPer=Double.valueOf(entry.getKey().toString());

                    Double taxableAmount=Double.valueOf(entry.getValue().toString());

                    if(SettingService.getTaxType().equals(TaxType.GST)){

                        Double taxSplit1=taxPer/2;

                        Double taxSplit2=taxPer/2;


                        Double taxSplitAmount1=taxableAmount*(taxSplit1/100);
                        Double taxSplitAmount2=taxableAmount*(taxSplit2/100);
                        String taxSplitLabel1="";
                        String taxSplitLabel2="";

                        if(SettingService.getLocationType().equals(LocationType.STATE)){
                            taxSplitLabel1="CGST@"+taxSplit1+"%";
                            taxSplitLabel2="SGST@"+taxSplit2+"%";

                        }else {
                            taxSplitLabel1="CGST@"+taxSplit1+"%";
                            taxSplitLabel2="UTGST@"+taxSplit2+"%";
                        }

                        String taxLine1= taxSplitLabel1+" "+ formatItemAmount.format(GeneralMethods.formatNumber(taxableAmount),null)+formatItemAmount.format(GeneralMethods.formatNumber(taxSplitAmount1),null);
                        String taxLine2= taxSplitLabel2+" "+ formatItemAmount.format(GeneralMethods.formatNumber(taxableAmount),null)+formatItemAmount.format(GeneralMethods.formatNumber(taxSplitAmount2),null);

                        hsUsbPrintDriver.LF();
                        hsUsbPrintDriver.CR();
                        hsUsbPrintDriver.USB_Write(taxLine1);
                        hsUsbPrintDriver.LF();
                        hsUsbPrintDriver.CR();
                        hsUsbPrintDriver.USB_Write(taxLine2);


                    }else {

                        Double taxAmount=taxableAmount*(taxPer/100);
                        String taxLabel="VAT@"+taxPer+"%";

                        String taxLine= taxLabel+" "+ formatItemAmount.format(GeneralMethods.formatNumber(taxableAmount),null)+formatItemAmount.format(GeneralMethods.formatNumber(taxAmount),null);

                        hsUsbPrintDriver.LF();
                        hsUsbPrintDriver.CR();
                        hsUsbPrintDriver.USB_Write(taxLine);

                    }
                }

                String discount= formatterLeftMiddle.format("Disc:"+GeneralMethods.formatNumber(sale.getDiscount()),null)+formatterRightMiddle.format("Round Off:"+GeneralMethods.formatNumber(sale.getRoundOff()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(discount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);



                String netTotal= formatFooterNetLabel.format("Net Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getNetAmount()),null);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(netTotal);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                String paymentMode="";
                String paymentInfo="";
                if(sale.getPaymentType().equals(PaymentType.CASH)) {
                    paymentMode += "CASH";
                    paymentInfo="Received:"+GeneralMethods.formatNumber(sale.getCashReceived())+" Return:"+GeneralMethods.formatNumber(sale.getReturnedAmount());

                }else if(sale.getPaymentType().equals(PaymentType.CARD)) {
                    paymentMode += "CARD";
                    paymentInfo="Received:"+GeneralMethods.formatNumber(sale.getCardReceived());

                }else if(sale.getPaymentType().equals(PaymentType.CREDIT))
                    paymentMode+="CREDIT";
                else if(sale.getPaymentType().equals(PaymentType.BOTH)) {
                    paymentMode += "";
                    paymentInfo="Cash:"+GeneralMethods.formatNumber(sale.getCashReceived())+ " Card:"+GeneralMethods.formatNumber(sale.getCardReceived())+" Return:"+GeneralMethods.formatNumber(sale.getReturnedAmount());

                }
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write("Payment Mode:"+paymentMode);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(paymentInfo);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_FOOTER));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();


            }
        }).start();

    }

    public void printDailySummaryReport(final String fromDate, final String toDate,final DailySummary dailySummary) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatItemName = null;

                StringAlign formatItemValue =null;



                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);


                    formatItemName = new StringAlign(38, StringAlign.JUST_LEFT);

                    formatItemValue =new StringAlign(10, StringAlign.JUST_RIGHT);


                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(22, StringAlign.JUST_LEFT);

                    formatItemValue =new StringAlign(10, StringAlign.JUST_RIGHT);

                }

                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x08);
                hsUsbPrintDriver.USB_Write("Category Summary Report");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.USB_Write("Report Period : "+fromDatetime+" To "+toDateTime);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                String noOfSales=formatItemName.format("Total No Of Sales",null)+formatItemValue.format(dailySummary.getNoOfSales()+"",null);
                hsUsbPrintDriver.USB_Write(noOfSales);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                String cashTransactions=formatItemName.format("Cash Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashTransaction()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(cashTransactions);

                String cardTransactions=formatItemName.format("Card Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardTransaction()),null);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(cardTransactions);

                String creditTransactions=formatItemName.format("Credit Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCreditTransaction()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(creditTransactions);

                String mixedTransactions=formatItemName.format("Mixed Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getBothTransaction()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(mixedTransactions);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                String totalAmount=formatItemName.format("Total Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTotalAmount()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(totalAmount);

                if(SettingService.getTaxType().equals(TaxType.VAT)){

                    String vatAmount=formatItemName.format("VAT",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTaxAmount()),null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(vatAmount);

                }else {
                    Double taxSplitAmount1=dailySummary.getTaxAmount()/2;
                    Double taxSplitAmount2=dailySummary.getTaxAmount()/2;

                    String cgst=formatItemName.format("CGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount1),null);

                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(cgst);

                    if(SettingService.getLocationType().equals(LocationType.STATE)){
                        String sgst=formatItemName.format("SGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        hsUsbPrintDriver.LF();
                        hsUsbPrintDriver.CR();
                        hsUsbPrintDriver.USB_Write(sgst);

                    }else {
                        String utgst=formatItemName.format("UTGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        hsUsbPrintDriver.LF();
                        hsUsbPrintDriver.CR();
                        hsUsbPrintDriver.USB_Write(utgst);
                    }
                }

                String grossAmount=formatItemName.format("Gross Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getGrossAmount()),null);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(grossAmount);

                String roundOff=formatItemName.format("Round Off",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getRoundOff()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(roundOff);

                String netAmount=formatItemName.format("Net Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getNetAmount()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(netAmount);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);


                String cashReceived=formatItemName.format("Cash Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashReceived()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(cashReceived);

                String cardReceived=formatItemName.format("Card Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardReceived()),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(cardReceived);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);
                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();


            }
        }).start();

    }

    public void printItemSummaryReport(final String fromDate, final String toDate, final List<ItemSummary> itemSummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;


                StringAlign formatItemName = null;

                StringAlign formatItemRate = null;

                StringAlign formatItemQty = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(27, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(11, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }

                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x08);
                hsUsbPrintDriver.USB_Write("Item Summary Report");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.USB_Write("Report Period : "+fromDatetime+" To "+toDateTime);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(dline);

                String amount=formatItemName.format("Item",null)+formatItemQty.format("Qty",null)+formatItemRate.format("Price",null)+formatItemAmount.format("Amount",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(amount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);
                //print line items
                Double totalAmount=0.0;
                LinkedList<TableItem> tableItems=new LinkedList<>();
                for(ItemSummary itemSummary:itemSummaries){

                    totalAmount+=itemSummary.getItemAmount();
                    String itemPrice=GeneralMethods.formatNumber(itemSummary.getItemPrice());
                    String itemAmount=GeneralMethods.formatNumber(itemSummary.getItemAmount());

                    String lineItem= formatItemName.format("",null)+formatItemQty.format(itemSummary.getItemQty().toString(),null)+formatItemRate.format(itemPrice,null)+formatItemAmount.format(itemAmount,null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(formatterLeft.format(itemSummary.getItemName(),null));

                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem);

                }
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(total);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();


            }
        }).start();

    }

    public void printCategorySummaryReport(final String fromDate, final String toDate,final List<CategorySummary> categorySummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;


                StringAlign formatItemName = null;

                StringAlign formatItemQty = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(35, StringAlign.JUST_LEFT);


                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(19, StringAlign.JUST_LEFT);


                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }


                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x08);
                hsUsbPrintDriver.USB_Write("Category Summary Report");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.USB_Write("Report Period : "+fromDatetime+" To "+toDateTime);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(dline);

                String amount=formatItemName.format("Category",null)+formatItemQty.format("Qty",null)+formatItemAmount.format("Amount",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(amount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);
                //print line items
                Double totalAmount=0.0;
                LinkedList<TableItem> tableItems=new LinkedList<>();
                for(CategorySummary categorySummary:categorySummaries){

                    totalAmount+=categorySummary.getCategoryAmount();
                    String itemAmount=GeneralMethods.formatNumber(categorySummary.getCategoryAmount());
                    String lineItem= formatItemName.format(categorySummary.getCategoryName(),null)+formatItemQty.format(categorySummary.getCategoryQty().toString(),null)+formatItemAmount.format(itemAmount,null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem);

                }
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(total);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();



            }
        }).start();

    }

    public void printStaffSummaryReport(final String fromDate, final String toDate,final List<StaffSummary> staffSummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;


                StringAlign formatItemName = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(40, StringAlign.JUST_LEFT);


                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatItemName = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }

                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x08);
                hsUsbPrintDriver.USB_Write("Staff Summary Report");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.USB_Write("Report Period : "+fromDatetime+" To "+toDateTime);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(dline);

                String amount=formatItemName.format("Staff",null)+formatItemAmount.format("Amount",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(amount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);
                //print line items
                Double totalAmount=0.0;
                LinkedList<TableItem> tableItems=new LinkedList<>();
                for(StaffSummary staffSummary :staffSummaries){

                    totalAmount+= staffSummary.getAmount();
                    String itemAmount=GeneralMethods.formatNumber(staffSummary.getAmount());

                    String lineItem= formatItemName.format(staffSummary.getUserName(),null)+formatItemAmount.format(itemAmount,null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem);

                }
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(total);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();



            }
        }).start();

    }

    public void printSalesSummaryReport(final String fromDate, final String toDate,final List<Sale> sales) {



        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatInvoiceNo = null;
                StringAlign formatInvoiceDate= null;

                StringAlign formatCustomer = null;
                StringAlign formatMode = null;
                StringAlign formatInvoiceAmount = null;



                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatInvoiceNo = new StringAlign(28, StringAlign.JUST_LEFT);
                    formatInvoiceDate= new StringAlign(20, StringAlign.JUST_LEFT);

                    formatCustomer = new StringAlign(32, StringAlign.JUST_LEFT);
                    formatMode = new StringAlign(6, StringAlign.JUST_RIGHT);
                    formatInvoiceAmount = new StringAlign(10, StringAlign.JUST_RIGHT);



                }else{


                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatInvoiceNo = new StringAlign(13, StringAlign.JUST_LEFT);
                    formatInvoiceDate= new StringAlign(19, StringAlign.JUST_LEFT);

                    formatCustomer = new StringAlign(16, StringAlign.JUST_LEFT);
                    formatMode = new StringAlign(6, StringAlign.JUST_RIGHT);
                    formatInvoiceAmount = new StringAlign(10, StringAlign.JUST_RIGHT);


                }

                String dline = formatterNormalCenter.format("","-");

                HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
                hsUsbPrintDriver.Begin();
                hsUsbPrintDriver.SetDefaultSetting();
                hsUsbPrintDriver.SetAlignMode((byte) 0x01);//居中
                hsUsbPrintDriver.SetCharacterPrintMode((byte) (0x08 | 0x10 | 0x20));//粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(SettingService.getSettingsString(SettingConfig.COMPANY_NAME));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x08);
                hsUsbPrintDriver.USB_Write("Sale Summary Report");
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetCharacterPrintMode((byte) 0x00);//解除粗体、倍高、倍宽
                hsUsbPrintDriver.USB_Write(dline);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x01);
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.USB_Write("Report Period : "+fromDatetime+" To "+toDateTime);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.SetAlignMode((byte)0x00);
                hsUsbPrintDriver.USB_Write(dline);

                Integer salesCount=sales.size();
                Double total=0.0;
                for(Sale sale:sales){
                    total+=sale.getNetAmount();
                }
                String amount=GeneralMethods.formatNumber(total);

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write("NO OF BILLS : "+String.valueOf(salesCount));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write("TOTAL SALES : "+amount);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                String invoiceHeader=formatInvoiceDate.format("INVOICE DATE",null)+formatInvoiceNo.format("INVOICE NO",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(invoiceHeader);

                String invoiceHeader1=formatCustomer.format("CUSTOMER",null)+formatMode.format("MODE",null)+formatInvoiceAmount.format("AMOUNT",null);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(invoiceHeader1);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                int i=0;

                for(Sale sale:sales){

                    String date=GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                    String itemAmount=GeneralMethods.formatNumber(sale.getNetAmount());
                    String lineItem=formatInvoiceDate.format(date,null)+ formatInvoiceNo.format(sale.getId().toString(),null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem);

                    String lineItem1=formatCustomer.format(sale.getCustomerName()==null?"":sale.getCustomerName(),null)+ formatMode.format(sale.getPaymentType().toString(),null)+formatInvoiceAmount.format(itemAmount,null);
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.USB_Write(lineItem1);
                }

                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(dline);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.USB_Write(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.LF();
                hsUsbPrintDriver.CR();
                hsUsbPrintDriver.PartialCutPaper();


            }
        }).start();

    }
}
