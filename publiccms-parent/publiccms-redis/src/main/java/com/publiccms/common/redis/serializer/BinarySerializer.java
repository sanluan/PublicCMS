package com.publiccms.common.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * BinarySerializer
 * 
 * @param <T>
 * 
 */
public class BinarySerializer<T> implements Serializer<T> {
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public byte[] serialize(final T graph) {
        if (null == graph) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(os);) {
            oos.writeObject(graph);
            oos.flush();
            return os.toByteArray();
        } catch (Exception e) {
            log.warn("Fail to serializer graph. graph=" + graph, e);
            return EMPTY_BYTES;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(final byte[] bytes) {
        if (null == bytes || 0 == bytes.length) {
            return null;
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(is);) {
            return (T) ois.readObject();
        } catch (Exception e) {
            log.warn("Fail to deserialize bytes.", e);
            return null;
        }
    }
}