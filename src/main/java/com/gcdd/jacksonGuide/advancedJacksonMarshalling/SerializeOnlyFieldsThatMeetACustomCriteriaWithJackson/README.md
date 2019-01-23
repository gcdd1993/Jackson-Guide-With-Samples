# 总览
> 本教程将说明如何使用Jackson仅在符合特定自定义条件的情况下序列化字段。

例如，假设我们只想序列化一个整数值，如果它是正的,我们将不进行序列化。

# 使用Jackson过滤器来控制序列化过程

首先，我们需要使用`@JsonFilter`注释在我们的实体上定义过滤器：

```java
@Data
@NoArgsConstructor
@JsonFilter("myFilter")
public class MyDto {
    private int intValue;
}
```

然后，我们需要定义我们的自定义PropertyFilter：

```java
@Test
public void test1() {
    PropertyFilter theFilter = new SimpleBeanPropertyFilter() {
        @Override
        public void serializeAsField
                (Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
                throws Exception {
            if (include(writer)) {
                if (!writer.getName().equals("intValue")) {
                    writer.serializeAsField(pojo, jgen, provider);
                    return;
                }
                int intValue = ((MyDtoWithFilter) pojo).getIntValue();
                if (intValue >= 0) {
                    writer.serializeAsField(pojo, jgen, provider);
                }
            } else if (!jgen.canOmitFields()) { // since 2.3
                writer.serializeAsOmittedField(pojo, jgen, provider);
            }
        }

        @Override
        protected boolean include(BeanPropertyWriter writer) {
            return true;
        }

        @Override
        protected boolean include(PropertyWriter writer) {
            return true;
        }
    };
}
```

此过滤器包含实际逻辑，根据其值确定intValue字段是否将被序列化。

接下来，我们将此过滤器注册到`ObjectMapper`中，然后我们序列化一个实体：

```java
FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
MyDtoWithFilter dtoObject = new MyDtoWithFilter();
dtoObject.setIntValue(-1);

ObjectMapper mapper = new ObjectMapper();
String dtoAsString = mapper.writer(filters).writeValueAsString(dtoObject);
```

最后，我们可以检查intValue字段确实不是JSON输出的一部分：

```java
assertThat(dtoAsString, not(containsString("intValue")));
```

# 根据条件来跳过对象的序列化
> 现在，让我们讨论如何在基于属性值进行序列化时跳过对象。我们将跳过属性隐藏为真的所有对象：

## `Hidable`类

首先，我们来看看我们的`Hidable`接口：

```java
@JsonIgnoreProperties("hidden")
public interface Hidable {
    boolean isHidden();
}
```

我们有两个简单的类实现这个接口`Person`，`Address`：

```java
@Data
public class Person  implements Hidable {
    private String name;
    private Address address;
    private boolean hidden;
}
@Data
public class Address implements Hidable {
    private String city;
    private String country;
    private boolean hidden;
}
```

**【注意】** 我们使用`@JsonIgnoreProperties("hidden")`来确保隐藏属性本身不包含在JSON中。

## 自定义序列化器

下一步，我们要自定义一个序列化器：

```java
public class HidableSerializer extends JsonSerializer<Hidable> {
    private JsonSerializer<Object> defaultSerializer;

    public HidableSerializer(JsonSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void serialize(Hidable value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value.isHidden()) {
            return;
        }
        defaultSerializer.serialize(value, jgen, provider);
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, Hidable value) {
        return (value == null || value.isHidden());
    }
}
```

**【注意】**

- 当不跳过对象时，我们将序列化委托给默认的注入序列化器。
- 我们重写了方法`isEmpty()` - 以确保在`Hidable`对象是属性的情况下，属性名称也从JSON中排除。

## 使用`BeanSerializerModifier`

最后，我们需要使用BeanSerializerModifier在我们的自定义HidableSerializer中注入默认序列化程序 - 如下所示：

```java
ObjectMapper mapper = new ObjectMapper();
mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
mapper.registerModule(new SimpleModule() {
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(
                    SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                if (Hidable.class.isAssignableFrom(desc.getBeanClass())) {
                    return new HidableSerializer((JsonSerializer<Object>) serializer);
                }
                return serializer;
            }
        });
    }
});
```

## 示例输出

这是一个简单的序列化示例：

```java
Address ad1 = new Address("tokyo", "jp", true);
Address ad2 = new Address("london", "uk", false);
Address ad3 = new Address("ny", "usa", false);
Person p1 = new Person("john", ad1, false);
Person p2 = new Person("tom", ad2, true);
Person p3 = new Person("adam", ad3, false);
 
System.out.println(mapper.writeValueAsString(Arrays.asList(p1, p2, p3)));
```

输出结果：

```json
[
    {
        "name":"john"
    },
    {
        "name":"adam",
        "address":{
            "city":"ny",
            "country":"usa"
        }
    }
]
```

## 测试

最后 - 这里是几个测试用例：

1. 没有隐藏的内容：

```java
Address ad = new Address("ny", "usa", false);
Person person = new Person("john", ad, false);
String result = mapper.writeValueAsString(person);

assertTrue(result.contains("name"));
assertTrue(result.contains("john"));
assertTrue(result.contains("address"));
assertTrue(result.contains("usa"));
```

2. 只有`Address`隐藏：

```java
@Test
public void whenAddressHidden_thenCorrect() throws JsonProcessingException {
    Address ad = new Address("ny", "usa", true);
    Person person = new Person("john", ad, false);
    String result = mapper.writeValueAsString(person);
 
    assertTrue(result.contains("name"));
    assertTrue(result.contains("john"));
    assertFalse(result.contains("address"));
    assertFalse(result.contains("usa"));
}
```

3. 隐藏`Person`：

```java
@Test
public void whenAllHidden_thenCorrect() throws JsonProcessingException {
    Address ad = new Address("ny", "usa", false);
    Person person = new Person("john", ad, true);
    String result = mapper.writeValueAsString(person);
 
    assertTrue(result.length() == 0);
}
```
