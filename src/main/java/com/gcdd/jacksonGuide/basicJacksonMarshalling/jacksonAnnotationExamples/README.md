# 总览
> 在本文中，我们将深入探讨Jackson注解。我们将学习到如何使用现有注解，如何创建自定义注释以及如何禁用它们。

# Jackson序列化注解
> 首先,让我们看一下序列化注解

## @JsonAnyGetter
> `@JsonAnyGetter`注解允许使用Map字段作为标准属性。

下面是一个快速入门的示例,`ExtendableBean`实体具有`name`属性和一组以键/值对形式的可扩展属性:

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

当我们序列化这个实体的一个实例时,我们将Map中的所有键值作为标准的普通属性:

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

展示下输出结果:

```json
{
    "name":"My bean",
    "attr2":"val2",
    "attr1":"val1"
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
    System.out.println(result);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
}
```

输出结果:

```json
{
    "name":"My bean",
    "id":1
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
    System.out.println(result);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
}
```

输出结果:

```json
{
    "name":"My bean",
    "id":1
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
    System.out.println(result);
    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("{\"attr\":false}"));
}
```

输出结果:

```json
{
    "name":"My bean",
    "json":{
        "attr":false
    }
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
    System.out.println(enumAsString);
    assertEquals(enumAsString, "\"Type A\"");
}
```

输出结果:

```json
"Type A"
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

    System.out.println(result);
    assertThat(result, containsString("John"));
    assertThat(result, containsString("user"));
}
```

输出结果:

```json
{
    "user":{
        "id":1,
        "name":"John"
    }
}
```

## @JsonSerialize
> `@JsonSerialize`用于指示将使用自定义序列化程序来序列化实体。

让我们看一个简单的例子 - 我们将使用`@JsonSerialize`使用`CustomDateSerializer`序列化`eventDate`属性：

```java
@AllArgsConstructor
public class Event {
    public String name;

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date eventDate;
}
```

这是简单的自定义Jackson序列化器：

```java
public class CustomDateSerializer extends StdSerializer<Date> {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
```

让我们在测试中使用它们：

```java
@Test
public void whenSerializingUsingJsonSerialize_thenCorrect() throws JsonProcessingException, ParseException {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);

    String result = new ObjectMapper().writeValueAsString(event);
    System.out.println(result);
    assertThat(result, containsString(toParse));
}
```

输出结果:

```json
{
    "name":"party",
    "eventDate":"20-12-2014 02:30:00"
}
```

# Jackson反序列化注解
> 接下来,让我们探讨Jackson反序列化注解

## @JsonCreator
> `@JsonCreator`注解用于调整反序列化中使用的构造函数/工厂。

当我们需要反序列化一些与我们需要获得的目标实体不完全匹配的JSON时，它非常有用。

我们来看一个例子,例如我们需要反序列化以下JSON:

```json
{
    "id":1,
    "theName":"My bean"
}
```

但是,我们的目标实体中没有theName字段,只有一个name字段。现在,我们不想更改实体本身,我们可以使用`@JsonCreator`注解构造函数并使用`@JsonProperty`注解:

```java
public class BeanWithCreator {
    public int id;
    public String name;

    @JsonCreator
    public BeanWithCreator(@JsonProperty("id") int id,
                           @JsonProperty("theName") String name) {
        this.id = id;
        this.name = name;
    }
}
```

来测试下:

```java
@Test
public void whenDeserializingUsingJsonCreator_thenCorrect() throws IOException {
    String json = "{\"id\":1,\"theName\":\"My bean\"}";

    BeanWithCreator bean = new ObjectMapper()
            .readerFor(BeanWithCreator.class)
            .readValue(json);
    assertEquals("My bean", bean.name);
}
```

## @JacksonInject
> `@JacksonInject`用于标识将从注入而不是从JSON数据获取其值的属性。

在以下示例中,我们使用`@JacksonInject`来注入属性id:

