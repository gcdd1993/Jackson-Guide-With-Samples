package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jacksonDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * @author: gaochen
 * Date: 2019/1/24
 */
@Data
@AllArgsConstructor
public class JodoEvent {
    private String name;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private DateTime date;
}
