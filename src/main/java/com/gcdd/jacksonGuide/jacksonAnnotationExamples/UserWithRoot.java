package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/21
 */
@JsonRootName(value = "user")
@AllArgsConstructor
public class UserWithRoot {
    public int id;
    public String name;
}
