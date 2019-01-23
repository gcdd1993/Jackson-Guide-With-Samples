package com.gcdd.jacksonGuide.advancedJacksonMarshalling.SerializeOnlyFieldsThatMeetACustomCriteriaWithJackson;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@Data
@AllArgsConstructor
public class Address implements Hidable {
    private String city;
    private String country;
    private boolean hidden;
}
