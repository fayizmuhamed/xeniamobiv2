package com.spidertechnosoft.app.xeniamobi_v2.utils;

import android.content.Context;
import android.os.Message;

import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.DailySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.ItemSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import vpos.apipackage.PosApiHelper;

public class CionteckPrintUtil {

    private Context mContext;

    SessionManager sessionManager;

    PosApiHelper posApiHelper = PosApiHelper.getInstance();

    int ret = -1;

    public CionteckPrintUtil(Context mContext) {
        this.mContext = mContext;
        this.sessionManager=new SessionManager(mContext);
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
                    formatFooterNetLabel = new StringAlign(24, StringAlign.JUST_LEFT);

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


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getSettingsString(SettingConfig.COMPANY_ADDRESS),null));

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getSettingsString(SettingConfig.COMPANY_PLACE),null));

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getSettingsString(SettingConfig.COMPANY_CONTACT),null));

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getTaxType().equals(TaxType.GST)?"GST TIN":"TIN"+SettingService.getSettingsString(SettingConfig.COMPANY_TIN),null));

                //posApiHelper.PrintSetGray(ret);
               /* ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }*/
                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("INVOICE","-"));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterLeftMiddle.format("Inv.No:"+sale.getId(),null)+formatterRightMiddle.format("Date:"+GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_INVOICE_FORMAT),null));

                posApiHelper.PrintStr(formatterRight.format(GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_TIME_INVOICE_FORMAT),null));


                posApiHelper.PrintStr(formatterLeft.format("Customer:"+(sale.getCustomerName()==null?"":sale.getCustomerName()),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));


                String amount=formatItemName.format("ITEM",null)+formatItemQty.format("QTY",null)+formatItemRate.format("PRICE",null)+formatItemAmount.format("AMOUNT",null);

                posApiHelper.PrintStr(amount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

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

                    String lineItem= formatItemQty.format(saleLineItem.getQty().toString(),null)+formatItemRate.format(itemPrice,null)+formatItemAmount.format(itemAmount,null);

                    posApiHelper.PrintStr(formatterLeft.format(saleLineItem.getItemName(),null));

                    posApiHelper.PrintStr(formatterRight.format(lineItem,null));
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String total= formatFooterLabel.format("Total",null)+formatFooterQty.format(totalQty.toString(),null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getGrossAmount()),null);

                posApiHelper.PrintStr(total);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String taxHeader= " "+formatItemAmount.format("Gross",null)+formatItemAmount.format("Tax",null);


                posApiHelper.PrintStr(formatterRight.format(taxHeader,null));

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

                        posApiHelper.PrintStr(formatterRight.format(taxLine1,null));
                        posApiHelper.PrintStr(formatterRight.format(taxLine2,null));
                    }else {

                        Double taxAmount=taxableAmount*(taxPer/100);
                        String taxLabel="VAT@"+taxPer+"%";

                        String taxLine= taxLabel+" "+ formatItemAmount.format(GeneralMethods.formatNumber(taxableAmount),null)+formatItemAmount.format(GeneralMethods.formatNumber(taxAmount),null);

                        posApiHelper.PrintStr(formatterRight.format(taxLine,null));
                    }
                }

                String discount= formatterLeftMiddle.format("Disc:"+GeneralMethods.formatNumber(sale.getDiscount()),null)+formatterRightMiddle.format("Round Off:"+GeneralMethods.formatNumber(sale.getRoundOff()),null);

                posApiHelper.PrintStr(discount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));


                String netTotal= formatFooterNetLabel.format("Net Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getNetAmount()),null);

                posApiHelper.PrintStr(netTotal);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

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

                posApiHelper.PrintStr("Payment Mode:"+paymentMode);

                posApiHelper.PrintStr(paymentInfo);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getSettingsString(SettingConfig.COMPANY_FOOTER),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                try {

                    ret = posApiHelper.PrintCtnStart();

                }catch (Exception ex){

                    ret=-4;
                    ex.printStackTrace();
                }

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

                StringAlign formatterHeader = null;
                StringAlign formatterRight = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);


                    formatItemName = new StringAlign(38, StringAlign.JUST_LEFT);

                    formatItemValue =new StringAlign(10, StringAlign.JUST_RIGHT);


                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(22, StringAlign.JUST_LEFT);

                    formatItemValue =new StringAlign(10, StringAlign.JUST_RIGHT);

                }

                Message msg = Message.obtain();
                Message msg1 = Message.obtain();


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("Daily Summary Report","-"));

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x00);

                String fromDatetime=GeneralMethods.convertDateFormat(fromDate,GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterNormalCenter.format("Report Period : "+fromDatetime+" To "+toDateTime,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));


                String noOfSales=formatItemName.format("Total No Of Sales",null)+formatItemValue.format(dailySummary.getNoOfSales()+"",null);
                posApiHelper.PrintStr(noOfSales);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String cashTransactions=formatItemName.format("Cash Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashTransaction()),null);
                posApiHelper.PrintStr(cashTransactions);

                String cardTransactions=formatItemName.format("Card Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardTransaction()),null);
                posApiHelper.PrintStr(cardTransactions);

                String creditTransactions=formatItemName.format("Credit Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCreditTransaction()),null);
                posApiHelper.PrintStr(creditTransactions);

                String mixedTransactions=formatItemName.format("Mixed Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getBothTransaction()),null);
                posApiHelper.PrintStr(mixedTransactions);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String totalAmount=formatItemName.format("Total Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTotalAmount()),null);
                posApiHelper.PrintStr(totalAmount);

                if(SettingService.getTaxType().equals(TaxType.VAT)){

                    String vatAmount=formatItemName.format("VAT",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTaxAmount()),null);
                    posApiHelper.PrintStr(vatAmount);
                }else {
                    Double taxSplitAmount1=dailySummary.getTaxAmount()/2;
                    Double taxSplitAmount2=dailySummary.getTaxAmount()/2;

                    String cgst=formatItemName.format("CGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount1),null);
                    posApiHelper.PrintStr(cgst);

                    if(SettingService.getLocationType().equals(LocationType.STATE)){
                        String sgst=formatItemName.format("SGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        posApiHelper.PrintStr(sgst);

                    }else {
                        String utgst=formatItemName.format("UTGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        posApiHelper.PrintStr(utgst);
                    }
                }

                String grossAmount=formatItemName.format("Gross Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getGrossAmount()),null);
                posApiHelper.PrintStr(grossAmount);

                String roundOff=formatItemName.format("Round Off",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getRoundOff()),null);
                posApiHelper.PrintStr(roundOff);

                String netAmount=formatItemName.format("Net Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getNetAmount()),null);
                posApiHelper.PrintStr(netAmount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String cashReceived=formatItemName.format("Cash Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashReceived()),null);
                posApiHelper.PrintStr(cashReceived);

                String cardReceived=formatItemName.format("Card Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardReceived()),null);
                posApiHelper.PrintStr(cardReceived);


                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser(),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                ret = posApiHelper.PrintCtnStart();


            }
        }).start();

    }

    public void printItemSummaryReport(final String fromDate, final String toDate, final List<ItemSummary> itemSummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatterHeader = null;
                StringAlign formatterRight = null;


                StringAlign formatItemName = null;

                StringAlign formatItemRate = null;

                StringAlign formatItemQty = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(27, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(11, StringAlign.JUST_LEFT);

                    formatItemRate = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }


                Message msg = Message.obtain();
                Message msg1 = Message.obtain();


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }


                StringAlign formatterLeftMiddle = new StringAlign(12, StringAlign.JUST_LEFT);
                StringAlign formatterRightMiddle = new StringAlign(12, StringAlign.JUST_RIGHT);
                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("Item Summary Report","-"));

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x00);

                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterNormalCenter.format("Report Period : "+fromDatetime+" To "+toDateTime,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));



                String amount=formatItemName.format("Item",null)+formatItemQty.format("Qty",null)+formatItemRate.format("Price",null)+formatItemAmount.format("Amount",null);

                posApiHelper.PrintStr(amount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                Double totalAmount=0.0;

                for(ItemSummary itemSummary:itemSummaries){

                    totalAmount+=itemSummary.getItemAmount();
                    String itemPrice=GeneralMethods.formatNumber(itemSummary.getItemPrice());
                    String itemAmount=GeneralMethods.formatNumber(itemSummary.getItemAmount());

                    String lineItem= formatItemQty.format(itemSummary.getItemQty().toString(),null)+formatItemRate.format(itemPrice,null)+formatItemAmount.format(itemAmount,null);

                    posApiHelper.PrintStr(formatterLeft.format(itemSummary.getItemName(),null));

                    posApiHelper.PrintStr(formatterRight.format(lineItem,null));
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);

                posApiHelper.PrintStr(total);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                ret = posApiHelper.PrintCtnStart();


            }
        }).start();

    }

    public void printCategorySummaryReport(final String fromDate, final String toDate,final List<CategorySummary> categorySummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatterHeader = null;
                StringAlign formatterRight = null;


                StringAlign formatItemName = null;

                StringAlign formatItemQty = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(35, StringAlign.JUST_LEFT);


                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(19, StringAlign.JUST_LEFT);


                    formatItemQty = new StringAlign(5, StringAlign.JUST_CENTER);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }

                Message msg = Message.obtain();
                Message msg1 = Message.obtain();


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }


                StringAlign formatterLeftMiddle = new StringAlign(12, StringAlign.JUST_LEFT);
                StringAlign formatterRightMiddle = new StringAlign(12, StringAlign.JUST_RIGHT);
                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("Category Summary Report","-"));

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x00);

                String fromDatetime=GeneralMethods.convertDateFormat(fromDate,GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterNormalCenter.format("Report Period : "+fromDatetime+" To "+toDateTime,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));



                String amount=formatItemName.format("Category",null)+formatItemQty.format("Qty",null)+formatItemAmount.format("Amount",null);

                posApiHelper.PrintStr(amount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                Double totalAmount=0.0;

                for(CategorySummary categorySummary:categorySummaries){

                    totalAmount+=categorySummary.getCategoryAmount();
                    String itemAmount=GeneralMethods.formatNumber(categorySummary.getCategoryAmount());

                    String lineItem= formatItemName.format(categorySummary.getCategoryName(),null)+formatItemQty.format(categorySummary.getCategoryQty().toString(),null)+formatItemAmount.format(itemAmount,null);

                    posApiHelper.PrintStr(formatterRight.format(lineItem,null));
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));


                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);

                posApiHelper.PrintStr(total);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser(),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                ret = posApiHelper.PrintCtnStart();


            }
        }).start();

    }

    public void printStaffSummaryReport(final String fromDate, final String toDate,final List<StaffSummary> staffSummaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterLeft = null;

                StringAlign formatterHeader = null;
                StringAlign formatterRight = null;
                StringAlign formatItemName = null;

                StringAlign formatItemAmount = null;

                StringAlign formatFooterLabel =null;

                StringAlign formatFooterTotal = null;


                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(40, StringAlign.JUST_LEFT);


                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(24, StringAlign.JUST_RIGHT);

                }else{

                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);

                    formatItemName = new StringAlign(24, StringAlign.JUST_LEFT);

                    formatItemAmount = new StringAlign(8, StringAlign.JUST_RIGHT);

                    formatFooterLabel = new StringAlign(16, StringAlign.JUST_LEFT);

                    formatFooterTotal = new StringAlign(16, StringAlign.JUST_RIGHT);
                }

                Message msg = Message.obtain();
                Message msg1 = Message.obtain();


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("Staff Summary Report","-"));

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x00);

                String fromDatetime=GeneralMethods.convertDateFormat(fromDate,GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterNormalCenter.format("Report Period : "+fromDatetime+" To "+toDateTime,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String amount=formatItemName.format("Staff",null)+formatItemAmount.format("Amount",null);

                posApiHelper.PrintStr(amount);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                Double totalAmount=0.0;

                for(StaffSummary staffSummary :staffSummaries){

                    totalAmount+= staffSummary.getAmount();
                    String itemAmount=GeneralMethods.formatNumber(staffSummary.getAmount());

                    String lineItem= formatItemName.format(staffSummary.getUserName(),null)+formatItemAmount.format(itemAmount,null);

                    posApiHelper.PrintStr(formatterRight.format(lineItem,null));
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);

                posApiHelper.PrintStr(total);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                ret = posApiHelper.PrintCtnStart();


            }
        }).start();

    }

    public void printSalesSummaryReport(final String fromDate, final String toDate,final List<Sale> sales) {



        new Thread(new Runnable() {
            @Override
            public void run() {

                StringAlign formatterNormalCenter = null;

                StringAlign formatterHeader = null;
                StringAlign formatterRight = null;

                StringAlign formatterLeft = null;

                StringAlign formatInvoiceNo = null;
                StringAlign formatInvoiceDate= null;

                StringAlign formatCustomer = null;
                StringAlign formatMode = null;
                StringAlign formatInvoiceAmount = null;



                if(SettingService.getPrintingSize().equals(PrintingSize.EIGHTY)){

                    formatterNormalCenter = new StringAlign(48, StringAlign.JUST_CENTER);

                    formatterLeft = new StringAlign(48, StringAlign.JUST_LEFT);

                    formatterHeader = new StringAlign(36, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(48, StringAlign.JUST_RIGHT);

                    formatInvoiceNo = new StringAlign(28, StringAlign.JUST_LEFT);
                    formatInvoiceDate= new StringAlign(20, StringAlign.JUST_LEFT);

                    formatCustomer = new StringAlign(32, StringAlign.JUST_LEFT);
                    formatMode = new StringAlign(6, StringAlign.JUST_RIGHT);
                    formatInvoiceAmount = new StringAlign(10, StringAlign.JUST_RIGHT);



                }else{


                    formatterNormalCenter = new StringAlign(32, StringAlign.JUST_CENTER);
                    formatterHeader = new StringAlign(24, StringAlign.JUST_CENTER);
                    formatterRight = new StringAlign(32, StringAlign.JUST_RIGHT);

                    formatterLeft = new StringAlign(32, StringAlign.JUST_LEFT);

                    formatInvoiceNo = new StringAlign(13, StringAlign.JUST_LEFT);
                    formatInvoiceDate= new StringAlign(19, StringAlign.JUST_LEFT);

                    formatCustomer = new StringAlign(16, StringAlign.JUST_LEFT);
                    formatMode = new StringAlign(6, StringAlign.JUST_RIGHT);
                    formatInvoiceAmount = new StringAlign(10, StringAlign.JUST_RIGHT);


                }

                Message msg = Message.obtain();
                Message msg1 = Message.obtain();


                ret = posApiHelper.PrintInit(2, 24, 24, 0);

                //posApiHelper.PrintSetGray(ret);
                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);

                if (ret != 0) {
                    return;
                }


                posApiHelper.PrintStr(formatterHeader.format(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format("Sale Summary Report","-"));

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                ret = posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x00);

                String fromDatetime=GeneralMethods.convertDateFormat(fromDate,GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);

                posApiHelper.PrintStr(formatterNormalCenter.format("Report Period : "+fromDatetime+" To "+toDateTime,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));



                Integer salesCount=sales.size();
                Double total=0.0;
                for(Sale sale:sales){
                    total+=sale.getNetAmount();
                }
                String amount=GeneralMethods.formatNumber(total);

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterLeft.format("NO OF BILLS : "+String.valueOf(salesCount),null));

                posApiHelper.PrintStr(formatterLeft.format("TOTAL SALES : "+amount,null));

                ret = posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                if (ret != 0) {
                    return;
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                String invoiceHeader=formatInvoiceDate.format("INVOICE DATE",null)+formatInvoiceNo.format("INVOICE NO",null);

                posApiHelper.PrintStr(invoiceHeader);


                String invoiceHeader1=formatCustomer.format("CUSTOMER",null)+formatMode.format("MODE",null)+formatInvoiceAmount.format("AMOUNT",null);

                posApiHelper.PrintStr(invoiceHeader1);

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));

                int i=0;

                for(Sale sale:sales){

                    String date=GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                    String itemAmount=GeneralMethods.formatNumber(sale.getNetAmount());

                    String lineItem=formatInvoiceDate.format(date,null)+ formatInvoiceNo.format(sale.getId().toString(),null);

                    posApiHelper.PrintStr(formatterLeft.format(lineItem,null));

                    String lineItem1=formatCustomer.format(sale.getCustomerName(),null)+ formatMode.format(sale.getPaymentType().toString(),null)+formatInvoiceAmount.format(itemAmount,null);

                    posApiHelper.PrintStr(formatterRight.format(lineItem1,null));
                }

                posApiHelper.PrintStr(formatterNormalCenter.format("","-"));


                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr(formatterNormalCenter.format(SettingService.getSettingsString(SettingConfig.COMPANY_FOOTER),null));

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                posApiHelper.PrintStr("\n");

                ret = posApiHelper.PrintCtnStart();


            }
        }).start();

    }
}
