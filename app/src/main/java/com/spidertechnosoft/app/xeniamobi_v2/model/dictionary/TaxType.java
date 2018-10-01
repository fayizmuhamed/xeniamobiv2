package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.HashMap;
import java.util.Map;

public enum TaxType {

    VAT(0),
    GST(1),
    NON_TAX(2);

    private int value;
    private static Map map = new HashMap<>();

    private TaxType(int value) {
        this.value = value;
    }

    static {
        for (TaxType taxType : TaxType.values()) {
            map.put(taxType.value, taxType);
        }
    }

    public static TaxType valueOf(int taxType) {
        return (TaxType) map.get(taxType);
    }
}
