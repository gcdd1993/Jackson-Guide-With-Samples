# 介绍
> 在本文中，我们将概述`Optional`类，然后解释在与 Jackson 一起使用时可能遇到的一些问题。
接下来，我们将介绍一种解决方案，让 Jackson 将`Optional`类视为普通的可空对象。

# 问题概述
> 首先，让我们来看看当我们尝试使用Jackson序列化和反序列化`Optionals`时会发生什么。

## Maven依赖

引入Jackson最新依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.9.6</version>
</dependency>
```

## 实体对象`Book`

然后，让我们创建一个类`Book`，其中包含一个普通字段和一个`Optional`字段：

```java

```

**【注意】** `Optionals`不应该用作字段，我们这样做是为了演示问题。

## 序列化

```java
@Test
public void test1() throws JsonProcessingException {
    Book book = new Book();
    book.setTitle("Oliver Twist");
    book.setSubTitle(Optional.of("The Parish Boy's Progress"));
    ObjectMapper mapper = new ObjectMapper();
    String result = mapper.writeValueAsString(book);
    System.out.println(result);
}
```

我们将看到Optional字段的输出不包含其值，而是包含一个名为present的字段的嵌套JSON对象：

```json
{
    "title":"Oliver Twist",
    "subTitle":{
        "present":true
    }
}
```

虽然这可能看起来很奇怪，但它实际上是有道理的。

在这种情况下，`isPresent()`是`Optional`类的公共`getter`。这意味着它将使用值true或false进行序列化，具体取决于它是否为空。这是Jackson的默认序列化行为。

不过，我们想要序列化的实际上是`Optional`的`value`值。

## 反序列化

现在，让我们颠倒前面的例子，这次尝试将对象反序列化为`Optional`。我们将看到现在我们得到一个`JsonMappingException`：

```java
@Test(expected = JsonMappingException.class)
public void givenFieldWithValue_whenDeserializing_thenThrowException() throws IOException {
    String bookJson = "{ \"title\": \"Oliver Twist\", \"subTitle\": \"foo\" }";
    ObjectMapper mapper = new ObjectMapper();
    Book result = mapper.readValue(bookJson, Book.class);
}
```

让我们来看看堆栈跟踪：

```java
com.fasterxml.jackson.databind.JsonMappingException:
  Can not construct instance of java.util.Optional:
  no String-argument constructor/factory method to deserialize from String value ('The Parish Boy's Progress')
```

## 结论

我们的目的是，序列化时，Jackson将空的Optional视为null；反序列化时，将json序列化到`Optional`的值。

还好，Jackson已经帮我们解决了这个问题。Jackson有一组处理JDK 8数据类型的模块，包括`Optional`。

# Maven依赖和注册模块

首先，引入Jackson JDK8 依赖：

```xml
<dependency>
   <groupId>com.fasterxml.jackson.datatype</groupId>
   <artifactId>jackson-datatype-jdk8</artifactId>
   <version>2.9.6</version>
</dependency>
```

接着，使用`ObjectMapper`注册模块：

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new Jdk8Module());
```

## 序列化

现在，让我们来测试一下。如果我们再次尝试序列化我们的Book对象：

```java
@Test
public void test2() throws JsonProcessingException {
    Book book = new Book();
    book.setTitle("Oliver Twist");
    book.setSubTitle(Optional.of("The Parish Boy's Progress"));
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    String result = mapper.writeValueAsString(book);
    System.out.println(result);
}
```

输出结果：

```json
{
    "title":"Oliver Twist",
    "subTitle":"The Parish Boy's Progress"
}
```

如果将`subTitle`字段设置为`Optional.empty()`，将会被序列化为null：

```java
book.setSubTitle(Optional.empty());
```

输出结果：

```json
{
    "title":"Oliver Twist",
    "subTitle":null
}
```

## 反序列化

现在，让我们重复我们的反序列化测试。这次，将不会抛出`JsonMappingException`异常：

```java
@Test
public void givenFieldWithValue() throws IOException {
    String bookJson = "{ \"title\": \"Oliver Twist\", \"subTitle\": \"foo\" }";
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    Book result = mapper.readValue(bookJson, Book.class);
}
```

如果是null，将会反序列化为`Optional.empty()`对象。