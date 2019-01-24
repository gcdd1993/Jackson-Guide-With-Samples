# 总览
> 在本教程中，我们将使用Jackson序列化日期。我们首先将序列化一个简单的`java.util.Date`，然后是`Joda-Time`以及`Java 8 DateTime`。

# 序列化日期到时间戳
> 首先，让我们看看如何使用Jackson序列化一个简单的`java.util.Date`。

在以下示例中，我们将序列化具有`Date`字段`eventDate`的`Event`实例：

```java
@Test
public void whenSerializingDateWithJackson_thenSerializedToTimestamp() throws JsonProcessingException, ParseException {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));

    Date date = df.parse("01-01-1970 01:00");
    Event event = new Event("party", date);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValueAsString(event);
}
```

Jackson默认将日期序列化为时间戳格式（1970年1月1日以来的UTC毫秒数）。

上面的序列化输出为：

```json
{
   "name":"party",
   "eventDate":3600000
}
```

# 序列化`Date`为 ISO-8601

序列化为时间戳的形式是不利于人阅读的，现在我们把`Date`序列化为 ISO-8601：

```java
@Test
public void whenSerializingDateToISO8601_thenSerializedToText()
        throws JsonProcessingException, ParseException {

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));

    String toParse = "01-01-1970 02:30";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // StdDateFormat is ISO8601 since jackson 2.9
    mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString("1970-01-01T02:30:00.000+00:00"));
}
```

# 设置`ObjectMapper DateFormat`
> 以前的解决方案还是不够灵活，无法自定义日期的输出形式。

现在让我们看一个配置，它允许我们设置表示日期的格式：

```java
@Test
public void whenSettingObjectMapperDateFormat_thenCorrect()
        throws JsonProcessingException, ParseException {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    String toParse = "20-12-2014 02:30";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);

    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(df);

    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString(toParse));
}
```

请注意，即使我们现在对日期格式更加灵活，我们仍然在整个ObjectMapper级别使用全局配置。

# 使用`@JsonFormat`来序列化时间

接下来，让我们看一下`@JsonFormat`注释来控制整个应用程序的各个类而不是全局的日期格式：

```java
@Data
@AllArgsConstructor
public class Event {
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;
}
```

让我们测试下：

```java
@Test
public void whenUsingJsonFormatAnnotationToFormatDate_thenCorrect()
  throws JsonProcessingException, ParseException {
  
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
 
    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);
 
    ObjectMapper mapper = new ObjectMapper();
    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString(toParse));
}
```

# 自定义`Date`序列化器

下一步，为了完全控制输出，我们可以自定义一个日期序列化器：

```java
public class CustomDateSerializer extends StdSerializer<Date> {
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
```

接下来，让我们将它用作`eventDate`字段的序列化器：

```java
@Data
@AllArgsConstructor
public class Event {
    private String name;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date date;
}
```

测试一下：

```java
@Test
public void whenUsingCustomDateSerializer_thenCorrect()
        throws JsonProcessingException, ParseException {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Event event = new Event("party", date);

    ObjectMapper mapper = new ObjectMapper();
    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString(toParse));
}
```

# 使用Jackson序列化`Joda-Time`

日期并不总是`java.util.Date`的实例，实际上，它们越来越多地由其他类代表。当然，常见的是来自Joda-Time库的`DateTime`实现。

让我们看看我们如何用Jackson序列化`DateTime`。

我们将使用`jackson-datatype-joda`模块开箱即用的`Joda-Time`支持：

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-joda</artifactId>
  <version>2.9.7</version>
</dependency>
```

接下来，我们可以简单地通过注册`JodaModule`来序列化`Joda-Time`：

```java
@Test
public void whenSerializingJodaTime_thenCorrect()
        throws JsonProcessingException {
    DateTime date = new DateTime(2014, 12, 20, 2, 30,
            DateTimeZone.forID("Europe/London"));

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String result = mapper.writeValueAsString(date);
    assertThat(result, containsString("2014-12-20T02:30:00.000Z"));
}
```

# 使用自定义序列化器序列化`Joda-Time`：

如果我们不想使用`jackson-datatype-joda`依赖，我们也可以自定义序列化器来序列化`Joda-Time`：

```java
public class CustomDateTimeSerializer extends StdSerializer<DateTime> {
    private static DateTimeFormatter formatter =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    public CustomDateTimeSerializer() {
        this(null);
    }

    public CustomDateTimeSerializer(Class<DateTime> t) {
        super(t);
    }

