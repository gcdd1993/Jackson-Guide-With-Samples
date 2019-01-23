package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonIgnorePropertiesOnMarshalling.ignoreFieldsAtTheClassLevel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@JsonIgnoreProperties(value = {"intValue"})
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}