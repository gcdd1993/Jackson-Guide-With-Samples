package com.gcdd.jacksonGuide;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gcdd.jacksonGuide.jacksonAnnotationExamples.*;
import com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.BeanWithIgnore;
import com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.PrivateBean;
import com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.User;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
public class JacksonAnnotationExamples {
    @Test
    public void whenSerializingUsingJsonAnyGetter_thenCorrect() throws JsonProcessingException {
//        ExtendableBean bean = new ExtendableBean("My bean");
//        bean.getProperties().put("attr1", "val1");
//        bean.getProperties().put("attr2", "val2");
//
//        String result = new ObjectMapper().writeValueAsString(bean);
//        System.out.println(result);
//        assertThat(result, containsString("attr1"));
//        assertThat(result, containsString("val1"));
    }

    @Test
    public void whenSerializingUsingJsonGetter_thenCorrect() throws JsonProcessingException {
        MyBean bean = new MyBean(1, "My bean");

        String result = new ObjectMapper().writeValueAsString(bean);
        System.out.println(result);
        assertThat(result, containsString("My bean"));
        assertThat(result, containsString("1"));
    }

    @Test
    public void whenSerializingUsingJsonPropertyOrder_thenCorrect() throws JsonProcessingException {
        MyBean bean = new MyBean(1, "My bean");

        String result = new ObjectMapper().writeValueAsString(bean);
        System.out.println(result);
        assertThat(result, containsString("My bean"));
        assertThat(result, containsString("1"));
    }

    @Test
    public void whenSerializingUsingJsonRawValue_thenCorrect() throws JsonProcessingException {
        RawBean bean = new RawBean("My bean", "{\"attr\":false}");

        String result = new ObjectMapper().writeValueAsString(bean);
        System.out.println(result);
        assertThat(result, containsString("My bean"));
        assertThat(result, containsString("{\"attr\":false}"));
    }

    @Test
    public void whenSerializingUsingJsonValue_thenCorrect() throws JsonParseException, IOException {
        String enumAsString = new ObjectMapper().writeValueAsString(TypeEnumWithValue.TYPE1);
        System.out.println(enumAsString);
        assertEquals(enumAsString, "\"Type A\"");
    }

    @Test
    public void whenSerializingUsingJsonRootName_thenCorrect() throws JsonProcessingException {
        UserWithRoot user = new UserWithRoot(1, "John");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String result = mapper.writeValueAsString(user);

        System.out.println(result);
        assertThat(result, containsString("John"));
        assertThat(result, containsString("user"));
    }

    @Test
    public void whenSerializingUsingJsonSerialize_thenCorrect() throws JsonProcessingException, ParseException {
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//
//        String toParse = "20-12-2014 02:30:00";
//        Date date = df.parse(toParse);
//        Event event = new Event("party", date);
//
//        String result = new ObjectMapper().writeValueAsString(event);
//        System.out.println(result);
//        assertThat(result, containsString(toParse));
    }

    @Test
    public void whenDeserializingUsingJsonCreator_thenCorrect() throws IOException {
        String json = "{\"id\":1,\"theName\":\"My bean\"}";

        BeanWithCreator bean = new ObjectMapper()
                .readerFor(BeanWithCreator.class)
                .readValue(json);
        assertEquals("My bean", bean.name);
    }

    @Test
    public void whenDeserializingUsingJsonInject_thenCorrect() throws IOException {
        String json = "{\"name\":\"My bean\"}";
        InjectableValues inject = new InjectableValues.Std()
                .addValue(int.class, 1);
        BeanWithInject bean = new ObjectMapper().reader(inject)
                .forType(BeanWithInject.class)
                .readValue(json);

        assertEquals("My bean", bean.name);
        assertEquals(1, bean.id);
    }

    @Test
    public void whenDeserializingUsingJsonAnySetter_thenCorrect() throws IOException {
        String json = "{\"name\":\"My bean\",\"attr2\":\"val2\",\"attr1\":\"val1\"}";

        ExtendableBean bean = new ObjectMapper()
                .readerFor(ExtendableBean.class)
                .readValue(json);

        assertEquals("My bean", bean.name);
        assertEquals("val2", bean.getProperties().get("attr2"));
    }

    @Test
    public void whenDeserializingUsingJsonSetter_thenCorrect() throws IOException {
        String json = "{\"id\":1,\"name\":\"My bean\"}";

        MyBean1 bean = new ObjectMapper()
                .readerFor(MyBean1.class)
                .readValue(json);
        assertEquals("My bean", bean.getName());
    }

    @Test
    public void whenDeserializingUsingJsonDeserialize_thenCorrect() throws IOException {
        String json = "{\"name\":\"party\",\"eventDate\":\"20-12-2014 02:30:00\"}";

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Event event = new ObjectMapper()
                .readerFor(Event.class)
                .readValue(json);

        assertEquals("20-12-2014 02:30:00", df.format(event.eventDate));
    }

    @Test
    public void whenSerializingUsingJsonIgnoreProperties_thenCorrect() throws JsonProcessingException {
        BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");
        String result = new ObjectMapper()
                .writeValueAsString(bean);
        System.out.println(result);
        assertThat(result, containsString("My bean"));
        assertThat(result, not(containsString("id")));
    }

    @Test
    public void whenSerializingUsingJsonIgnoreType_thenCorrect() throws JsonProcessingException, ParseException {
        User.Name name = new User.Name("John", "Doe");
        User user = new User(1, name);

        String result = new ObjectMapper().writeValueAsString(user);
        System.out.println(result);

        assertThat(result, containsString("1"));
        assertThat(result, not(containsString("name")));
        assertThat(result, not(containsString("John")));
    }

    @Test
    public void whenSerializingUsingJsonInclude_thenCorrect() throws JsonProcessingException {
        com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.MyBean bean = new com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.MyBean(1, null);
        String result = new ObjectMapper()
                .writeValueAsString(bean);
        System.out.println(result);

        assertThat(result, containsString("1"));
        assertThat(result, not(containsString("name")));
    }

    @Test
    public void whenSerializingUsingJsonAutoDetect_thenCorrect() throws JsonProcessingException {
        PrivateBean bean = new PrivateBean(1, "My bean");
        String result = new ObjectMapper()
                .writeValueAsString(bean);
        System.out.println(result);

        assertThat(result, containsString("1"));
        assertThat(result, containsString("My bean"));
    }
}
