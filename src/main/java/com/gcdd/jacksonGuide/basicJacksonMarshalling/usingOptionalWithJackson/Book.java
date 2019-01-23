package com.gcdd.jacksonGuide.basicJacksonMarshalling.usingOptionalWithJackson;

import lombok.Data;

import java.util.Optional;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
@Data
public class Book {
    String title;
    Optional<String> subTitle;
}
