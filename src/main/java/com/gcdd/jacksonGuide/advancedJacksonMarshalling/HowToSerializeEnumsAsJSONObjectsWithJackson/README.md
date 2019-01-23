# 总览
> 本快速教程将展示如何使用Jackson 2.x控制Java Enum序列化为JSON输出的方式。

# 控制枚举序列化

让我们定义以下枚举：

```java
public enum Distance {
    KILOMETER("km", 1000),
    MILE("miles", 1609.34),
    METER("meters", 1),
    INCH("inches", 0.0254),
    CENTIMETER("cm", 0.01),
    MILLIMETER("mm", 0.001);

    private String unit;
    private final double meters;

    Distance(String unit, double meters) {
        this.unit = unit;
        this.meters = meters;
    }
}
```

# 默认枚举序列化

默认情况下，Jackson将Java Enums表示为简单的String  - 例如：

```java
new ObjectMapper().writeValueAsString(Distance.MILE);
```

将会输出如下：

```json
"MILE"
```

而我们想要的是：

```json
{"unit":"miles","meters":1609.34}
```

# 将枚举作为Json对象序列化

从Jackson 2.1.2开始 - 现在是一个配置选项，可以通过@JsonFormat注释在Enum级别处理这种表示：

```java
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Distance { ... }
```

在为`Distance.MILE`序列化此枚举时，将是我们想要的答案：

```json
{"unit":"miles","meters":1609.34}
```

# 枚举和`@JsonValue`

另一种控制枚举的编组输出的简单方法是在`getter`上使用`@JsonValue`注释：

```java
public enum Distance { 
    ...
  
    @JsonValue
    public String getMeters() {
        return meters;
    }
}
```

这时候`getMeters()`方法是序列化的实际调用方法，所以输出如下：

```json
1609.34
```

# Enum的自定义序列化程序

在Jackson 2.1.2之前，或者如果枚举需要更多自定义，我们可以使用自定义Jackson序列化程序：

```java
public class DistanceSerializer extends StdSerializer<Distance> {
    public DistanceSerializer() {
        super(Distance.class);
    }

    public DistanceSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(Distance distance, JsonGenerator generator,
                          SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString(distance.name());
        generator.writeFieldName("unit");
        generator.writeString(distance.getUnit());
        generator.writeFieldName("meters");
        generator.writeNumber(distance.getMeters());
        generator.writeEndObject();
    }
}
```

然后将自定义序列化程序和我们的类绑定在一起：

```java
@JsonSerialize(using = DistanceSerializer.class)
public enum TypeEnum { ... }
```

输出结果：

```json
{"name":"MILE","unit":"miles","meters":1609.34}
```