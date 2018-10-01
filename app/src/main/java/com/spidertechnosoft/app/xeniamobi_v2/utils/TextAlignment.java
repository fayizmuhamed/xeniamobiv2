package com.spidertechnosoft.app.xeniamobi_v2.utils;

import java.util.HashMap;
import java.util.Map;

public enum TextAlignment {
    LEFT(0),
    CENTER(1),
    RIGHT(2);

    private int value;
    private static Map map = new HashMap<>();

    private TextAlignment(int value) {
        this.value = value;
    }

    static {
        for (TextAlignment textAlignment : TextAlignment.values()) {
            map.put(textAlignment.value, textAlignment);
        }
    }

    public static TextAlignment valueOf(int textAlignment) {
        return (TextAlignment) map.get(textAlignment);
    }

    public int getValue() {
        return value;
    }
}