```java
public class BeanWithInject {
    @JacksonInject
    public int id;
     
    public String name;
}
```

这是如何工作的:

```java
@Test
public void whenDeserializingUsingJsonInject_thenCorrect() throws IOException {
    String json = "{\"name\":\"My bean\"}";
    InjectableValues inject = new InjectableValues.Std()
            .addValue(int.class, 1);
    BeanWithInject bean = new ObjectMapper().reader(inject)
            .forType(BeanWithInject.class)
            .readValue(json);

    assertEquals("My bean", bean.name);
    assertEquals(1, bean.id);
}
```

## @JsonAnySetter
> `@JsonAnySetter`允许您灵活地使用Map作为标准属性。在反序列化时，JSON中的属性将简单地添加到Map中。

让我们看看它是如何工作的,我们将使用`@JsonAnySetter`来反序列化实体`ExtendableBean`:

```java
public class ExtendableBean {
    public String name;
    private Map<String, String> properties;
 
    @JsonAnySetter
    public void add(String key, String value) {
        properties.put(key, value);
    }
}
```

这是我们需要反序列化的JSON:

```json
{
    "name":"My bean",
    "attr2":"val2",
    "attr1":"val1"
}
```

看看这一切如何联系在一起的:

```java
@Test
public void whenDeserializingUsingJsonAnySetter_thenCorrect() throws IOException {
    String json = "{\"name\":\"My bean\",\"attr2\":\"val2\",\"attr1\":\"val1\"}";

    ExtendableBean bean = new ObjectMapper()
            .readerFor(ExtendableBean.class)
            .readValue(json);

    assertEquals("My bean", bean.name);
    assertEquals("val2", bean.getProperties().get("attr2"));
}
```

## @JsonSetter
> `@JsonSetter`是`@JsonProperty`的替代品,用于将方法标记为setter方法。
  
当我们需要读取一些目标实体类与该数据不完全匹配的JSON数据时,非常有用,我们可以使用`@JsonSetter`让他们能匹配起来。

在下面的示例中,我们将指定方法`setTheName()`作为`MyBean`实体中`name`属性的`setter`：

```java
public class MyBean {
    public int id;
    private String name;
 
    @JsonSetter("name")
    public void setTheName(String name) {
        this.name = name;
    }
}
```

现在,当我们需要反序列化一些JSON数据时,这非常有效：

```java
@Test
public void whenDeserializingUsingJsonSetter_thenCorrect() throws IOException {
    String json = "{\"id\":1,\"name\":\"My bean\"}";

    MyBean bean = new ObjectMapper()
            .readerFor(MyBean.class)
            .readValue(json);
    assertEquals("My bean", bean.getName());
}
```

## @JsonDeserialize
> @JsonDeserialize用于指示使用自定义反序列化器。

让我们看看它是如何发挥作用的,我们将使用`@JsonDeserialize`并自定义`CustomDateDeserializer`进行反序列化`eventDate`属性：

```java
public class Event {
    public String name;
 
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date eventDate;
}
```

这是自定义反序列化器:

```java
public class CustomDateDeserializer extends StdDeserializer<Date> {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String date = jsonparser.getText();
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
```

测试一下:

```java
@Test
public void whenDeserializingUsingJsonDeserialize_thenCorrect() throws IOException {
    String json = "{\"name\":\"party\",\"eventDate\":\"20-12-2014 02:30:00\"}";

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    Event event = new ObjectMapper()
            .readerFor(Event.class)
            .readValue(json);

    assertEquals("20-12-2014 02:30:00", df.format(event.eventDate));
}
```

# Jackson属性包含注解

## @JsonIgnoreProperties
> `@JsonIgnoreProperties`是Jackson中最常见的注释之一,用于标记要在类级别忽略的属性或属性列表。

让我们来看一个忽略序列化属性id的快速示例:

```java
@JsonIgnoreProperties({ "id" })
public class BeanWithIgnore {
    public int id;
    public String name;
}
```

