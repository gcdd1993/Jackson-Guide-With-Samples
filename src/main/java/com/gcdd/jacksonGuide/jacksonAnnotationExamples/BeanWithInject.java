package com.gcdd.jacksonGuide.jacksonAnnotationExamples;

import com.fasterxml.jackson.annotation.JacksonInject;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class BeanWithInject {
    @JacksonInject
    public int id;

    public String name;
}
