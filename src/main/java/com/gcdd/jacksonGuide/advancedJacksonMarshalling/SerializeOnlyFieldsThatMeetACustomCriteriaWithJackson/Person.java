package com.gcdd.jacksonGuide.advancedJacksonMarshalling.SerializeOnlyFieldsThatMeetACustomCriteriaWithJackson;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@Data
@AllArgsConstructor
public class Person  implements Hidable {
    private String name;
    private Address address;
    private boolean hidden;
}