这是测试:

```java
@Test
public void whenSerializingUsingJsonIgnoreProperties_thenCorrect() throws JsonProcessingException {
    BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");
    String result = new ObjectMapper()
            .writeValueAsString(bean);
    System.out.println(result);
    assertThat(result, containsString("My bean"));
    assertThat(result, not(containsString("id")));
}
```

## @JsonIgnore
> `@JsonIgnore`注解用于标记要在字段级别忽略的属性。

让我们使用`@JsonIgnore`来忽略序列化中的属性id:

```java
public class BeanWithIgnore {
    @JsonIgnore
    public int id;
 
    public String name;
}
```

测试确保成功忽略了id:

```java
@Test
public void whenSerializingUsingJsonIgnoreProperties_thenCorrect() throws JsonProcessingException {
    BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");
    String result = new ObjectMapper()
            .writeValueAsString(bean);
    System.out.println(result);
    assertThat(result, containsString("My bean"));
    assertThat(result, not(containsString("id")));
}
```

## @JsonIgnoreType
> 被`@JsonIgnoreType`注解修饰的类型将忽略所有属性。

让我们使用注释来标记要忽略的Name类型的所有属性:

```java
@AllArgsConstructor
public class User {
    public int id;
    public Name name;

    @JsonIgnoreType
    @AllArgsConstructor
    public static class Name {
        public String firstName;
        public String lastName;
    }
}
```

这是简单的测试，确保忽略正常工作:

```java
@Test
public void whenSerializingUsingJsonIgnoreType_thenCorrect() throws JsonProcessingException, ParseException {
    User.Name name = new User.Name("John", "Doe");
    User user = new User(1, name);

    String result = new ObjectMapper().writeValueAsString(user);
    System.out.println(result);
    
    assertThat(result, containsString("1"));
    assertThat(result, not(containsString("name")));
    assertThat(result, not(containsString("John")));
}
```

输出结果:

```json
{
    "id":1
}
```

## @JsonInclude
> `@JsonInclud`e用于排除空/null/默认值的属性。

让我们看一个例子,从序列化中排除空值:

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class MyBean {
    public int id;
    public String name;
}
```

这是完整的测试:

```java
@Test
public void whenSerializingUsingJsonInclude_thenCorrect() throws JsonProcessingException {
    MyBean bean = new MyBean(1, null);
    String result = new ObjectMapper()
            .writeValueAsString(bean);
    System.out.println(result);

    assertThat(result, containsString("1"));
    assertThat(result, not(containsString("name")));
}
```

## @JsonAutoDetect
> `@JsonAutoDetect`用于改变可见级别。

看一个简单的例子,让我们序列化私有属性:

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PrivateBean {
    private int id;
    private String name;
}
```

测试一下:

```java
@Test
public void whenSerializingUsingJsonAutoDetect_thenCorrect() throws JsonProcessingException {
    PrivateBean bean = new PrivateBean(1, "My bean");
    String result = new ObjectMapper()
            .writeValueAsString(bean);
    System.out.println(result);

    assertThat(result, containsString("1"));
    assertThat(result, containsString("My bean"));
}
```

# Jackson多态类型处理注解
> 接下来,让我们来看看Jackson多态类型处理注解:

- `@JsonTypeInfo`用于标记序列化中包含的类型信息的详细信息。
- `@JsonSubTypes`用于标记带注解类型的子类型。
- `@JsonTypeName`用于定义用于带注解的类的逻辑类型名称。

让我们看一个稍复杂的例子,使用三个`@JsonTypeInfo`,`@JsonSubTypes`和`@JsonTypeName`来序列化/反序列化实体Zoo：

