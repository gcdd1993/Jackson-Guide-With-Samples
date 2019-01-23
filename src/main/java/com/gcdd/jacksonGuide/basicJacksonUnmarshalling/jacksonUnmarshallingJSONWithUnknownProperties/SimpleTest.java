package com.gcdd.jacksonGuide.basicJacksonUnmarshalling.jacksonUnmarshallingJSONWithUnknownProperties;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public class SimpleTest {
    @Test(expected = UnrecognizedPropertyException.class)
    public void givenJsonHasUnknownValues_whenDeserializing_thenException() throws JsonParseException, JsonMappingException, IOException {
        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();
        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);
    }

    @Test
    public void givenJsonHasUnknownValuesButJacksonIsIgnoringUnknowns_whenDeserializing_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);
        assertThat(readValue.getStringValue(), equalTo("a"));
        assertThat(readValue.isBooleanValue(), equalTo(true));
        assertThat(readValue.getIntValue(), equalTo(1));
    }

    @Test
    public void givenJsonHasUnknownValuesButIgnoredOnClass_whenDeserializing_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();

        MyDtoIgnoreUnknown readValue = mapper
                .readValue(jsonAsString, MyDtoIgnoreUnknown.class);

        assertNotNull(readValue);
        assertThat(readValue.getStringValue(), equalTo("a"));
        assertThat(readValue.isBooleanValue(), equalTo(true));
        assertThat(readValue.getIntValue(), equalTo(1));
    }

    @Test
    public void givenNotAllFieldsHaveValuesInJson_whenDeserializingAJsonToAClass_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
        String jsonAsString = "{\"stringValue\":\"a\",\"booleanValue\":true}";
        ObjectMapper mapper = new ObjectMapper();

        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);
        assertThat(readValue.getStringValue(), equalTo("a"));
        assertThat(readValue.isBooleanValue(), equalTo(true));
    }
}
