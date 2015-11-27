package com.publiccms.common.index;

import org.hibernate.search.bridge.TwoWayStringBridge;

public class StringBridge implements TwoWayStringBridge {

    public static final StringBridge INSTANCE = new StringBridge();

    @Override
    public Object stringToObject(String stringValue) {
        return stringValue;
    }

    @Override
    public String objectToString(Object object) {
        return String.valueOf((Integer) object);
    }
}
