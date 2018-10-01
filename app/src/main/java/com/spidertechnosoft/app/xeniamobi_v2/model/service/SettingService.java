package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import com.google.gson.Gson;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.LocationType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterConnectionType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterInfo;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrinterModel;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PrintingSize;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.TaxType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Setting;
import com.spidertechnosoft.app.xeniamobi_v2.model.helper.SettingConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandheepgr on 12/1/16.
 */
public class SettingService implements IEntityService<Setting> {

    private static final String TAG = SettingService.class.getSimpleName();

    @Override
    public Long save(Setting setting) {

        // check if the setting is null
        if ( setting == null ) return null;

        // Save and return the id
        return setting.save();


    }

    public Long saveSetting(Setting setting, boolean isUpdate){

        Setting existingSettings=findByKey(setting.getKey());

        if(existingSettings==null){

            return setting.save();

        }else {

            if(isUpdate){

                setting.setId(existingSettings.getId());

                return setting.save();

            }else {

                return -1L;
            }
        }

    }

    @Override
    public Setting findById(Long id) {

        // Create the Setting object
        Setting setting = new Setting();

        // the setting by id
        return  setting.findById(Setting.class, id);

    }

    @Override
    public List<Setting> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {

        // Create the Setting object
        Setting setting = new Setting();

        // Return the list
        return setting.find(Setting.class, whereClause, whereArgs, groupBy, orderBy, limit);

    }


    @Override
    public boolean delete(Long id) {

        // Create the Setting object
        Setting setting = new Setting();
        setting.setId(id);

        // Return the response after delete
        return setting.delete();

    }

    @Override
    public boolean delete(Setting setting) {

        // Return the response after delete
        return setting.delete();

    }

    public void deleteDuplicateKey(){


        try {
            Setting setting=new Setting();

            setting.deleteAll(Setting.class, "id NOT IN (SELECT min(id) FROM SETTING GROUP BY key)");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Setting findByKey(String key){

        // Create the Settings list
        List<Setting> settings = new ArrayList<Setting>();

        Setting setting=new Setting();

        // If the uid is null, then return null
        if ( key == null ) {

            return null;

        }

        // the aircraft by id
        settings=  setting.find(Setting.class, "key = ?", key);

        return settings.size()>0?settings.get(0):null;

    }

    public static String getSettingsValueByKey(String key){


        Setting setting=new Setting();

        // If the uid is null, then return null
        if ( key == null ) {

            return null;

        }

        // the aircraft by id
        List<Setting> settings=  setting.find(Setting.class, "key = ?", key);

        if(settings==null||settings.size()==0){

            return "";
        }

        Setting fetchedSettings=settings.get(0);

        if(fetchedSettings==null){

            return "";
        }

        return fetchedSettings.getValue();

    }


    public static String getSettingsString(String key){

        // the aircraft by id
        String value=  getSettingsValueByKey(key);

        if(value==null||value.trim().isEmpty()){

            return "";
        }

        return value;

    }



    public static Integer getSettingsInteger(String key){


        // the aircraft by id
        String value=  getSettingsValueByKey(key);

        if(value==null||value.trim().isEmpty()){

            return 0;
        }

        return Integer.parseInt(value);

    }

    public static TaxType getTaxType(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.TAX_TYPE);

        if(value==null||value.trim().isEmpty()){

            return TaxType.GST;
        }

        Gson gson = new Gson();

        TaxType taxType = gson.fromJson(value, TaxType.class);

        return taxType;

    }

    public static LocationType getLocationType(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.LOCATION_TYPE);

        if(value==null||value.trim().isEmpty()){

            return LocationType.STATE;
        }

        Gson gson = new Gson();

        LocationType locationType = gson.fromJson(value, LocationType.class);

        return locationType;

    }

    public static PrinterModel getPrinterModel(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.PRINTER_MODEL);

        if(value==null||value.trim().isEmpty()){

            return PrinterModel.CIONTEK;
        }

        Gson gson = new Gson();

        PrinterModel printerModel = gson.fromJson(value, PrinterModel.class);

        return printerModel;

    }

    public static PrinterConnectionType getPrinterConnectionType(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.PRINTER_CONNECTION_TYPE);

        if(value==null||value.trim().isEmpty()){

            return PrinterConnectionType.BLUETOOTH_PRINTER;
        }

        Gson gson = new Gson();

        PrinterConnectionType printerConnectionType = gson.fromJson(value, PrinterConnectionType.class);

        return printerConnectionType;

    }

    public static PrintingSize getPrintingSize(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.PRINTING_SIZE);

        if(value==null||value.trim().isEmpty()){

            return PrintingSize.FIFTY_EIGHT;
        }

        Gson gson = new Gson();

        PrintingSize printingSize = gson.fromJson(value, PrintingSize.class);

        return printingSize;

    }

    public static String getNoOfPrint(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.NO_OF_PRINT);

        if(value==null||value.trim().isEmpty()){

            return "1";
        }

        return value;

    }


    public static PrinterInfo getPrinterConnectionInfo(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.PRINTER_CONNECTION_INFO);

        if(value==null||value.trim().isEmpty()){

            return null;
        }

        Gson gson = new Gson();

        try {

            PrinterInfo printerInfo = gson.fromJson(value, PrinterInfo.class);
            return printerInfo;
        }catch (Exception ex){
            return null;
        }




    }

    public static Integer isStaffSelectionRequired(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.IS_STAFF_SELECTION_REQUIRED);

        if(value==null||value.trim().isEmpty()){

            return IndicatorStatus.NO;
        }


        return Integer.parseInt(value);

    }

    public static Integer isStaffAuthenticationRequired(){


        // the aircraft by id
        String value=  getSettingsValueByKey(SettingConfig.IS_STAFF_AUTH_REQUIRED);

        if(value==null||value.trim().isEmpty()){

            return IndicatorStatus.NO;
        }


        return Integer.parseInt(value);

    }


}
