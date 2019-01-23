package com.gcdd.jacksonGuide.basicJacksonMarshalling.ignoreNullFieldsWithJackson;

import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
