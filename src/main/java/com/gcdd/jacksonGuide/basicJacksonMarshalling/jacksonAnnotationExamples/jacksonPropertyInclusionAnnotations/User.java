package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@AllArgsConstructor
public class User {
    public int id;
    public Name name;

    @JsonIgnoreType
    @AllArgsConstructor
    public static class Name {
        public String firstName;
        public String lastName;
    }
}
