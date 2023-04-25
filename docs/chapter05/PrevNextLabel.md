<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### PrevNext上一篇、下一篇标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
上一篇、下一篇标签
</p>

#### 功能
<p>
用于在文章详情页面展示上一篇、下一篇
</p>

#### 语法
```html?linenums
{dreamer-cms:prevnext layout="prev,next" /}
或
{dreamer-cms:prevnext layout="prev,next"}{/dreamer-cms:prevnext}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.PrevNextTag
```

#### 参数
参数名|描述
:--:|:--
layout|要展示上一篇、下一篇的布局，可选项[prev/next/prev,next]

#### 底层字段
无

#### 范例
```html?linenums
{dreamer-cms:prevnext layout="prev,next" /}
```

#### 效果
无
