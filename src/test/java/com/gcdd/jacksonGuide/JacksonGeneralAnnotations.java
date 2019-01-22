package com.gcdd.jacksonGuide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcdd.jacksonGuide.jacksonAnnotationExamples.jacksonGeneralAnnotations.MyBean;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author: gaochen
 * Date: 2019/1/22
 */
public class JacksonGeneralAnnotations {
    @Test
    public void whenUsingJsonProperty_thenCorrect() throws IOException {
        MyBean bean = new MyBean();
        bean.id = 1;
        bean.setTheName("My bean");
        String result = new ObjectMapper().writeValueAsString(bean);

        assertThat(result, containsString("My bean"));
        assertThat(result, containsString("1"));

        MyBean resultBean = new ObjectMapper()
                .readerFor(MyBean.class)
                .readValue(result);
        assertEquals("My bean", resultBean.getTheName());
    }
}
