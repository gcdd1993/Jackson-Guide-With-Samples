package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jacksonDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: gaochen
 * Date: 2019/1/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String name;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;
}
