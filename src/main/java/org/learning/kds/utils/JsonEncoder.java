package org.learning.kds.utils;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.learning.kds.model.Metrics;

import java.io.Serializable;

public class JsonEncoder implements Serializable {

    private static final long serialVersionUID = -8809541393316790041L;

    private final JsonMapperFactory jsonMapperFactory;
    private transient ObjectMapper jsonMapper;

    public JsonEncoder(JsonMapperFactory jsonMapperFactory) {
        this.jsonMapperFactory = jsonMapperFactory;
    }

    public String encode(Metrics object) {
        if (jsonMapper == null) {
            jsonMapper = jsonMapperFactory.get();
        }

        try {
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialise as JSON", e);
        }
    }

    public interface JsonMapperFactory extends Serializable {

        ObjectMapper get();

    }

}
