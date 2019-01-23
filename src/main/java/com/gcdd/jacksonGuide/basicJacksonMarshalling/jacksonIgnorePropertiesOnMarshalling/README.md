# 总览
> 本教程将介绍在使用Jackson 2.x将对象序列化为JSON时如何忽略某些字段。
  
当Jackson默认值不够而且我们需要精确控制序列化的内容时,这非常有用,并且有几种方法可以忽略属性。

# 在类级别忽略某些字段
> 我们可以在类级别上忽略特定字段，使用`@JsonIgnoreProperties`注解并按名称指定字段

```java
@JsonIgnoreProperties(value = {"intValue"})
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
```

我们现在可以测试，在将对象写入JSON之后，该字段确实不是输出的一部分:

```java
@Test
public void givenFieldIsIgnoredByName_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    MyDto dtoObject = new MyDto();

    String dtoAsString = mapper.writeValueAsString(dtoObject);
    System.out.println(dtoAsString);

    assertThat(dtoAsString, not(containsString("intValue")));
}
```

输出结果:

```json
{
    "stringValue":null,
    "booleanValue":false
}
```

# 在字段上忽略某些字段
> 我们也可以直接通过字段上的`@JsonIgnore`注解忽略一个字段

```java
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    @JsonIgnore
    private int intValue;
    private boolean booleanValue;
}
```

我们现在可以测试intValue字段确实不是序列化JSON输出的一部分:

```java
@Test
public void givenFieldIsIgnoredDirectly_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    MyDto dtoObject = new MyDto();

    String dtoAsString = mapper.writeValueAsString(dtoObject);
    System.out.println(dtoAsString);

    assertThat(dtoAsString, not(containsString("intValue")));
}
```

输出结果:

```json
{
    "stringValue":null,
    "booleanValue":false
}
```

# 按类型忽略所有字段
> 最后，我们可以使用`@JsonIgnoreType`注释忽略指定类型的所有字段

如果我们控制类型，那么我们可以直接注释该类:

```java
@JsonIgnoreType
public class SomeType { ... }
```

但是，我们通常无法控制类本身。在这种情况下，我们可以充分利用`Jackson mixins`。

首先,我们为我们要忽略的类型定义一个MixIn，并用`@JsonIgnoreType`注释:

```java
@JsonIgnoreType
public class MyMixInForIgnoreType {}
```

然后我们注册mixin以在序列化期间替换（并忽略）所有String[]类型:

```java
mapper.addMixInAnnotations(String[].class, MyMixInForIgnoreType.class);
```

此时，将忽略所有String数组，而不是将它们序列化为JSON:

```java
@Test
public final void givenFieldTypeIsIgnored_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.addMixIn(String[].class, MyMixInForIgnoreType.class);
    MyDtoWithSpecialField dtoObject = new MyDtoWithSpecialField();
    dtoObject.setBooleanValue(true);

    String dtoAsString = mapper.writeValueAsString(dtoObject);
    System.out.println(dtoAsString);

    assertThat(dtoAsString, containsString("intValue"));
    assertThat(dtoAsString, containsString("booleanValue"));
    assertThat(dtoAsString, not(containsString("stringValue")));
}
```

输出结果:

```json
{
    "intValue":0,
    "booleanValue":true
}
```

这是我们上面用到的DTO:

```java
@NoArgsConstructor
@Data
public class MyDto {
    private String stringValue;
    @JsonIgnore
    private int intValue;
    private boolean booleanValue;
}
```

**【注意】**:从版本2.5开始,似乎我们不能使用此方法来忽略原始数据类型，但我们可以将它用于自定义数据类型和数组。

# 使用过滤器忽略字段
> 最后，我们还可以使用Filters来忽略Jackson中的特定字段

首先，我们需要在java对象上定义过滤器:

```java
@JsonFilter("myFilter")
public class MyDtoWithFilter { ... }
```

然后，我们定义一个忽略intValue字段的简单过滤器:

```java
SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
  .serializeAllExcept("intValue");
FilterProvider filters = new SimpleFilterProvider()
  .addFilter("myFilter", theFilter);
```

现在我们可以序列化对象并确保在JSON输出中不存在intValue字段:

```java
@Test
public final void givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect() throws JsonParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
            .serializeAllExcept("intValue");
    FilterProvider filters = new SimpleFilterProvider()
            .addFilter("myFilter", theFilter);

    MyDtoWithFilter dtoObject = new MyDtoWithFilter();
    String dtoAsString = mapper.writer(filters).writeValueAsString(dtoObject);

    assertThat(dtoAsString, not(containsString("intValue")));
    assertThat(dtoAsString, containsString("booleanValue"));
    assertThat(dtoAsString, containsString("stringValue"));
    System.out.println(dtoAsString);
}
```

输出结果:

```json
{
    "stringValue":null,
    "booleanValue":false
}
```

这里是我们上面使用的DTO:

```java
@JsonFilter("myFilter")
@Data
public class MyDtoWithFilter {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
}
```

# 总结