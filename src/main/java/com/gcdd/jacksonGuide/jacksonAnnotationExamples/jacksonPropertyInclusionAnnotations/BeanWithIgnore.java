package com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@AllArgsConstructor
//@JsonIgnoreProperties({ "id" })
public class BeanWithIgnore {
    @JsonIgnore
    public int id;
    public String name;
}
