# 总览
> 在本快速教程中，我们将分析没有1getter1的实体的序列化以及Jackson `JsonMappingException`异常的解决方案。

# 问题

默认情况下，Jackson 2.x仅适用于公共字段或具有公共`getter`方法的字段 - 序列化所有字段为private或package private的实体将失败：

```java
public class MyDtoNoAccessors {
    String stringValue;
    int intValue;
    boolean booleanValue;
 
    public MyDtoNoAccessors() {
        super();
    }
 
    // no getters
}
```

测试代码：

```java
@Test(expected = JsonMappingException.class)
public void givenObjectHasNoAccessors_whenSerializing_thenException() throws JsonParseException, IOException {
    String dtoAsString = new ObjectMapper().writeValueAsString(new MyDtoNoAccessors());

    assertThat(dtoAsString, notNullValue());
}
```