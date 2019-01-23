package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
