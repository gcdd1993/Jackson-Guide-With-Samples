package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jsonMappingException;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public class MyDtoNoAccessors {
    String stringValue;
    int intValue;
    boolean booleanValue;

    public MyDtoNoAccessors() {
        super();
    }

    // no getters
}
