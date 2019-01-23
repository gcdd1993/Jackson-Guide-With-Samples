# 总览
> 本快速教程将介绍如何使用Jackson 2将JSON字符串转换为`JsonNode(com.fasterxml.jackson.databind.JsonNode)`。

# 快速解析

很简单，要解析JSON String，我们只需要一个ObjectMapper：

```java
@Test
public void whenParsingJsonStringIntoJsonNode_thenCorrect() throws JsonParseException, IOException {
    String jsonString = "{\"k1\":\"v1\",\"k2\":\"v2\"}";

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(jsonString);

    assertNotNull(actualObj);
}
```

# 低级解析

降低一个级别，看看实际来负责解析的是什么：

```java
@Test
public void givenUsingLowLevelApi_whenParsingJsonStringIntoJsonNode_thenCorrect() 
  throws JsonParseException, IOException {
    String jsonString = "{"k1":"v1","k2":"v2"}";
 
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser = factory.createParser(jsonString);
    JsonNode actualObj = mapper.readTree(parser);
 
    assertNotNull(actualObj);
}
```

# 使用`JsonNode`

将JSON解析为JsonNode对象后，我们可以使用Jackson JSON树模型：

```java
@Test
public void givenTheJsonNode_whenRetrievingDataFromId_thenCorrect() throws JsonParseException, IOException {
    String jsonString = "{\"k1\":\"v1\",\"k2\":\"v2\"}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(jsonString);

    // When
    JsonNode jsonNode1 = actualObj.get("k1");
    assertThat(jsonNode1.textValue(), equalTo("v1"));
}
```