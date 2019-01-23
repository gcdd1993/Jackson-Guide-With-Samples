package com.gcdd.jacksonGuide.basicJacksonUnmarshalling.jacksonUnmarshallingJSONWithUnknownProperties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDtoIgnoreUnknown {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
