package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonSerializationAnnotations;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@AllArgsConstructor
public class RawBean {
    public String name;

    @JsonRawValue
    public String json;
}
