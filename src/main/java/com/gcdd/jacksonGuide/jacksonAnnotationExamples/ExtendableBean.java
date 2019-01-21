package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
public class ExtendableBean {
    public String name;
    private Map<String, String> properties;

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
