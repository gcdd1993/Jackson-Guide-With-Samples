# 总览
> 本快速教程说明了如何在序列化的时候更改字段名称来映射为另一个json属性

# 在序列化中更改字段名称

需要序列化的实体:

```java
public class MyDto {
    private String stringValue;

    public MyDto() {
        super();
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
```

序列化它将产生以下JSON：

```json
{"stringValue":"some value"}
```

如果我们要把序列化后的`stringValue`字段名称改为`strVal`，我们可以这样做：

```java
@JsonProperty("strVal")
public String getStringValue() {
    return stringValue;
}
```

现在，序列化时我们将会得到我们要的输出：

```json
{"strValue":"some value"}
```

测试代码:

```java
@Test
public void givenNameOfFieldIsChanged_whenSerializing_thenCorrect() throws JsonParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    MyDtoFieldNameChanged dtoObject = new MyDtoFieldNameChanged();
    dtoObject.setStringValue("a");

    String dtoAsString = mapper.writeValueAsString(dtoObject);
    System.out.println(dtoAsString);

    assertThat(dtoAsString, not(containsString("stringValue")));
    assertThat(dtoAsString, containsString("strVal"));
}
```

输出结果：

```json
{"strVal":"a"}
```

# 总结
> 本文通过使用`@JsonProperty`注解来实现了一种常用操作，即将实体序列化为遵循特定格式的json。