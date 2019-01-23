package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.customJacksonAnnotation;

import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@CustomAnnotation
@AllArgsConstructor
public class BeanWithCustomAnnotation {
    public int id;
    public String name;
    public Date dateCreated;

}
