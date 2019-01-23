package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jsonMappingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public class SimpleTest {
    @Test(expected = JsonMappingException.class)
    public void givenObjectHasNoAccessors_whenSerializing_thenException() throws JsonParseException, IOException {
        String dtoAsString = new ObjectMapper().writeValueAsString(new MyDtoNoAccessors());

        assertThat(dtoAsString, notNullValue());
    }
}
