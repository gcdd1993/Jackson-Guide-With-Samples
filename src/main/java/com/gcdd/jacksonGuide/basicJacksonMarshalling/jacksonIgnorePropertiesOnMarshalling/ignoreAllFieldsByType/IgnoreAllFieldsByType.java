package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonIgnorePropertiesOnMarshalling.ignoreAllFieldsByType;

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
public class IgnoreAllFieldsByType {
    @Test
    public final void givenFieldTypeIsIgnored_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(String[].class, MyMixInForIgnoreType.class);
        MyDtoWithSpecialField dtoObject = new MyDtoWithSpecialField();
        dtoObject.setBooleanValue(true);

        String dtoAsString = mapper.writeValueAsString(dtoObject);
        System.out.println(dtoAsString);

        assertThat(dtoAsString, containsString("intValue"));
        assertThat(dtoAsString, containsString("booleanValue"));
        assertThat(dtoAsString, not(containsString("stringValue")));
    }
}
