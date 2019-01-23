package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonIgnorePropertiesOnMarshalling.ignoreFieldsUsingFilters;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@JsonFilter("myFilter")
@Data
public class MyDtoWithFilter {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
