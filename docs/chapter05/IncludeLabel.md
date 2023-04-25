<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Include引入文件标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
引入文件标签
</p>

#### 功能
<p>
用于引入其它模板的内容，以方便拆分头部、尾部等公共部分
</p>

#### 语法
```html?linenums
{dreamer-cms:include file='header.html'/}
或
{dreamer-cms:include file="header.html"}{/dreamer-cms:include}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.IncludeTag
```

#### 参数
参数名|描述
:--:|:--
file|要引入的模版文件名称

#### 底层字段
无

#### 范例
```html?linenums
{dreamer-cms:include file='header.html'/}
```

#### 效果
无
