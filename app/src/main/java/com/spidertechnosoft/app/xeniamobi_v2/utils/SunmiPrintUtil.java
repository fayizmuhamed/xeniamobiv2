package com.spidertechnosoft.app.xeniamobi_v2.utils;

import android.content.Context;

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
import java.util.LinkedList;
import java.util.List;

public class SunmiPrintUtil {

    private Context mContext;

    SessionManager sessionManager;

    int ret = -1;

    public SunmiPrintUtil(Context mContext) {
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

                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print company address
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_ADDRESS),24,false,false,TextAlignment.CENTER);

                //print company place
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_PLACE),24,false,false,TextAlignment.CENTER);

                //print company contact
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_CONTACT),24,false,false,TextAlignment.CENTER);

                //print company gst/tin
                AidlUtil.getInstance().printText(SettingService.getTaxType().equals(TaxType.GST)?"GST":"TIN"+SettingService.getSettingsString(SettingConfig.COMPANY_TIN),24,false,false,TextAlignment.CENTER);

                AidlUtil.getInstance().printText("INVOICE",24,true,false,TextAlignment.CENTER);

                String invoiceSubHead=formatterLeftMiddle.format("Inv.No:"+sale.getId(),null)+formatterRightMiddle.format("Date:"+GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_INVOICE_FORMAT),null);

                AidlUtil.getInstance().printText(invoiceSubHead,24,false,false,TextAlignment.LEFT);

                String invoiceSubHead1=formatterRight.format(GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_TIME_INVOICE_FORMAT),null);

                AidlUtil.getInstance().printText(invoiceSubHead1,24,false,false,TextAlignment.RIGHT);

                AidlUtil.getInstance().printText(formatterLeft.format("Customer:"+(sale.getCustomerName()==null?"":sale.getCustomerName()),null),24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                String amount=formatItemName.format("ITEM",null)+formatItemQty.format("QTY",null)+formatItemRate.format("PRICE",null)+formatItemAmount.format("AMOUNT",null);

                AidlUtil.getInstance().printText(amount,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

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

                    AidlUtil.getInstance().printText(formatterLeft.format(saleLineItem.getItemName(),null),24,false,false,TextAlignment.LEFT);

                    AidlUtil.getInstance().printText(formatterRight.format(lineItem,null),24,false,false,TextAlignment.LEFT);

                }

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String total= formatFooterLabel.format("Total",null)+formatFooterQty.format(totalQty.toString(),null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getGrossAmount()),null);

                AidlUtil.getInstance().printText(formatterLeft.format(total,null),24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String taxHeader= " "+formatItemAmount.format("Gross",null)+formatItemAmount.format("Tax",null);

                AidlUtil.getInstance().printText(formatterRight.format(taxHeader,null),24,false,false,TextAlignment.LEFT);

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

                        AidlUtil.getInstance().printText(formatterRight.format(taxLine1,null),24,false,false,TextAlignment.LEFT);
                        AidlUtil.getInstance().printText(formatterRight.format(taxLine2,null),24,false,false,TextAlignment.LEFT);

                    }else {

                        Double taxAmount=taxableAmount*(taxPer/100);
                        String taxLabel="VAT@"+taxPer+"%";

                        String taxLine= taxLabel+" "+ formatItemAmount.format(GeneralMethods.formatNumber(taxableAmount),null)+formatItemAmount.format(GeneralMethods.formatNumber(taxAmount),null);

                        AidlUtil.getInstance().printText(formatterRight.format(taxLine,null),24,false,false,TextAlignment.LEFT);

                    }
                }

                String discount= formatterLeftMiddle.format("Disc:"+GeneralMethods.formatNumber(sale.getDiscount()),null)+formatterRightMiddle.format("Round Off:"+GeneralMethods.formatNumber(sale.getRoundOff()),null);

                AidlUtil.getInstance().printText(formatterLeft.format(discount,null),24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                String netTotal= formatFooterNetLabel.format("Net Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(sale.getNetAmount()),null);

                AidlUtil.getInstance().printText(netTotal,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

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
                AidlUtil.getInstance().printText("Payment Mode:"+paymentMode,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printText(paymentInfo,24,false,false,TextAlignment.LEFT);


                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_FOOTER),24,false,false,TextAlignment.CENTER);

                //print end line break
                AidlUtil.getInstance().print3Line();

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

                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print report header
                AidlUtil.getInstance().printText("Category Summary Report",24,true,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print report period
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText("Report Period : "+fromDatetime+" To "+toDateTime,20,false,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                String noOfSales=formatItemName.format("Total No Of Sales",null)+formatItemValue.format(dailySummary.getNoOfSales()+"",null);
                AidlUtil.getInstance().printText(noOfSales,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String cashTransactions=formatItemName.format("Cash Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashTransaction()),null);
                AidlUtil.getInstance().printText(cashTransactions,24,false,false,TextAlignment.LEFT);

                String cardTransactions=formatItemName.format("Card Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardTransaction()),null);
                AidlUtil.getInstance().printText(cardTransactions,24,false,false,TextAlignment.LEFT);

                String creditTransactions=formatItemName.format("Credit Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCreditTransaction()),null);
                AidlUtil.getInstance().printText(creditTransactions,24,false,false,TextAlignment.LEFT);

                String mixedTransactions=formatItemName.format("Mixed Transactions",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getBothTransaction()),null);
                AidlUtil.getInstance().printText(mixedTransactions,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String totalAmount=formatItemName.format("Total Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTotalAmount()),null);
                AidlUtil.getInstance().printText(totalAmount,24,false,false,TextAlignment.LEFT);

                if(SettingService.getTaxType().equals(TaxType.VAT)){

                    String vatAmount=formatItemName.format("VAT",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getTaxAmount()),null);
                    AidlUtil.getInstance().printText(vatAmount,24,false,false,TextAlignment.LEFT);
                }else {
                    Double taxSplitAmount1=dailySummary.getTaxAmount()/2;
                    Double taxSplitAmount2=dailySummary.getTaxAmount()/2;

                    String cgst=formatItemName.format("CGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount1),null);
                    AidlUtil.getInstance().printText(cgst,24,false,false,TextAlignment.LEFT);

                    if(SettingService.getLocationType().equals(LocationType.STATE)){
                        String sgst=formatItemName.format("SGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        AidlUtil.getInstance().printText(sgst,24,false,false,TextAlignment.LEFT);

                    }else {
                        String utgst=formatItemName.format("UTGST",null)+formatItemValue.format(GeneralMethods.formatNumber(taxSplitAmount2),null);
                        AidlUtil.getInstance().printText(utgst,24,false,false,TextAlignment.LEFT);
                    }
                }

                String grossAmount=formatItemName.format("Gross Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getGrossAmount()),null);
                AidlUtil.getInstance().printText(grossAmount,24,false,false,TextAlignment.LEFT);

                String roundOff=formatItemName.format("Round Off",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getRoundOff()),null);
                AidlUtil.getInstance().printText(roundOff,24,false,false,TextAlignment.LEFT);

                String netAmount=formatItemName.format("Net Amount",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getNetAmount()),null);
                AidlUtil.getInstance().printText(netAmount,24,false,false,TextAlignment.LEFT);

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String cashReceived=formatItemName.format("Cash Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCashReceived()),null);
                AidlUtil.getInstance().printText(cashReceived,24,false,false,TextAlignment.LEFT);

                String cardReceived=formatItemName.format("Card Received",null)+formatItemValue.format(GeneralMethods.formatNumber(dailySummary.getCardReceived()),null);
                AidlUtil.getInstance().printText(cardReceived,24,false,false,TextAlignment.LEFT);


                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null),20,false,false,TextAlignment.LEFT);

                //print end line break
                AidlUtil.getInstance().print3Line();



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


                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print report header
                AidlUtil.getInstance().printText("Item Summary Report",24,true,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print report period
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText("Report Period : "+fromDatetime+" To "+toDateTime,20,false,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                /*LinkedList<TableItem> tableHeaders=new LinkedList<>();

                TableItem tableHeader=new TableItem();
                tableHeader.setText(new String[]{"Item","Qty","Price","Amount"});
                tableHeader.setWidth(new int[]{11,5,7,7});
                tableHeader.setAlign(new int[]{TextAlignment.LEFT.getValue(),TextAlignment.CENTER.getValue(),TextAlignment.RIGHT.getValue(),TextAlignment.RIGHT.getValue()});

                tableHeaders.add(tableHeader);
                AidlUtil.getInstance().printTable(tableHeaders);*/

                //print list header
                String amount=formatItemName.format("Item",null)+formatItemQty.format("Qty",null)+formatItemRate.format("Price",null)+formatItemAmount.format("Amount",null);
                AidlUtil.getInstance().printText(amount,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print line items
                Double totalAmount=0.0;
                LinkedList<TableItem> tableItems=new LinkedList<>();
                for(ItemSummary itemSummary:itemSummaries){

                    totalAmount+=itemSummary.getItemAmount();
                    String itemPrice=GeneralMethods.formatNumber(itemSummary.getItemPrice());
                    String itemAmount=GeneralMethods.formatNumber(itemSummary.getItemAmount());

                    String lineItem= formatItemName.format("",null)+formatItemQty.format(itemSummary.getItemQty().toString(),null)+formatItemRate.format(itemPrice,null)+formatItemAmount.format(itemAmount,null);
                    AidlUtil.getInstance().printText(formatterLeft.format(itemSummary.getItemName(),null),24,false,false,TextAlignment.LEFT);
                    AidlUtil.getInstance().printText(lineItem,24,false,false,TextAlignment.LEFT);
                }
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                AidlUtil.getInstance().printText(total,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null),20,false,false,TextAlignment.LEFT);

                //print end line break
                AidlUtil.getInstance().print3Line();


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
                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print report header
                AidlUtil.getInstance().printText("Category Summary Report",24,true,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print report period
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText("Report Period : "+fromDatetime+" To "+toDateTime,20,false,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                //print item header
                String amount=formatItemName.format("Category",null)+formatItemQty.format("Qty",null)+formatItemAmount.format("Amount",null);
                AidlUtil.getInstance().printText(amount,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print line items
                Double totalAmount=0.0;
                for(CategorySummary categorySummary:categorySummaries){

                    totalAmount+=categorySummary.getCategoryAmount();
                    String itemAmount=GeneralMethods.formatNumber(categorySummary.getCategoryAmount());
                    String lineItem= formatItemName.format(categorySummary.getCategoryName(),null)+formatItemQty.format(categorySummary.getCategoryQty().toString(),null)+formatItemAmount.format(itemAmount,null);
                    AidlUtil.getInstance().printText(lineItem,24,false,false,TextAlignment.LEFT);


                }
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                AidlUtil.getInstance().printText(total,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null),20,false,false,TextAlignment.LEFT);

                //print end line break
                AidlUtil.getInstance().print3Line();


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

                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print report header
                AidlUtil.getInstance().printText("Staff Summary Report",24,true,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print report period
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText("Report Period : "+fromDatetime+" To "+toDateTime,20,false,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print item header
                String amount=formatItemName.format("Staff",null)+formatItemAmount.format("Amount",null);
                AidlUtil.getInstance().printText(amount,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print line items
                Double totalAmount=0.0;

                for(StaffSummary staffSummary :staffSummaries){

                    totalAmount+= staffSummary.getAmount();
                    String itemAmount=GeneralMethods.formatNumber(staffSummary.getAmount());

                    String lineItem= formatItemName.format(staffSummary.getUserName(),null)+formatItemAmount.format(itemAmount,null);

                    AidlUtil.getInstance().printText(lineItem,24,false,false,TextAlignment.LEFT);
                }

                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer total
                String total= formatFooterLabel.format("Total",null)+formatFooterTotal.format(GeneralMethods.formatNumber(totalAmount),null);
                AidlUtil.getInstance().printText(total,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null),20,false,false,TextAlignment.LEFT);

                //print end line break
                AidlUtil.getInstance().print3Line();

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

                AidlUtil.getInstance().initPrinter();

                //print company name
                AidlUtil.getInstance().printText(SettingService.getSettingsString(SettingConfig.COMPANY_NAME),36,true,false,TextAlignment.CENTER);

                //print report header
                AidlUtil.getInstance().printText("Sale Summary Report",24,true,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print report period
                String fromDatetime= GeneralMethods.convertDateFormat(fromDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                String toDateTime=GeneralMethods.convertDateFormat(toDate,GeneralMethods.DISPLAY_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText("Report Period : "+fromDatetime+" To "+toDateTime,20,false,false,TextAlignment.CENTER);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);


                Integer salesCount=sales.size();
                Double total=0.0;
                for(Sale sale:sales){
                    total+=sale.getNetAmount();
                }
                String amount=GeneralMethods.formatNumber(total);

                AidlUtil.getInstance().printText("NO OF BILLS : "+String.valueOf(salesCount),24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printText("TOTAL SALES : "+amount,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                String invoiceHeader=formatInvoiceDate.format("INVOICE DATE",null)+formatInvoiceNo.format("INVOICE NO",null);
                AidlUtil.getInstance().printText(invoiceHeader,24,false,false,TextAlignment.LEFT);

                String invoiceHeader1=formatCustomer.format("CUSTOMER",null)+formatMode.format("MODE",null)+formatInvoiceAmount.format("AMOUNT",null);
                AidlUtil.getInstance().printText(invoiceHeader1,24,false,false,TextAlignment.LEFT);
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                int i=0;

                for(Sale sale:sales){

                    String date=GeneralMethods.convertDateFormat(sale.getSalesDate(),GeneralMethods.SERVER_DATE_TIME_FORMAT,GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                    String itemAmount=GeneralMethods.formatNumber(sale.getNetAmount());
                    String lineItem=formatInvoiceDate.format(date,null)+ formatInvoiceNo.format(sale.getId().toString(),null);
                    AidlUtil.getInstance().printText(lineItem,24,false,false,TextAlignment.LEFT);

                    String lineItem1=formatCustomer.format(sale.getCustomerName(),null)+ formatMode.format(sale.getPaymentType().toString(),null)+formatInvoiceAmount.format(itemAmount,null);
                    AidlUtil.getInstance().printText(lineItem1,24,false,false,TextAlignment.LEFT);
                }
                AidlUtil.getInstance().printTextWithoutLineWrap(formatterNormalCenter.format("","-"),24,false,false,TextAlignment.CENTER);

                //print footer
                String currentDateTime=GeneralMethods.getDateInSpecifiedFormat(new Date(),GeneralMethods.LOCAL_DATE_TIME_FORMAT);
                AidlUtil.getInstance().printText(formatterLeft.format("Created : "+currentDateTime+","+"Generated: "+ sessionManager.getLoggedInUser().getUsername(),null),20,false,false,TextAlignment.LEFT);

                //print end line break
                AidlUtil.getInstance().print3Line();


            }
        }).start();

    }
}
