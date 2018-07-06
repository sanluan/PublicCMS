package com.publiccms.common.tools;

import java.io.IOException;

import com.publiccms.common.constants.Constants;

/**
 * 
 * JsonUtils
 * 
 */
public final class JsonUtils {

    /**
     * @param object
     * @return json string
     */
    public static String getString(Object object) {
        try {
            return Constants.objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }
}
