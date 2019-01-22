# 总览
> 本快速教程将介绍如何在序列化java类时使用Jackson 2.x忽略空字段。

#　在类上忽略空字段的Jackson注解
> Jackson允许在类上控制这种行为

```java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyDto {
    private String stringValue;
    private int intValue;
}
```

或者，更精确地在字段上使用注解:

```java
@Data
public class MyDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stringValue;
    private int intValue;
}
```

现在，我们应该能够测试空值确实不是最终JSON输出的一部分：

```java
@Test
public void givenNullsIgnoredOnClass_whenWritingObjectWithNullField_thenIgnored() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    MyDto dtoObject = new MyDto();

    String dtoAsString = mapper.writeValueAsString(dtoObject);
    System.out.println(dtoAsString);
    
    assertThat(dtoAsString, containsString("intValue"));
    assertThat(dtoAsString, not(containsString("stringValue")));
}
```

# Jackson设置全局忽略Null字段
> `Jackson`还允许在`ObjectMapper`上全局配置此行为

```java
mapper.setSerializationInclusion(Include.NON_NULL);
```

现在，将忽略通过此`mapper`序列化的任何类中的任何Null字段：

```java
@Test
public void givenNullsIgnoredGlobally_whenWritingObjectWithNullField_thenIgnored() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    MyDto dtoObject = new MyDto();

    String dtoAsString = mapper.writeValueAsString(dtoObject);

    assertThat(dtoAsString, containsString("intValue"));
    assertThat(dtoAsString, containsString("booleanValue"));
    assertThat(dtoAsString, not(containsString("stringValue")));
}
```

这是测试使用的DTO:

```java
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
```

# 总结
