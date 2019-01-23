package com.gcdd.jacksonGuide.basicJacksonMarshalling.usingOptionalWithJackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public class SimpleTest {
    @Test
    public void test1() throws JsonProcessingException {
        Book book = new Book();
        book.setTitle("Oliver Twist");
        book.setSubTitle(Optional.of("The Parish Boy's Progress"));
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(book);
        System.out.println(result);
    }

    @Test(expected = JsonMappingException.class)
    public void givenFieldWithValue_whenDeserializing_thenThrowException() throws IOException {
        String bookJson = "{ \"title\": \"Oliver Twist\", \"subTitle\": \"foo\" }";
        ObjectMapper mapper = new ObjectMapper();
        Book result = mapper.readValue(bookJson, Book.class);
    }

    @Test
    public void test2() throws JsonProcessingException {
        Book book = new Book();
        book.setTitle("Oliver Twist");
        book.setSubTitle(Optional.empty());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String serializedBook = mapper.writeValueAsString(book);
        System.out.println(serializedBook);
    }

    @Test
    public void givenFieldWithValue() throws IOException {
        String bookJson = "{ \"title\": \"Oliver Twist\", \"subTitle\": \"foo\" }";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        Book result = mapper.readValue(bookJson, Book.class);
    }
}
