package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@NoArgsConstructor
public class Event {
    public String name;

    //    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date eventDate;
}
