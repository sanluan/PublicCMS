package com.publiccms.common.tools;

import java.io.IOException;

import com.publiccms.common.base.Base;

/**
 * 
 * JsonUtils
 * 
 */
public final class JsonUtils implements Base {

    /**
     * @param object
     * @return
     */
    public static String getString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }
}
