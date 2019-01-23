package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class MyBean {
    public int id;
    public String name;
}
