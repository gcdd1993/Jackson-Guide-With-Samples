package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonDeserializationAnnotations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.customJacksonAnnotation.CustomDateDeserializer;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@NoArgsConstructor
public class Event {
    public String name;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date eventDate;
}
