package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum UserType {
    SUPER_ADMIN(0),
    ADMIN(1),
    USER(2),
    STAFF(3);

    private int value;
    private static Map map = new HashMap<>();

    private UserType(int value) {
        this.value = value;
    }

    static {
        for (UserType userType : UserType.values()) {
            map.put(userType.value, userType);
        }
    }

    public static UserType valueOf(int userTye) {
        return (UserType) map.get(userTye);
    }


}
