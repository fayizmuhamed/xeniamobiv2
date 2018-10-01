package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.HashMap;
import java.util.Map;

public enum PrinterConnectionType {

    BLUETOOTH_PRINTER(0),
    USB_PRINTER(1),
    WIFI_PRINTER(2);


    private int value;
    private static Map map = new HashMap<>();

    private PrinterConnectionType(int value) {
        this.value = value;
    }

    static {
        for (PrinterConnectionType printerConnectionType : PrinterConnectionType.values()) {
            map.put(printerConnectionType.value, printerConnectionType);
        }
    }

    public static PrinterConnectionType valueOf(int printerConnectionType) {
        return (PrinterConnectionType) map.get(printerConnectionType);
    }
}
