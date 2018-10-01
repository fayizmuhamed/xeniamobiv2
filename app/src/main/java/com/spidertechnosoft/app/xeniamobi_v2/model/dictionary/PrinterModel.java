package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.HashMap;
import java.util.Map;

public enum PrinterModel {

    OTHER(0),
    CIONTEK(1),
    SUNMI_V1(2);

    private int value;
    private static Map map = new HashMap<>();

    private PrinterModel(int value) {
        this.value = value;
    }

    static {
        for (PrinterModel printerModel : PrinterModel.values()) {
            map.put(printerModel.value, printerModel);
        }
    }

    public static PrinterModel valueOf(int deviceType) {
        return (PrinterModel) map.get(deviceType);
    }
}
