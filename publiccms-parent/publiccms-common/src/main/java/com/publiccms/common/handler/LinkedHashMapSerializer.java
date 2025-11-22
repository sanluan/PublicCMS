package com.publiccms.common.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class LinkedHashMapSerializer extends StdSerializer<LinkedHashMap<?, ?>> {
    public LinkedHashMapSerializer() {
        super(LinkedHashMap.class);
    }

    @Override
    public void serialize(LinkedHashMap<?, ?> value, JsonGenerator gen, SerializationContext provider) {
        gen.writeStartObject();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            gen.writePOJOProperty((String) entry.getKey(), entry.getValue());
        }
        gen.writeEndObject();
    }
}