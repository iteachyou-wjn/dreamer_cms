<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 模板语法
<p>
DreamerCMS模板引擎是一种使用XML名字空间形式的模板解析器，使用解析器解析模板的最大好处是可以轻松的制定标记的属性，感觉上就像在用HTML一样，使模板代码十分直观灵活。
</p>

#### 代码样式
<p>
{dreamer-cms:标记名称 属性="值" /}<br />
{dreamer-cms:标记名称 属性="值"}{/dreamer-cms:标记名称}<br />
{dreamer-cms:标记名称 属性="值"}自定义样式模板(InnerText){/dreamer-cms:标记名称}
</p>

```html?linenums
注：模板的标记，必须严格用{dreamer-cms:标记名称 属性="值"}{/dreamer-cms:标记名称} 这种格式，否则会报错。
```
