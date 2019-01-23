package com.gcdd.jacksonGuide.advancedJacksonMarshalling.SerializeOnlyFieldsThatMeetACustomCriteriaWithJackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@JsonIgnoreProperties("hidden")
public interface Hidable {
    boolean isHidden();
}
