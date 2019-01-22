package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@Data
@NoArgsConstructor
public class MyBean1 {
    public int id;
    private String name;

    @JsonSetter("name")
    public void setTheName(String name) {
        this.name = name;
    }
}