```java
@AllArgsConstructor
public class Zoo {
    public Animal animal;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "dog"),
            @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    @AllArgsConstructor
    public static class Animal {
        public String name;
    }

    @JsonTypeName("dog")
    public static class Dog extends Animal {
        public double barkVolume;

        public Dog(String name) {
            super(name);
        }
    }

    @JsonTypeName("cat")
    public static class Cat extends Animal {
        boolean likesCream;
        public int lives;

        public Cat(String name) {
            super(name);
        }
    }
}
```

当我们进行序列化时:

```java
@Test
public void whenSerializingPolymorphic_thenCorrect() throws JsonProcessingException {
    Zoo.Dog dog = new Zoo.Dog("lacy");
    Zoo zoo = new Zoo(dog);

    String result = new ObjectMapper()
            .writeValueAsString(zoo);
    System.out.println(result);
    
    assertThat(result, containsString("type"));
    assertThat(result, containsString("dog"));
}
```

下是使用Dog序列化Zoo实例的结果:

```json
{
    "animal":{
        "type":"dog",
        "name":"lacy",
        "barkVolume":0
    }
}
```

现在进行反序列化,让我们从以下JSON输入开始:

```json
{
    "animal":{
        "name":"lacy",
        "type":"cat"
    }
}
```

让我们看看它如何被反序列化到Zoo实例:

```java
@Test
public void whenDeserializingPolymorphic_thenCorrect() throws IOException {
    String json = "{\"animal\":{\"name\":\"lacy\",\"type\":\"cat\"}}";

    Zoo zoo = new ObjectMapper()
            .readerFor(Zoo.class)
            .readValue(json);

    assertEquals("lacy", zoo.animal.name);
    assertEquals(Zoo.Cat.class, zoo.animal.getClass());
}
```

# Jackson常用注解
> 接下来,让我们讨论Jackson的一些更常见的注解。

## @JsonProperty
> `@JsonProperty`用于指定JSON中的属性名称

让我们用一个简单的例子来看一下注释,当我们处理非标准的getter和setter时，使用@`JsonProperty`来指定序列化/反序列化属性名：

```java
public class MyBean {
    public int id;
    private String name;

    @JsonProperty("name")
    public void setTheName(String name) {
        this.name = name;
    }

    @JsonProperty("name")
    public String getTheName() {
        return name;
    }
}
```

我们的测试:

```java
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
```

## @JsonFormat
> `@JsonFormat`注解可用于在序列化日期/时间值时指定格式。
  
在下面的示例中,我们使用`@JsonFormat`来控制属性`eventDate`的日期格式:

```java
public class Event {
    public String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    public Date eventDate;
}
```

下面是测试:

```java
@Test
public void whenSerializingUsingJsonFormat_thenCorrect() throws JsonProcessingException, ParseException {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));

    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);

    String result = new ObjectMapper().writeValueAsString(event);

    assertThat(result, containsString(toParse));
}
```

## @JsonUnwrapped
> `@JsonUnwrapped`用于定义序列化/反序列化时应解包/展平的值。

让我们看看它是如何工作的,我们将使用`@JsonUnwrapped`来解包属性名称:

```java
public class UnwrappedUser {
    public int id;

    @JsonUnwrapped
    public Name name;

    public static class Name {
        public String firstName;
        public String lastName;
    }
}
```

现在让我们序列化这个类的一个实例:

```java
@Test
public void whenSerializingUsingJsonUnwrapped_thenCorrect() throws JsonProcessingException, ParseException {
    UnwrappedUser.Name name = new UnwrappedUser.Name("John", "Doe");
    UnwrappedUser user = new UnwrappedUser(1, name);

    String result = new ObjectMapper().writeValueAsString(user);

    assertThat(result, containsString("John"));
    assertThat(result, not(containsString("name")));
}
```

以下是输出的样子,静态嵌套类的字段与其他字段一起展开:

```json
{
    "id":1,
    "firstName":"John",
    "lastName":"Doe"
}
```

## @JsonView
> `@JsonView`用于指定视图，其中将包含属性以进行序列化/反序列化。
  
