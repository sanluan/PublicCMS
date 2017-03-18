package com.sanluan.common.tools;

import java.io.IOException;

import com.sanluan.common.api.Json;
import com.sanluan.common.base.Base;

/**
 * 
 * JsonUtils
 * 
 */
public final class JsonUtils extends Base implements Json {

    public static String getString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }
}
