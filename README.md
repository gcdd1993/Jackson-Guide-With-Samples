# Jackson-Guide-With-Samples

**【说明】**
后面部分雷同的太多，所以不翻译了，写了篇总结，在这里👉 [Jackson使用指南](https://gcdd1993.github.io/%E5%B7%A5%E5%85%B7%E4%BD%BF%E7%94%A8/Jackson%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97/)

> 翻译自[Jackson JSON Tutorial](https://www.baeldung.com/jackson)

从事JAVA开发工作以来,一直都离不开Jackson的序列化反序列化,对于Jackson的使用也一直处于够用但不深入的状态,最近看到这篇感觉非常好的Jackson使用指南,就想着在学习的同时也翻译一遍加深印象。

# 说明

- 本文使用了[Lombok](https://projectlombok.org/)进行了代码简化。
- 本文中出现的所有测试代码都进行过验证,部分进行了稍许修改。

# Jackson基本序列化

- [Jackson注解示例(常用)](https://github.com/gcdd1993/Jackson-Guide-With-Samples/tree/master/src/main/java/com/gcdd/jacksonGuide/jacksonAnnotationExamples)
- Jackson序列化忽略属性
- Jackson序列化忽略NULL
- 更改字段名称
- Jackson ObjectMapper简介(常用)
- 使用Jackson进行XML序列化和反序列化
- Jackson-序列化字符串到JsonNode
- 与Jackson一起使用Optional

# Jackson基本反序列化

- 反序列化具有未知属性的JSON

# Jackson高级序列化

- 控制Jackson仅序列化符合自定义条件的字段
- 将枚举序列化为JSON对象
- JsonMappingException异常(没有为类找到序列化程序)
- Jackson序列化时间
- Jackson JSON Views
- Jackson异常-问题与解决方案
- Jackson-在有键值对和空值的情况下如何工作

# Jackson高级反序列化

- 反序列化到集合/数组
- Jackson-自定义反序列化类

# Jackson高级用法

- Jackson-双向关系
- Jackson-决定哪些字段被序列化/反序列化
- 使用Jackson中的树形节点
- 更多Jackson注解
- Jackson中的继承
- 使用Jackson映射嵌套值
- 使用Jackson序列化和反序列化键值对
- @JsonFormat注解的使用指南