下面这个示例将准确显示其工作原理,我们将使用`@JsonView`序列化`Item`实体的实例。

让我们从Views开始:

```java
public class Views {
    public static class Public {}
    public static class Internal extends Public {}
}
```

现在这里是Item实体,使用Views:

```java
public class Item {
    @JsonView(Views.Public.class)
    public int id;

    @JsonView(Views.Public.class)
    public String itemName;

    @JsonView(Views.Internal.class)
    public String ownerName;
}
```

最后是完整的测试:

```java
@Test
public void whenSerializingUsingJsonView_thenCorrect() throws JsonProcessingException {
    Item item = new Item(2, "book", "John");

    String result = new ObjectMapper()
            .writerWithView(Views.Public.class)
            .writeValueAsString(item);

    assertThat(result, containsString("book"));
    assertThat(result, containsString("2"));
    assertThat(result, not(containsString("John")));
}
```

输出结果:

```json
{
    "id":2,
    "itemName":"book"
}
```

## @JsonManagedReference,@JsonBackReference
> `@JsonManagedReference`和`@JsonBackReference`注解用于处理父/子关系并解决循环问题。

在下面的示例中,我们使用`@JsonManagedReference`和`@JsonBackReference`来序列化`ItemWithRef`实体:

```java
public class ItemWithRef {
    public int id;
    public String itemName;

    @JsonManagedReference
    public UserWithRef owner;
}
public class UserWithRef {
    public int id;
    public String name;

    @JsonBackReference
    public List<ItemWithRef> userItems;
}
```

测试:

```java
@Test
public void whenSerializingUsingJacksonReferenceAnnotation_thenCorrect() throws JsonProcessingException {
    UserWithRef user = new UserWithRef(1, "John");
    ItemWithRef item = new ItemWithRef(2, "book", user);
    user.userItems = new ArrayList<>();
    user.userItems.add(item);

    String result = new ObjectMapper().writeValueAsString(item);

    System.out.println(result);
    assertThat(result, containsString("book"));
    assertThat(result, containsString("John"));
    assertThat(result, not(containsString("userItems")));
}
```

输出结果:

```json
{
    "id":2,
    "itemName":"book",
    "owner":{
        "id":1,
        "name":"John"
    }
}
```

## @JsonIdentityInfo
> @JsonIdentityInfo用于指示在序列化/反序列化值时使用对象标识。例如，处理无限递归类型的问题。
  
在下面的示例中,我们有一个`ItemWithIdentity`实体，它与`UserWithIdentity`实体具有双向关系:

```java
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ItemWithIdentity {
    public int id;
    public String itemName;
    public UserWithIdentity owner;
}
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserWithIdentity {
    public int id;
    public String name;
    public List<ItemWithIdentity> userItems;
}
```

现在,让我们看看如何处理无限递归问题:

```java
@Test
public void whenSerializingUsingJsonIdentityInfo_thenCorrect()
        throws JsonProcessingException {
    UserWithIdentity user = new UserWithIdentity(1, "John");
    ItemWithIdentity item = new ItemWithIdentity(2, "book", user);
    user.userItems = new ArrayList<>();
    user.userItems.add(item);

    String result = new ObjectMapper().writeValueAsString(item);
    System.out.println(result);

    assertThat(result, containsString("book"));
    assertThat(result, containsString("John"));
    assertThat(result, containsString("userItems"));
}
```

这是序列化项目和用户的完整输出:

```json
{
    "id": 2,
    "itemName": "book",
    "owner": {
        "id": 1,
        "name": "John",
        "userItems": [
            2
        ]
    }
}
```

## @JsonFilter
> `@JsonFilter`注解指定序列化期间要使用的过滤器。

我们来看一个例子;首先，我们定义实体，并指定过滤器:

```java
@JsonFilter("myFilter")
public class BeanWithFilter {
    public int id;
    public String name;
}
```

