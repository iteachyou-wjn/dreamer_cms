<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 正则工具类
在扩展标签时，需要对html模版进行正则解析，具体可使用系统中的正则工具类
```java?linenums
cc.iteachyou.cms.taglib.utils.RegexUtil
```
该类提供了正则匹配相关的一系列静态方法。

#### 范例
```java?linenums
//获取html中的标签
List<String> tags = RegexUtil.parseAll(html, channelAnnotation.regexp(), 0);
//获取html中的标签中的内容
List<String> contents = RegexUtil.parseAll(html, channelAnnotation.regexp(), 1);
```
