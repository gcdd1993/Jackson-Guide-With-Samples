package com.gcdd.jacksonGuide.advancedJacksonMarshalling.SerializeOnlyFieldsThatMeetACustomCriteriaWithJackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public class SimpleTest {
    @Test
    public void test1() throws JsonProcessingException {
        PropertyFilter theFilter = new SimpleBeanPropertyFilter() {
            @Override
            public void serializeAsField
                    (Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
                    throws Exception {
                if (include(writer)) {
                    if (!writer.getName().equals("intValue")) {
                        writer.serializeAsField(pojo, jgen, provider);
                        return;
                    }
                    int intValue = ((MyDtoWithFilter) pojo).getIntValue();
                    if (intValue >= 0) {
                        writer.serializeAsField(pojo, jgen, provider);
                    }
                } else if (!jgen.canOmitFields()) { // since 2.3
                    writer.serializeAsOmittedField(pojo, jgen, provider);
                }
            }

            @Override
            protected boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            protected boolean include(PropertyWriter writer) {
                return true;
            }
        };
        FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
        MyDtoWithFilter dtoObject = new MyDtoWithFilter();
        dtoObject.setIntValue(1);

        ObjectMapper mapper = new ObjectMapper();
        String dtoAsString = mapper.writer(filters).writeValueAsString(dtoObject);
        System.out.println(dtoAsString);
    }

    @Test
    public void test2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(
                            SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                        if (Hidable.class.isAssignableFrom(desc.getBeanClass())) {
                            return new HidableSerializer((JsonSerializer<Object>) serializer);
                        }
                        return serializer;
                    }
                });
            }
        });
        Address ad1 = new Address("tokyo", "jp", true);
        Address ad2 = new Address("london", "uk", false);
        Address ad3 = new Address("ny", "usa", false);
        Person p1 = new Person("john", ad1, false);
        Person p2 = new Person("tom", ad2, true);
        Person p3 = new Person("adam", ad3, false);

        System.out.println(mapper.writeValueAsString(Arrays.asList(p1, p2, p3)));
    }

    @Test
    public void whenNotHidden_thenCorrect() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(
                            SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                        if (Hidable.class.isAssignableFrom(desc.getBeanClass())) {
                            return new HidableSerializer((JsonSerializer<Object>) serializer);
                        }
                        return serializer;
                    }
                });
            }
        });
        Address ad = new Address("ny", "usa", false);
        Person person = new Person("john", ad, false);
        String result = mapper.writeValueAsString(person);

        assertTrue(result.contains("name"));
        assertTrue(result.contains("john"));
        assertTrue(result.contains("address"));
        assertTrue(result.contains("usa"));
    }
}
