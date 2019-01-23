package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.disableJacksonAnnotation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "id"})
@AllArgsConstructor
public class MyBean {
    public int id;
    public String name;
}
