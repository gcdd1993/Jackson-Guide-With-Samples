package com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPolymorphicTypeHandlingAnnotations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class Zoo {
    public Animal animal;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "dog"),
            @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    public static class Animal {
        public String name;
    }

    @JsonTypeName("dog")
    public static class Dog extends Animal {
        public double barkVolume;

    }

    @JsonTypeName("cat")
    public static class Cat extends Animal {
        boolean likesCream;
        public int lives;

    }
}
