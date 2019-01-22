package com.gcdd.jacksonGuide.jacksonIgnorePropertiesOnMarshalling.ignoreAllFieldsByType;

import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@Data
public class MyDtoWithSpecialField {
    private String[] stringValue;
    private int intValue;
    private boolean booleanValue;
}
