package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonDeserializationAnnotations;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
public class ExtendableBean {
    public String name;
    private Map<String, String> properties = new HashMap<>();

    @JsonAnySetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
