package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonSerializationAnnotations;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@AllArgsConstructor
public class Event {
    public String name;

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date eventDate;
}
