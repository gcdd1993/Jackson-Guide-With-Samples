package com.gcdd.jacksonGuide.basicJacksonMarshalling.jacksonAnnotationExamples.jacksonGeneralAnnotations;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserWithIdentity {
    public int id;

    public UserWithIdentity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String name;
    public List<ItemWithIdentity> userItems;
}
