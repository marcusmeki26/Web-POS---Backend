
package com.project.webpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webpos.Entity.DTO.SearchRequest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JacksonRecordTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRecordDeserialization() throws Exception {

        String json = """
            {
                "filters": [
                    {
                        "field": "receipt_no",
                        "operator": "<>",
                        "value": "R0001"
                    }
                ]
            }
            """;

        SearchRequest request =
            objectMapper.readValue(json, SearchRequest.class);

        System.out.println("Deserialized result: " + request);
        System.out.println("Filters: " + request.filters());

        assertNotNull(request.filters());
    }
}
