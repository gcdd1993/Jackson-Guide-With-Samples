# 总览
> 在本文中，我们将看看Jackson 2.x的反序列化过程，特别是如何处理具有未知属性的JSON。

# 带有额外/未知字段的JSON反序列化

JSON是一个个键值对组成的字符串，大多数情况下，我们需要将它映射到具有一定数量字段的预定义Java对象。本节的目标是简单地忽略任何无法映射到现有java字段的JSON属性。

例如，假设我们需要将JSON反序列化到以下java实体：

```java
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
```

## 未知字段上的`UnrecognizedPropertyException`
> 尝试将具有未知属性的JSON反序列化为此Java实体将抛出异常`com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException`：

```java
@Test(expected = UnrecognizedPropertyException.class)
public void givenJsonHasUnknownValues_whenDeserializing_thenException() throws JsonParseException, JsonMappingException, IOException {
    String jsonAsString =
            "{\"stringValue\":\"a\"," +
                    "\"intValue\":1," +
                    "\"booleanValue\":true," +
                    "\"stringValue2\":\"something\"}";
    ObjectMapper mapper = new ObjectMapper();
    MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

    assertNotNull(readValue);
}
```

上面的代码执行后会抛出异常：

```
com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: 
Unrecognized field "stringValue2" (class org.baeldung.jackson.ignore.MyDto), 
not marked as ignorable (3 known properties: "stringValue", "booleanValue", "intValue"])
```

## 使用`ObjectMapper`处理未知字段

我们现在可以配置完整的`ObjectMapper`来忽略JSON中的未知属性：

```java
new ObjectMapper()
  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
```

然后我们应该能够将这种JSON读入预定义的Java实体：

```java
@Test
public void givenJsonHasUnknownValuesButJacksonIsIgnoringUnknowns_whenDeserializing_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
    String jsonAsString =
            "{\"stringValue\":\"a\"," +
                    "\"intValue\":1," +
                    "\"booleanValue\":true," +
                    "\"stringValue2\":\"something\"}";
    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
    assertThat(readValue.getIntValue(), equalTo(1));
}
```

## 在类上处理未知字段

我们还可以将单个类标记为接受未知字段，而不是整个Jackson `ObjectMapper`：

```java
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDtoIgnoreUnknown {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
```

现在，我们应该能够像以前一样测试相同的行为，简单地忽略未知字段并且仅映射已知字段：

```java
@Test
public void givenJsonHasUnknownValuesButIgnoredOnClass_whenDeserializing_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
    String jsonAsString =
            "{\"stringValue\":\"a\"," +
                    "\"intValue\":1," +
                    "\"booleanValue\":true," +
                    "\"stringValue2\":\"something\"}";
    ObjectMapper mapper = new ObjectMapper();

    MyDtoIgnoreUnknown readValue = mapper
            .readValue(jsonAsString, MyDtoIgnoreUnknown.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
    assertThat(readValue.getIntValue(), equalTo(1));
}
```

# 反序列化不完整的json

与其他未知字段类似，解组不完整的JSON（一个不包含java类中所有字段的JSON）对Jackson来说不​​是问题：

```java
@Test
public void givenNotAllFieldsHaveValuesInJson_whenDeserializingAJsonToAClass_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
    String jsonAsString = "{\"stringValue\":\"a\",\"booleanValue\":true}";
    ObjectMapper mapper = new ObjectMapper();

    MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
}
```