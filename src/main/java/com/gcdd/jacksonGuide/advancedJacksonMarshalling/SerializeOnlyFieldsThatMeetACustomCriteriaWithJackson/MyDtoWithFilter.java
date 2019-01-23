package com.gcdd.jacksonGuide.advancedJacksonMarshalling.SerializeOnlyFieldsThatMeetACustomCriteriaWithJackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@Data
@NoArgsConstructor
@JsonFilter("myFilter")
public class MyDtoWithFilter {
    private int intValue;
}
