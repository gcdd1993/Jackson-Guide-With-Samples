package com.gcdd.jacksonGuide.basicJacksonMarshalling.ignoreNullFieldsWithJackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class AllTest {
    @Test
    public void givenNullsIgnoredOnClass_whenWritingObjectWithNullField_thenIgnored() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyDto dtoObject = new MyDto();

        String dtoAsString = mapper.writeValueAsString(dtoObject);
        System.out.println(dtoAsString);

        assertThat(dtoAsString, containsString("intValue"));
        assertThat(dtoAsString, not(containsString("stringValue")));
    }

    @Test
    public void givenNullsIgnoredGlobally_whenWritingObjectWithNullField_thenIgnored() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MyDto dtoObject = new MyDto();

        String dtoAsString = mapper.writeValueAsString(dtoObject);

        assertThat(dtoAsString, containsString("intValue"));
        assertThat(dtoAsString, containsString("booleanValue"));
        assertThat(dtoAsString, not(containsString("stringValue")));
    }
}
