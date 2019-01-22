package com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonGeneralAnnotations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class UserWithRef {
    public int id;
    public String name;

    @JsonBackReference
    public List<ItemWithRef> userItems;

    public UserWithRef(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
