package com.gcdd.jacksonGuide.basicJacksonUnmarshalling.jacksonUnmarshallingJSONWithUnknownProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
