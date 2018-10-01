package com.spidertechnosoft.app.xeniamobi_v2.utils;

import android.content.Context;

import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.DailySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterConnectionType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterModel;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.ItemSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.StaffSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;

import java.util.List;

public class PrintUtil {

    private Context mContext;

    SessionManager sessionManager;
    private static PrintUtil mPrintUtil = new PrintUtil();

    int ret = -1;

    private PrintUtil() {
    }

    public static PrintUtil getInstance() {
        return mPrintUtil;
    }

    public void initialize(Context context) {
        this.mContext = context.getApplicationContext();
        this.sessionManager=new SessionManager(mContext);
        if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
        }else {
            AidlUtil.getInstance().connectPrinterService(mContext);
        }

    }

    public void initializePrinter() {
        if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
        }else {
            AidlUtil.getInstance().initPrinter();
        }

    }

    public void printSale(final Sale sale) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printSale(sale);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printSale(sale);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printSale(sale);
        }

    }

    public void printDailySummaryReport(final String fromDate, final String toDate, final DailySummary dailySummary) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printDailySummaryReport(fromDate,toDate,dailySummary);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printDailySummaryReport(fromDate,toDate,dailySummary);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printDailySummaryReport(fromDate,toDate,dailySummary);
        }

    }

    public void printItemSummaryReport(final String fromDate, final String toDate, final List<ItemSummary> itemSummaries) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printItemSummaryReport(fromDate,toDate,itemSummaries);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printItemSummaryReport(fromDate,toDate,itemSummaries);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printItemSummaryReport(fromDate,toDate,itemSummaries);
        }

    }

    public void printCategorySummaryReport(final String fromDate, final String toDate, final List<CategorySummary> categorySummaries) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printCategorySummaryReport(fromDate,toDate,categorySummaries);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printCategorySummaryReport(fromDate,toDate,categorySummaries);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printCategorySummaryReport(fromDate,toDate,categorySummaries);
        }

    }

    public void printStaffSummaryReport(final String fromDate, final String toDate, final List<StaffSummary> staffSummaries) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printStaffSummaryReport(fromDate,toDate,staffSummaries);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printStaffSummaryReport(fromDate,toDate,staffSummaries);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printStaffSummaryReport(fromDate,toDate,staffSummaries);
        }

    }

    public void printSaleSummaryReport(final String fromDate, final String toDate, final List<Sale> sales) {

        if(SettingService.getPrinterModel().equals(PrinterModel.OTHER)) {

            if(SettingService.getPrinterConnectionType().equals(PrinterConnectionType.USB_PRINTER)){
                UsbPrintUtil usbPrintUtil=new UsbPrintUtil(mContext);
                if(usbPrintUtil.connect()){
                    usbPrintUtil.printSalesSummaryReport(fromDate,toDate,sales);
                }
            }

        }else if(SettingService.getPrinterModel().equals(PrinterModel.CIONTEK)){
            CionteckPrintUtil cionteckPrintUtil=new CionteckPrintUtil(mContext);
            cionteckPrintUtil.printSalesSummaryReport(fromDate,toDate,sales);
        }else {
            SunmiPrintUtil sunmiPrintUtil=new SunmiPrintUtil(mContext);
            sunmiPrintUtil.printSalesSummaryReport(fromDate,toDate,sales);
        }

    }

}
