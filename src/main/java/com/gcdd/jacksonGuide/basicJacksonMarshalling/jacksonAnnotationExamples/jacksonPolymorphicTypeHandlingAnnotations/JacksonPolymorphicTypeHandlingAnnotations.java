package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonPolymorphicTypeHandlingAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/22
 * Jackson Polymorphic Type Handling Annotations test codes
 */
public class JacksonPolymorphicTypeHandlingAnnotations {

    @Test
    public void whenDeserializingPolymorphic_thenCorrect() throws IOException {
        String json = "{\"animal\":{\"name\":\"lacy\",\"type\":\"cat\"}}";

        Zoo zoo = new ObjectMapper()
                .readerFor(Zoo.class)
                .readValue(json);

        assertEquals("lacy", zoo.animal.name);
        assertEquals(Zoo.Cat.class, zoo.animal.getClass());
    }

    @Test
    public void whenSerializingPolymorphic_thenCorrect() throws JsonProcessingException {
        Zoo.Dog dog = new Zoo.Dog("lacy");
        Zoo zoo = new Zoo(dog);

        String result = new ObjectMapper()
                .writeValueAsString(zoo);
        System.out.println(result);

        assertThat(result, containsString("type"));
        assertThat(result, containsString("dog"));
    }
}
