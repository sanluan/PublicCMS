package com.publiccms.common.handler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LinkedHashMapSerializer extends StdSerializer<LinkedHashMap<?, ?>> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LinkedHashMapSerializer() {
        super(LinkedHashMap.class, false);
    }

    @Override
    public void serialize(LinkedHashMap<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            gen.writeObjectField((String) entry.getKey(), entry.getValue());
        }
        gen.writeEndObject();
    }
}