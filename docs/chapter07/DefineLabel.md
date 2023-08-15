<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 定义标签
在实际开发中，可能系统自带的标签满足不了复杂需求，这时需要用户来自定义标签，前面的介绍，我们已经知道标签有统一的接口，就是：
```java?linenums
cc.iteachyou.cms.taglib.IParse
```
我们只需要实现该接口，重写两个parse方法即可，然后在统一入口中来调用就可以了。
```java?linenums
cc.iteachyou.cms.taglib.ParseEngine
```
并且自定义标签类，还需要添加@Tag注解，如有属性还需要添加@Attribute注解，最后需要将该类纳入Spring管理，添加@Component注解

#### 范例
```java?linenums
@Component
@Tag(beginTag="{dreamer-cms:custom}",endTag="{/dreamer-cms:custom}",regexp="\\{dreamer-cms:custom[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:custom\\}", attributes={
		@Attribute(name = "attr1",regex = "[ \t]+attr1=\".*?\""),
	})
public class CustomTag implements IParse {

	@Override
	public String parse(String html) {
		// 解析代码
		return null;
	}

	@Override
	public String parse(String html, String params) {
		// 解析代码
		return null;
	}

}
```
