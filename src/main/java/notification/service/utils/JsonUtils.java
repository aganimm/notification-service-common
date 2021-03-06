package notification.service.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String EMPTY_JSON = "{}";
    public static final String EMPTY_ARRAY = "[]";

    public static String convertObjectToJson(@Nullable Object object) {
        return convertObjectToJson(object, EMPTY_JSON);
    }

    public static String convertIterableToJson(@Nullable Iterable iterable) {
        return convertObjectToJson(iterable, EMPTY_ARRAY);
    }

    @Nullable
    public static String getStringTrimOrNull(@Nullable String value) {
        if (value != null) {
            return value.trim().length() == 0 ? null : value.trim();
        }
        return null;
    }

    private static String convertObjectToJson(@Nullable Object object, String nullCase) {
        if (object == null) {
            return nullCase;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Can't create json for object: {}.", object);
            return nullCase;
        }
    }

    public static <T> T convertObjectWithType(Object object, Class<T> type) {
        return OBJECT_MAPPER.convertValue(object, type);
    }

    public static <T> List<T> convertJsonToList(@Nullable String json, Class<T> type) {
        if (json == null) {
            return Collections.emptyList();
        }
        if (EMPTY_JSON.equals(json)) {
            return Collections.emptyList();
        }

        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(ArrayList.class, type));
        } catch (IOException ex) {
            logger.warn("Can't parse json: {}. Data was lost.", json);
            return Collections.emptyList();
        }
    }

    @Nullable
    public static <T> T convertJsonToObject(@Nullable String json, Class<T> type) {

        if (EMPTY_JSON.equals(json)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException ex) {
            logger.warn("Can't parse json: {}. Data was lost.", json);
            return null;
        }
    }
}
