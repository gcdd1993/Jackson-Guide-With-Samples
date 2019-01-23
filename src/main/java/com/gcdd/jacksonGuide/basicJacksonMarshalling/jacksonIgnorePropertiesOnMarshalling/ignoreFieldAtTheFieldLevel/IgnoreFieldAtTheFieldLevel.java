package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonIgnorePropertiesOnMarshalling.ignoreFieldAtTheFieldLevel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class IgnoreFieldAtTheFieldLevel {
    @Test
    public void givenFieldIsIgnoredDirectly_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyDto dtoObject = new MyDto();

        String dtoAsString = mapper.writeValueAsString(dtoObject);
        System.out.println(dtoAsString);

        assertThat(dtoAsString, not(containsString("intValue")));
    }
}
