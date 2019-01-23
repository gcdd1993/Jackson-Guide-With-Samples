package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonChangeNameOfField;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class MyDtoFieldNameChanged {
    private String stringValue;

    public MyDtoFieldNameChanged() {
        super();
    }

    @JsonProperty("strVal")
    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