    @Override
    public void serialize
            (DateTime value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(formatter.print(value));
    }
}
```

然后，在字段上注明使用该自定义序列化器：

```java
@Data
@AllArgsConstructor
public class Event {
    private String name;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private DateTime date;
}
```

最后，让我们来测试下：

```java
@Test
public void whenSerializingJodaTimeWithJackson_thenCorrect()
        throws JsonProcessingException {
    DateTime date = new DateTime(2014, 12, 20, 2, 30);
    JodoEvent event = new JodoEvent("party", date);

    ObjectMapper mapper = new ObjectMapper();
    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString("2014-12-20 02:30"));
}
```

# 使用Jackson序列化 `Java8 Date`

接下来，让我们看看如何序列化`Java 8 DateTime`。在本例中，使用Jackson序列化`LocalDateTime`。我们可以使用`jackson-datatype-jsr310`模块：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.9.7</version>
</dependency>
```

现在，我们需要做的就是注册`JavaTimeModule`（`JSR310Module`已弃用），Jackson将负责其余的工作：

```java
@Test
public void whenSerializingJava8Date_thenCorrect()
        throws JsonProcessingException {
    LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String result = mapper.writeValueAsString(date);
    assertThat(result, containsString("2014-12-20T02:30"));
}
```

# 不使用任何依赖来序列化`Java 8 Date`：

如果您不想要额外的依赖项，则可以使用自定义序列化程序将`Java 8 DateTime`写出为JSON：

```java
public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(
            LocalDateTime value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
```

接下来，让我们在`eventDate`字段上使用序列化器：

```java
public class Event {
    public String name;
 
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime eventDate;
}
```

测试一下：

```java
@Test
public void whenSerializingJava8DateWithCustomSerializer_thenCorrect()
        throws JsonProcessingException {

    LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);
    Java8Event event = new Java8Event("party", date);

    ObjectMapper mapper = new ObjectMapper();
    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString("2014-12-20 02:30"));
}
```

# 反序列化`Date`

接下来 - 让我们看看如何用杰克逊反序列化日期。在以下示例中 - 我们反序列化包含日期的“Event”实例：

```java
@Test
public void whenDeserializingDateWithJackson_thenCorrect()
        throws JsonProcessingException, IOException {

    String json = "{\"name\":\"party\",\"date\":\"20-12-2014 02:30:00\"}";

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(df);

    Event event = mapper.readerFor(Event.class).readValue(json);
    assertEquals("20-12-2014 02:30:00", df.format(event.getDate()));
}
```

# 反序列化`Joda ZonedDateTime`并保留时区

在默认配置中，Jackson将`Joda ZonedDateTime`的时区调整为本地上下文的时区。

默认情况下，如果本地上下文的时区未设置，就必须手动配置，否则Jackson会将时区调整为GMT：

```java
@Test
public void whenDeserialisingZonedDateTimeWithDefaults_thenNotCorrect()
  throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
    String converted = objectMapper.writeValueAsString(now);
 
    ZonedDateTime restored = objectMapper.readValue(converted, ZonedDateTime.class);
    System.out.println("serialized: " + now);
    System.out.println("restored: " + restored);
    assertThat(now, is(restored));
}
```

此测试用例将失败并输出：

```
serialized: 2017-08-14T13:52:22.071+02:00[Europe/Berlin]
restored: 2017-08-14T11:52:22.071Z[UTC]
```

幸运的是，对于这种奇怪的默认行为有一个快速而简单的解决方法：我们只需要告诉杰克逊，不要调整时区。

这可以通过将以下代码行添加到上面的测试用例中来完成：

```java
objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
```

**【注意】** 为了保留时区，我们还必须禁用将日期序列化为时间戳的默认行为。

# 自定义Jackson反序列化器

我们还将看到如何使用自定义日期反序列化器;我们将为属性`eventDate`编写一个自定义反序列化器：

```java
public class CustomDateDeserializer extends StdDeserializer<Date> {

    private SimpleDateFormat formatter =
            new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
```

接下来，让我们在`eventDate`上使用这个自定义反序列化器：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String name;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;
}
```

最后，让我们测试一下：

```java
@Test
public void whenDeserializingDateUsingCustomDeserializer_thenCorrect()
        throws JsonProcessingException, IOException {

    String json = "{\"name\":\"party\",\"date\":\"20-12-2014 02:30:00\"}";

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    ObjectMapper mapper = new ObjectMapper();

    Event event = mapper.readerFor(Event.class).readValue(json);
    assertEquals("20-12-2014 02:30:00", df.format(event.getDate()));
}
```