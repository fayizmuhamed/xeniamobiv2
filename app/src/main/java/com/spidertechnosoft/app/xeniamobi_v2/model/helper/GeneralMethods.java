package com.spidertechnosoft.app.xeniamobi_v2.model.helper;


import com.spidertechnosoft.app.xeniamobi_v2.model.service.SettingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 11/24/2017.
 */

public class GeneralMethods {

    public static final String DISPLAY_DATE_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";

    public static final String SERVER_DATE_TIME_FORMAT="yyyy-MM-dd'T'HH:mm:ss";

    public static final String LOCAL_DATE_TIME_FORMAT="yyyy-MM-dd hh:mm aa";

    public static final String LOCAL_DATE_FORMAT="yyyy-MM-dd";

    public static final String LOCAL_DATE_INVOICE_FORMAT="dd-MM-yyyy";

    public static final String LOCAL_TIME_INVOICE_FORMAT="hh:mm aa";

    /**
     * Function to format a number for the double value
     *
     * @param value - The value to be formatted
     * @return - The formatted value
     */
    public static String formatNumber(double value ) {

        // Create the DecimalFormat object
       /* DecimalFormat formatter = new DecimalFormat(AppSettings.decimalPosition==2?"#0.00":"#");
        formatter.setDecimalSeparatorAlwaysShown(false);*/

       Integer decimalPosition= SettingService.getSettingsInteger(SettingConfig.DECIMAL_POSITIONS);

        String.format("%."+decimalPosition+"f", value);

        // Get the formatted data
        String data =  String.format("%."+decimalPosition+"f", value);



        // Return the formatted value
        return data;
    }

    public static Double getBasePrice(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double basePrice=0.0;

        if(isTaxIncl){
            basePrice= price/(1+((taxPercentage==null?0.0:taxPercentage)/100));
        }else{
            basePrice=price;
        }

        return GeneralMethods.getTwoDigitDecimalFormat(basePrice,2);
    }

    public static Double getTaxAmount(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double taxAmount=0.0;

        if(isTaxIncl){
            taxAmount=price-(price/(1+((taxPercentage==null?0.0:taxPercentage)/100)));
        }else{
            taxAmount=price*((taxPercentage==null?0.0:taxPercentage)/100);
        }

        return GeneralMethods.getTwoDigitDecimalFormat(taxAmount,2);
    }

    public static Double getNetAmount(Double price,Double taxPercentage,Boolean isTaxIncl){
        Double basePrice=0.0;
        Double tax=0.0;

        if(isTaxIncl){
            tax=0.0;//(price*taxPercentage)/(100+taxPercentage);
            basePrice=price;//-tax;
        }else{
            tax=price*((taxPercentage==null?0.0:taxPercentage)/100);
            basePrice=price;
        }

        return GeneralMethods.getTwoDigitDecimalFormat(basePrice+tax,2);
    }

    public static Double getTwoDigitDecimalFormat(Double value,int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getDateInSpecifiedFormat(Date date, String format){

        if(date==null){

            return "";
        }

        // Create the formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        String dateInSpecifiedFormat=dateFormat.format(date);

        return dateInSpecifiedFormat;
    }

    public static Date getDateFromSpecifiedFormatString(String strDate, String format) {


        try {

            // Create the formatter
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            // Parse the date
            Date date = dateFormat.parse(strDate);

            // Return the date long value
            return date;

        } catch (ParseException e) {

            // Return -1
            return null;

        }
    }

    public static String convertDateFormat(String strDate, String fromFormat, String toFormat) {


        try {

            // Create the formatter
            SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat);

            // Parse the date
            Date date = sdfFrom.parse(strDate);

            // Create the formatter
            SimpleDateFormat sdfTo = new SimpleDateFormat(toFormat);

            String dateInSpecifiedFormat=sdfTo.format(date);

            // Return the date long value
            return dateInSpecifiedFormat;

        } catch (ParseException e) {

            // Return -1
            return null;

        }
    }

    public static String getImageUrl(String imageUrl){

        /*Database mDatabase=Database.getInstance();

        String baseUrl=mDatabase.getBaseUrl();*/

       /* if(baseUrl==null||baseUrl.isEmpty()){

            return imageUrl;
        }

        baseUrl+=imageUrl;*/

        return "";
    }

    public static Date getDayStart(){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        return now.getTime();
    }

}
