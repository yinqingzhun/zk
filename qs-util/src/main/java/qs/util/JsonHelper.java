package qs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class JsonHelper {
   
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object o, boolean prettyPrinter) {
        try {
            if (prettyPrinter)
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<T> clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(String s, TypeReference clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
