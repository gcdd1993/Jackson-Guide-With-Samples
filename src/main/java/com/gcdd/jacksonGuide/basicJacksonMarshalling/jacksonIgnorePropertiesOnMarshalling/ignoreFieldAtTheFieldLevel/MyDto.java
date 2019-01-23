package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonIgnorePropertiesOnMarshalling.ignoreFieldAtTheFieldLevel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    @JsonIgnore
    private int intValue;
    private boolean booleanValue;
}