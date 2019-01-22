# 总览
> 本文的重点是理解`Jackson ObjectMapper`类，以及如何使用`ObjectMapper`类将Java对象序列化为JSON，并将JSON字符串反序列化为Java对象。

# 依赖项

我们首先将以下依赖项添加到pom.xml：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.4</version>
</dependency>
```

此依赖项将同步依赖以下几个:

1. jackson-annotations-2.9.4.jar
2. jackson-core-2.9.4.jar
3. jackson-databind-2.9.4.jar

# 使用ObjectMapper进行读写
> 让我们从基本的读写操作开始。

`ObjectMapper`的简单`readValue` API 是一个很好的切入点，这可以用于将JSON内容解析或反序列化为Java对象。

在反序列化方面，`writeValue` API 可用于将任何Java对象序列化为JSON输出。

在本文中，我们将使用以下Car类作为序列化或反序列化的对象：

```java
@Data
public class Car {
    private String color;
    private String type;
}
```

## Java对象到JSON

使用`writeValue`方法将java对象序列化为json的简单示例：

```java
ObjectMapper objectMapper = new ObjectMapper();
Car car = new Car("yellow", "renault");
objectMapper.writeValue(new File("target/car.json"), car);
```

输出如下：

```json
{"color":"yellow","type":"renault"}
```

`ObjectMapper`类的`writeValueAsString`和`writeValueAsBytes`方法从Java对象生成JSON，并将生成的JSON作为字符串或字节数组返回：

```java
String carAsString = objectMapper.writeValueAsString(car);
```

## JSON到Java对象

下面是一个将json字符串反序列化为java对象的简单示例：

```java
String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
Car car = objectMapper.readValue(json, Car.class);  
```

readValue函数还接受其他形式的输入，如包含JSON字符串的文件：

```java
Car car = objectMapper.readValue(new File("target/json_car.json"), Car.class);
```

或者是URL：

```java
Car car = objectMapper.readValue(new URL("target/json_car.json"), Car.class);
```

## JSON到Jackson JsonNode

或者，我们也可以将json字符串反序列化为`JsonNode`对象，用来从指定节点查找数据：

```java
String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
JsonNode jsonNode = objectMapper.readTree(json);
String color = jsonNode.get("color").asText();
// Output: color -> Black
```

## 从JSON数组字符串创建Java列表

我们还可以将json数组反序列化为java列表：

```java
String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
```

## 从JSON字符串创建Java Map对象

通过以下方式解析，可以将字符串形式的JSON解析为Java Map对象：

```java
String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
```

# 高级功能
> Jackson库的最大优势之一是高度可定制的序列化和反序列化过程。
  
在本节中，我们将介绍一些高级功能，其中输入或输出JSON响应可以与生成或使用响应的对象不同。

## 配置序列化或反序列化功能

在将JSON对象转换为Java类时，如果JSON字符串具有一些Java类中没有的字段，则将会抛出异常：

```java
String jsonString = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";
```

上面示例中的JSON字符串在反序列化成Car类时将抛出`UnrecognizedPropertyException`异常。

通过configure方法，我们可以忽略Java类中没有的字段：

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
Car car = objectMapper.readValue(jsonString, Car.class);
 
JsonNode jsonNodeRoot = objectMapper.readTree(jsonString);
JsonNode jsonNodeYear = jsonNodeRoot.get("year");
String year = jsonNodeYear.asText();
```

另一个选项基于`FAIL_ON_NULL_FOR_PRIMITIVES`，它定义是否允许空值：

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
```

同样，`FAIL_ON_NUMBERS_FOR_ENUM`控制是否允许将枚举值序列化/反序列化为数字：

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
```

更加详细的配置可以参考[官方文档](https://github.com/FasterXML/jackson-databind/wiki/Serialization-Features)

# 自定义序列化/反序列化

ObjectMapper类的另一个基本特性是能够注册自定义序列化器和反序列化器。

自定义序列化/反序列化在json与Java类型不匹配的情况下非常有用。

下面是自定义JSON序列化程序的示例：

```java
public class CustomCarSerializer extends StdSerializer<Car> {
     
    public CustomCarSerializer() {
        this(null);
    }
 
    public CustomCarSerializer(Class<Car> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      Car car, JsonGenerator jsonGenerator, SerializerProvider serializer) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("car_brand", car.getType());
        jsonGenerator.writeEndObject();
    }
}
```

我们可以这样来调用自定义序列化器：

```java
ObjectMapper mapper = new ObjectMapper();
SimpleModule module = new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
module.addSerializer(Car.class, new CustomCarSerializer());
mapper.registerModule(module);
Car car = new Car("yellow", "renault");
String carJson = mapper.writeValueAsString(car);
```

假设有个描述汽车的json字符串：

```json
var carJson = {"car_brand":"renault"}
```

这是一个自定义JSON反序列化器的示例：

```java
public class CustomCarDeserializer extends StdDeserializer<Car> {
     
    public CustomCarDeserializer() {
        this(null);
    }
 
    public CustomCarDeserializer(Class<?> vc) {
        super(vc);
    }
 
    @Override
    public Car deserialize(JsonParser parser, DeserializationContext deserializer) {
        Car car = new Car();
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
         
        // try catch block
        JsonNode colorNode = node.get("color");
        String color = colorNode.asText();
        car.setColor(color);
        return car;
    }
}
```

可以通过以下方式调用此自定义反序列化器：

```java
String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
ObjectMapper mapper = new ObjectMapper();
SimpleModule module = new SimpleModule("CustomCarDeserializer", new Version(1, 0, 0, null, null, null));
module.addDeserializer(Car.class, new CustomCarDeserializer());
mapper.registerModule(module);
Car car = mapper.readValue(json, Car.class);
```

# 处理日期格式

`java.util.Date`的默认序列化产生一个数字，即纪元时间戳（自1970年1月1日，UTC以来的毫秒数）。但这不是人类可读的，需要进一步转换才能显示人类可读的格式。

让我们使用`datePurchased`属性将我们目前使用的Car实例包装在Request类中：

```java
public class Request 
{
    private Car car;
    private Date datePurchased;
 
    // standard getters setters
}
```

我们可以设置时间序列化/反序列化方式：

```java
ObjectMapper objectMapper = new ObjectMapper();
DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
objectMapper.setDateFormat(df);
String carAsString = objectMapper.writeValueAsString(request);
// output: {"car":{"color":"yellow","type":"renault"},"datePurchased":"2016-07-03 11:43 AM CEST"}
```

# 处理集合

Jackson反序列化的另一个有用特性是：能够从JSON数组响应中生成所需的集合类型。

例如：

```java
String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
Car[] cars = objectMapper.readValue(jsonCarArray, Car[].class);
// print cars
```

或者反序列化为`List`：

```java
String jsonCarArray = 
  "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
ObjectMapper objectMapper = new ObjectMapper();
List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
// print cars
```

# 总结

`Jackson ObjectMapper`是一个用于Java的可靠而成熟的JSON序列化/反序列化库。 `ObjectMapper API`提供了一种简单的方法在json和Java对象之间进行序列化/反序列化。