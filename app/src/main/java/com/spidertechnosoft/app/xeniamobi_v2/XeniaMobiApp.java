package com.spidertechnosoft.app.xeniamobi_v2;

import com.orm.SugarApp;
import com.spidertechnosoft.app.xeniamobi_v2.utils.PrintUtil;
import com.squareup.picasso.Picasso;


/**
 * Created by DELL on 12/25/2017.
 */

public class XeniaMobiApp extends SugarApp {


    @Override
    public void onCreate() {
        super.onCreate();
        //new Database(this);
        
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        PrintUtil.getInstance().initialize(this);
    }
}
