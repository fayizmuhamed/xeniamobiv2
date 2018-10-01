package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.HashMap;
import java.util.Map;

public enum  LocationType {
    STATE(0),
    UNION_TERRITORY(1);

    private int value;
    private static Map map = new HashMap<>();

    private LocationType(int value) {
        this.value = value;
    }

    static {
        for (LocationType locationType : LocationType.values()) {
            map.put(locationType.value, locationType);
        }
    }

    public static LocationType valueOf(int locationType) {
        return (LocationType) map.get(locationType);
    }
}