现在,在完整测试中,我们定义了过滤器,它排除了除序列化名称之外的所有其他属性:

```java
@Test
public void whenSerializingUsingJsonFilter_thenCorrect()
        throws JsonProcessingException {
    BeanWithFilter bean = new BeanWithFilter(1, "My bean");

    FilterProvider filters = new SimpleFilterProvider().addFilter(
            "myFilter",
            SimpleBeanPropertyFilter.filterOutAllExcept("name"));

    String result = new ObjectMapper()
            .writer(filters)
            .writeValueAsString(bean);
    System.out.println(result);

    assertThat(result, containsString("My bean"));
    assertThat(result, not(containsString("id")));
}
```

输出结果:

```json
{
    "name":"My bean"
}
```

# 自定义Jackson注解
> 接下来,让我们看看如何创建自定义Jackson注解

我们可以使用`@JacksonAnnotationsInside`注解,如下例所示:

```java
@Retention(RetentionPolicy.RUNTIME)
    @JacksonAnnotationsInside
    @JsonInclude(Include.NON_NULL)
    @JsonPropertyOrder({ "name", "id", "dateCreated" })
    public @interface CustomAnnotation {}
```

现在，如果我们在实体上使用新注释:

```java
@CustomAnnotation
public class BeanWithCustomAnnotation {
    public int id;
    public String name;
    public Date dateCreated;
}
```

我们可以看到它如何将现有注释组合成一个更简单的自定义注释，我们可以将其用作速记:

```java
@Test
public void whenSerializingUsingCustomAnnotation_thenCorrect() throws JsonProcessingException {
    BeanWithCustomAnnotation bean = new BeanWithCustomAnnotation(1, "My bean", null);

    String result = new ObjectMapper().writeValueAsString(bean);
    System.out.println(result);

    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
    assertThat(result, not(containsString("dateCreated")));
}
```

序列化过程的输出:

```json
{
    "name":"My bean",
    "id":1
}
```

# Jackson MixIn 注解
> 接下来,让我们看看如何使用Jackson MixIn 注解。
  
让我们使用MixIn注解。例如,忽略User类型的属性:

```java
@AllArgsConstructor
public class Item {
    public int id;
    public String itemName;
    public User owner;
}
@JsonIgnoreType
public class MyMixInForIgnoreType {
}
```

来看看示例:

```java
@Test
public void whenSerializingUsingMixInAnnotation_thenCorrect() throws JsonProcessingException {
    Item item = new Item(1, "book", null);

    String result = new ObjectMapper().writeValueAsString(item);
    assertThat(result, containsString("owner"));
    System.out.println(result);

    ObjectMapper mapper = new ObjectMapper();
    mapper.addMixIn(User.class, MyMixInForIgnoreType.class);

    result = mapper.writeValueAsString(item);
    System.out.println(result);
    assertThat(result, not(containsString("owner")));
}
```

输出结果:

```json
{"id":1,"itemName":"book","owner":null}
```

```json
{"id":1,"itemName":"book"}
```

## 禁用Jackson注解
> 最后 - 让我们看看我们如何禁用所有Jackson注解。

我们可以通过禁用`MapperFeature.USE_ANNOTATIONS`来实现这一点，如下例所示:

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "id"})
@AllArgsConstructor
public class MyBean {
    public int id;
    public String name;
}
```

现在，在禁用注释后，这些应该没有效果，并且应该应用库的默认值:

```java
@Test
public void whenDisablingAllAnnotations_thenAllDisabled() throws IOException {
    MyBean bean = new MyBean(1, null);

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(MapperFeature.USE_ANNOTATIONS);
    String result = mapper.writeValueAsString(bean);
    System.out.println(result);

    assertThat(result, containsString("1"));
    assertThat(result, containsString("name"));
}
```

禁用注释之前序列化的结果:

```json
{"id":1}
```

禁用注解之后序列化的结果:

```json
{
    "id":1,
    "name":null
}
```

# 总结