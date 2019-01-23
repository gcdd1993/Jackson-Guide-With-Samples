package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonMixInAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class JacksonMixInAnnotations {
    @Test
    public void whenSerializingUsingMixInAnnotation_thenCorrect() throws JsonProcessingException {
        Item item = new Item(1, "book", null);

        String result = new ObjectMapper().writeValueAsString(item);
        assertThat(result, containsString("owner"));
        System.out.println(result);

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(User.class, MyMixInForIgnoreType.class);

        result = mapper.writeValueAsString(item);
        System.out.println(result);
        assertThat(result, not(containsString("owner")));
    }
}
