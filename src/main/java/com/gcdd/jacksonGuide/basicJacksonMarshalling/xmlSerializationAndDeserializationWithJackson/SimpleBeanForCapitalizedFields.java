package com.gcdd.jacksonGuide.basicJacksonMarshalling.xmlSerializationAndDeserializationWithJackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@Data
public class SimpleBeanForCapitalizedFields {
    @JsonProperty("X")
    private int x = 1;
    private int y = 2;
}
