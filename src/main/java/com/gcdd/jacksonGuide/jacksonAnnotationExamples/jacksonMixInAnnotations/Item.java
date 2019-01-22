package com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonMixInAnnotations;

import com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonPropertyInclusionAnnotations.User;
import lombok.AllArgsConstructor;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
@AllArgsConstructor
public class Item {
    public int id;
    public String itemName;
    public User owner;
}
