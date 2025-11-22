package com.publiccms.common.tools;

import com.publiccms.common.constants.Constants;

import tools.jackson.core.JacksonException;

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
        } catch (JacksonException e) {
            return null;
        }
    }
}
