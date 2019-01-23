# 总览
> 在本文中，我们将介绍如何使用Jackson 2.x将Java对象序列化为XML数据并将其反序列化为POJO。

#　`XmlMapper`对象

`XmlMapper`是Jackson 2.x中的主要类，它帮助我们进行序列化，因此我们需要创建它的一个实例：

```java
XmlMapper mapper = new XmlMapper();
```

依赖:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.4</version>
</dependency>
```

# 将Java序列化为XML
> XmlMapper是ObjectMapper的子类，用于JSON序列化。但是，它会向父类添加一些XML特定的调整。

我们现在可以看看如何使用它来进行实际的序列化。让我们创建要序列化的对象的java类：

```java
@Data
public class SimpleBean {
    private int x = 1;
    private int y = 2;
}
```

## 序列化为XML字符串

我们可以将Java对象序列化为XML字符串：

```java
@Test
public void whenJavaSerializedToXmlStr_thenCorrect() throws JsonProcessingException {
    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(new SimpleBean());
    System.out.println(xml);
    assertNotNull(xml);
}
```

输出结果:

```xml
<SimpleBean>
    <x>1</x>
    <y>2</y>
</SimpleBean>
```

## 序列化为XML文件

我们还可以将Java对象序列化为XML文件以供以后使用：

```java
@Test
public void whenJavaSerializedToXmlFile_thenCorrect() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.writeValue(new File("simple_bean.xml"), new SimpleBean());
    File file = new File("simple_bean.xml");
    assertNotNull(file);
}
```

在当前目录下生成了文件 simple_bean.xml，内容如下：

```xml
<SimpleBean>
    <x>1</x>
    <y>2</y>
</SimpleBean>
```

# 将XML反序列化为Java
> 在本节中，我们将了解如何从XML获取Java对象。

## 从XML字符串反序列化

与序列化一样，我们也可以将XML String反序列化回Java对象，如下所示：

```java
@Test
public void whenJavaGotFromXmlStr_thenCorrect() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    SimpleBean value =
            xmlMapper.readValue("<SimpleBean><x>1</x><y>2</y></SimpleBean>",
                    SimpleBean.class);
    assertTrue(value.getX() == 1 && value.getY() == 2);
}
```

## 从XML文件反序列化

同样，如果我们有一个XML文件，本节将介绍如何将其转换回Java对象。

这里我们首先将文件读入输入流，然后使用简单的实用方法将输入流转换为字符串。

其余代码类似于上面4.1节中的代码：

```java
@Test
public void whenJavaGotFromXmlFile_thenCorrect() throws IOException {
    File file = new File("simple_bean.xml");
    XmlMapper xmlMapper = new XmlMapper();
    String xml = inputStreamToString(new FileInputStream(file));
    SimpleBean value = xmlMapper.readValue(xml, SimpleBean.class);
    assertTrue(value.getX() == 1 && value.getY() == 2);
}
```

用到的`inputStreamToString`方法：

```java
public static String inputStreamToString(InputStream is) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    while ((line = br.readLine()) != null) {
        sb.append(line);
    }
    br.close();
    return sb.toString();
}
```

# 处理大写元素
> 在本节中，我们将讨论如何处理带有大写元素的XML反序列化的场景，要么我们需要将一个或多个元素大写的java对象序列化为XML。

## 从XML字符串反序列化

假设我们有一个字段大写的XML：

```xml
<SimpleBeanForCapitalizedFields>
    <X>1</X>
    <y>2</y>
</SimpleBeanForCapitalizedFields>
```

为了正确处理大写元素，我们需要使用@JsonProperty注释来注释“x”字段：

```java
@Data
public class SimpleBeanForCapitalizedFields {
    @JsonProperty("X")
    private int x = 1;
    private int y = 2;
}
```

我们现在可以正确地将XML String反序列化回Java对象：

```java
@Test
public void whenJavaGotFromXmlStrWithCapitalElem_thenCorrect() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    SimpleBeanForCapitalizedFields value = xmlMapper.
      readValue("<SimpleBeanForCapitalizedFields><X>1</X><y>2</y></SimpleBeanForCapitalizedFields>",
        SimpleBeanForCapitalizedFields.class);
    assertTrue(value.getX() == 1 && value.getY() == 2);
}
```

## 序列化为XML文件

通过使用@JsonProperty注释必需的字段，我们可以正确地将Java对象序列化为具有一个或多个大写元素的XML文件：

```java
@Test
public void whenJavaSerializedToXmlFileWithCapitalizedField_thenCorrect() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.writeValue(new File("target/simple_bean_capitalized.xml"),new SimpleBeanForCapitalizedFields());
    File file = new File("target/simple_bean_capitalized.xml");
    assertNotNull(file);
}
```