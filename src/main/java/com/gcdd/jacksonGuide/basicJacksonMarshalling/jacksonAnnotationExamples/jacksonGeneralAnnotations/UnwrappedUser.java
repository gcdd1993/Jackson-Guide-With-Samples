package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonGeneralAnnotations;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@AllArgsConstructor
public class UnwrappedUser {
    public int id;

    @JsonUnwrapped
    public Name name;

    @AllArgsConstructor
    public static class Name {
        public String firstName;
        public String lastName;
    }
}
