package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jacksonDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: gaochen
 * Date: 2019/1/24
 */
@Data
@AllArgsConstructor
public class Java8Event {
    private String name;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime date;
}
