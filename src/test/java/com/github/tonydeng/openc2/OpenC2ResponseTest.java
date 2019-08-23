package com.github.tonydeng.openc2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tonydeng.openc2.header.Header;
import com.github.tonydeng.openc2.json.JsonFormatter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.github.tonydeng.openc2.utilities.StatusCode.BAD_REQUEST;
import static com.github.tonydeng.openc2.utilities.StatusCode.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author dengtao
 **/
@Slf4j
public class OpenC2ResponseTest {
    private static final String ID_VALUE = "TEST-id-1";
    private static final String VERSION_VALUE = "0.1.0";
    private static final String CONTENT_VALUE = "context";
    private static final String STATUS_TEXT_VALUE = "Successful";
    private static final String RESULTS_VALUE = "These are the results";
    private static final String RESP_ID = "CommandResp";


    private static String expected1 = "{\"id\":\"CommandResp\",\"id_ref\":\"complete\",\"status\":200}";
    private static String expected2 = "{\"id\":\"CommandResp\",\"id_ref\":\"complete\",\"status\":200,\"status_text\":\"Successful\",\"results\":\"These are the results\"}";
    private static String expected3 = "{\"header\":{\"version\":\"0.1.0\",\"id\":\"TEST-id-1\",\"content_type\":\"context\"},\"response\":{\"id\":\"CommandResp\",\"id_ref\":\"complete\",\"status\":200,\"status_text\":\"Successful\",\"results\":\"These are the results\"}}}";

    @Test
    public void testCodeCoverage() throws Exception {
        val response = OpenC2Response.builder()
                .status(OK.getValue())
                .id("CommandResp")
                .build();
        assertEquals(OK.getValue(), response.getStatus());
        response.setStatus(BAD_REQUEST.getValue());
        assertEquals(BAD_REQUEST.getValue(), response.getStatus());

        log.info("{}", response.toPrettyJson());
    }

    @Test
    void testGson() throws JsonProcessingException {
        val response = OpenC2Response.builder().status(OK.getValue())
                .id(RESP_ID).idRef("complete")
                .build();
        val response2 = new Gson().fromJson(response.toJson(), OpenC2Response.class);
        val response3 = new Gson().fromJson(expected1, OpenC2Response.class);
        assertEquals(response.toJson(), response2.toJson());
        assertEquals(response.toJson(), response3.toJson());
        assertEquals(response2.toJson(), response3.toJson());
    }

    @Test
    public void test1() throws Exception {

        val response = OpenC2Response.builder().status(OK.getValue())
                .id(RESP_ID).idRef("complete")
                .build();

        val response2 = JsonFormatter.readOpenC2Response(response.toJson());
        val response3 = JsonFormatter.readOpenC2Response(expected1);


        val responseJN = new ObjectMapper().readTree(response.toJson());
        val response2JN = new ObjectMapper().readTree(response2.toJson());
        val response3JN = new ObjectMapper().readTree(response3.toJson());

        assertEquals(responseJN, response2JN);
        assertEquals(responseJN, response3JN);
    }

    @Test
    public void test2() throws Exception {

        val response = OpenC2Response.builder().status(OK.getValue()).statusText("Successful")
                .id(RESP_ID).idRef("complete")
                .results(RESULTS_VALUE).build();

        val response2 = JsonFormatter.readOpenC2Response(response.toJson());
        val response3 = JsonFormatter.readOpenC2Response(expected2);

        val responseJN = new ObjectMapper().readTree(response.toJson());
        val response2JN = new ObjectMapper().readTree(response2.toJson());
        val response3JN = new ObjectMapper().readTree(response3.toJson());

        assertEquals(responseJN, response2JN);  // Verify that the object created from a string is the same
        assertEquals(responseJN, response3JN);  // Verify that the object from an external JSON string is the same

    }

    @Test
    public void test3() throws Exception {

        OpenC2Response response = OpenC2Response.builder().status(OK.getValue())
                .id(RESP_ID).idRef("complete").statusText(STATUS_TEXT_VALUE)
                .results(RESULTS_VALUE)
                .header(new Header(VERSION_VALUE, CONTENT_VALUE).setCommandId(ID_VALUE))
                .build();

        val response2 = JsonFormatter.readOpenC2Response(response.toJson());
        val response3 = JsonFormatter.readOpenC2Response(expected3);

        log.info("response json \n {}", response.toPrettyJson());
        log.info("response2 json \n {}", response2.toPrettyJson());
        log.info("response3 json \n {}", response3.toPrettyJson());

        val responseJN = new ObjectMapper().readTree(response.toJson());
        val response2JN = new ObjectMapper().readTree(response2.toJson());
        val response3JN = new ObjectMapper().readTree(response3.toJson());

        assertEquals(responseJN, response2JN);
        assertEquals(responseJN, response3JN);

        val inMsg = JsonFormatter.readOpenC2Response(expected3);
        assertTrue(inMsg.hasHeader());
        assertEquals(ID_VALUE, inMsg.getHeader().getCommandId());
        assertEquals(CONTENT_VALUE, inMsg.getHeader().getContentType());
    }
}
