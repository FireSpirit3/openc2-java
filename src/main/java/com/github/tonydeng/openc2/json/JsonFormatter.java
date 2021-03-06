package com.github.tonydeng.openc2.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tonydeng.openc2.OpenC2Command;
import com.github.tonydeng.openc2.OpenC2Message;
import com.github.tonydeng.openc2.OpenC2Response;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;

/**
 * Jackson JSON methods to convert OpenC2Messages to and from JSON strings
 *
 * @author Tony Deng
 * @version V1.0
 **/
public class JsonFormatter {

    private JsonFormatter() {
    }

    /**
     * Convert an OpenC2Message object to a JSON string
     *
     * @param message     OpenC2Message object to be serialized into a JSON string
     * @param prettyPrint boolean to toggle if the return string is in human readable or not
     * @return String containing the JSON representation of the OpenC2Message object
     * @throws JsonProcessingException Exception thrown by the Jackson library
     */
    public static String getJson(@NonNull final OpenC2Message message, @NonNull final boolean prettyPrint)
            throws JsonProcessingException {
        val mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (prettyPrint) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        }
        return mapper.writeValueAsString(message);
    }


    /**
     * Read a OpenC2 JSON string and convert it into a OpenC2Command object
     *
     * @param json OpenC2Command JSON string
     * @return OpenC2Command object represented by the JSON string
     * @throws IOException Exception thrown by the Jackson library
     */
    public static OpenC2Command readOpenC2Command(@NonNull String json) throws IOException {
        return (OpenC2Command) read(json, OpenC2Command.class);
    }

    /**
     * Read a OpenC2 JSON string and convert it into a OpenC2Response object
     *
     * @param json response JSON string
     * @return OpenC2Response object
     * @throws IOException Exception thrown if there is a problem reading the JSON
     */
    public static OpenC2Response readOpenC2Response(@NonNull String json) throws IOException {
        return (OpenC2Response) read(json, OpenC2Response.class);
    }

    /**
     * Read a OpenC2 JSON string and convert it into a  object
     *
     * @param json  JSON String
     * @param clazz Object class
     * @return object represented by the JSON string
     * @throws IOException Exception thrown if there is a problem reading the JSON
     */
    private static Object read(@NonNull String json, @NonNull Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
