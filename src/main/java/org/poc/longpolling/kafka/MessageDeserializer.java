package org.poc.longpolling.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class MessageDeserializer implements Deserializer<AccountStatusChangeEvent> {

    public static final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @Override
    public AccountStatusChangeEvent deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, AccountStatusChangeEvent.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
