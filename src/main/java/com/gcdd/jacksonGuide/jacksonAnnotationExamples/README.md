# 总览
在本文中，我们将深入探讨Jackson注解。我们将学习到如何使用现有注解，如何创建自定义注释以及如何禁用它们。

# Jackson序列化注解
> 首先,让我们看一下序列化注解

## @JsonAnyGetter
> `@JsonAnyGetter`注解允许使用Map字段作为标准属性。

下面是一个快速入门的示例,ExtendableBean实体具有name属性和一组以键/值对形式的可扩展属性:

```java
public class ExtendableBean {
    public String name;
    private Map<String, String> properties;

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
```

当我们序列化这个实体的一个实例时，我们将Map中的所有键值作为标准的普通属性：

```json
{
    "name":"My bean",
    "attr2":"val2",
    "attr1":"val1"
}
```

用测试代码测试一下这个实体的序列化:

```java
@Test
public void whenSerializingUsingJsonAnyGetter_thenCorrect() throws JsonProcessingException {
    ExtendableBean bean = new ExtendableBean("My bean");
    bean.getProperties().put("attr1", "val1");
    bean.getProperties().put("attr2", "val2");

    String result = new ObjectMapper().writeValueAsString(bean);
    System.out.println(result);
    assertThat(result, containsString("attr1"));
    assertThat(result, containsString("val1"));
}
```

## @JsonGetter
> `@JsonGetter`注解是`@JsonProperty`注解的替代，用于将指定的方法标记为getter方法。

在以下示例中,我们将方法getTheName()指定为MyBean实体的name属性的getter方法:

```java
public class MyBean {
    public int id;
    private String name;

    @JsonGetter("name")
    public String getTheName() {
        return name;
    }
}
```

以下是它在实践中的工作原理:

```java
@Test
public void whenSerializingUsingJsonGetter_thenCorrect() throws JsonProcessingException {
    MyBean bean = new MyBean(1, "My bean");

    String result = new ObjectMapper().writeValueAsString(bean);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
}
```

## @JsonPropertyOrder
> `@JsonPropertyOrder`注释用于指定序列化的属性顺序。

让我们为MyBean实体的属性设置自定义顺序：

```java
@JsonPropertyOrder({ "name", "id" })
public class MyBean {
    public int id;
    public String name;
}
```

这是序列化的输出：

```json
{
    "name":"My bean",
    "id":1
}
```

一个简单的测试:

```java
@Test
public void whenSerializingUsingJsonPropertyOrder_thenCorrect() throws JsonProcessingException {
    MyBean bean = new MyBean(1, "My bean");

    String result = new ObjectMapper().writeValueAsString(bean);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
}
```

## @JsonRawValue
> `@JsonRawValue`用于指示Jackson完全按原样序列化属性。

在下面的示例中,我们使用@JsonRawValue将一些自定义JSON嵌入为实体的值：

```java
@AllArgsConstructor
public class RawBean {
    public String name;

    @JsonRawValue
    public String json;
}
```

序列化实体的输出是：

```json
{
    "name":"My bean",
    "json":{
        "attr":false
    }
}
```

一个简单的测试：

```java
@Test
public void whenSerializingUsingJsonRawValue_thenCorrect() throws JsonProcessingException {
    RawBean bean = new RawBean("My bean", "{\"attr\":false}");

    String result = new ObjectMapper().writeValueAsString(bean);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("{\"attr\":false}"));
}
```

## @JsonValue
> `@JsonValue`表示应该用于序列化整个实例的单个方法。

例如，在枚举中 - 我们使用@JsonValue注释getName，以便通过其名称序列化任何此类实体：

```java
@AllArgsConstructor
public enum TypeEnumWithValue {
    TYPE1(1, "Type A"),
    TYPE2(2, "Type 2");

    private Integer id;
    private String name;

    // standard constructors

    @JsonValue
    public String getName() {
        return name;
    }
}
```

我们的测试：

```java
@Test
public void whenSerializingUsingJsonValue_thenCorrect() throws JsonParseException, IOException {
    String enumAsString = new ObjectMapper().writeValueAsString(TypeEnumWithValue.TYPE1);
    assertEquals(enumAsString, "\"Type A\"");
}
```

## @JsonRootName
> 如果启用了包装，则使用`@JsonRootName`注释来指定要使用的根包装器的名称。

包装意味着不是将用户序列化为类似于:

```json
{
    "id": 1,
    "name": "John"
}
```

它会像这样包裹起来:

```json
{
    "User": {
        "id": 1,
        "name": "John"
    }
}
```

那么,让我们看一个例子:我们将使用`@JsonRootName`注释来指示这个潜在的包装器实体的名称：

```java
@JsonRootName(value = "user")
public class UserWithRoot {
    public int id;
    public String name;
}
```

默认情况下，包装器的名称将是类的名称:`UserWithRoot`。通过使用注解，我们可以获得看起来更干净的用户：

```java
@Test
public void whenSerializingUsingJsonRootName_thenCorrect() throws JsonProcessingException {
    UserWithRoot user = new UserWithRoot(1, "John");

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    String result = mapper.writeValueAsString(user);

    assertThat(result, containsString("John"));
    assertThat(result, containsString("user"));
}
```