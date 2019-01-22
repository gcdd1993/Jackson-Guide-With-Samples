package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@Data
public class ExtendableBean {
    public String name;
    private Map<String, String> properties = new HashMap<>();
//
//    public ExtendableBean(String name) {
//        this.name = name;
//    }

//    @JsonAnyGetter
    @JsonAnySetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
