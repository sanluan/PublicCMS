package com.publiccms.common.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.ParameterizedType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ConfigurableObjectInputStream;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * ValueSerializer
 * 
 * @param <T>
 * 
 */
public class ValueSerializer<T> implements Serializer<T> {
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> clazz;

    @Override
    public byte[] serialize(final T graph) {
        if (null == graph) {
            return EMPTY_BYTES;
        }
        try {
            return Constants.objectMapper.writeValueAsBytes(graph);
        } catch (Exception e) {
            log.warn(CommonUtils.joinString("Fail to serializer graph. graph = ", graph), e);
            return EMPTY_BYTES;
        }
    }

    @Override
    public T deserialize(final byte[] bytes) {
        if (null == bytes || 0 == bytes.length) {
            return null;
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ConfigurableObjectInputStream(is, Thread.currentThread().getContextClassLoader())) {
            return Constants.objectMapper.readValue(bytes, getValueClass());
        } catch (Exception e) {
            log.warn("Fail to deserialize bytes.", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> getValueClass() {
        return null == clazz
                ? this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
                : clazz;
    }
}