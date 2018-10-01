package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.HashMap;
import java.util.Map;

public enum PrintingSize {

    FIFTY_EIGHT(0),
    EIGHTY(1);


    private int value;
    private static Map map = new HashMap<>();

    private PrintingSize(int value) {
        this.value = value;
    }

    static {
        for (PrintingSize printingSize : PrintingSize.values()) {
            map.put(printingSize.value, printingSize);
        }
    }

    public static PrintingSize valueOf(int printingSize) {
        return (PrintingSize) map.get(printingSize);
    }
}
