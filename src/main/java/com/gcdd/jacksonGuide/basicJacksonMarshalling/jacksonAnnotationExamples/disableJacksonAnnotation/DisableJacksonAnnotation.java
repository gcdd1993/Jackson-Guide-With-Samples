package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.disableJacksonAnnotation;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class DisableJacksonAnnotation {
    @Test
    public void whenDisablingAllAnnotations_thenAllDisabled() throws IOException {
        MyBean bean = new MyBean(1, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);
        String result = mapper.writeValueAsString(bean);
        System.out.println(result);

        assertThat(result, containsString("1"));
        assertThat(result, containsString("name"));
    }
}
