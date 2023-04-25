<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### If判断标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
标签列表标签
</p>

#### 功能
<p>
用于在页面中展示文章标签列表
</p>

#### 语法
```html?linenums
{dreamer-cms:label start="0" length="8"}
	<a href="#">
	[field:tagname /]
	</a>
{/dreamer-cms:label} 
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.LabelTag
```

#### 参数
参数名|描述
:--:|:--
start|从第几个开始获取
length|获取个数

#### 底层字段
字段名|描述
:--:|:--
autoindex|序号
id|标签ID
tagname|标签名称
tagfirstchar|标签首字母
tagpinyin|标签拼音

#### 范例
```html?linenums
{dreamer-cms:label start="0" length="8"}
	<a href="#">
	[field:tagname /]
	</a>
{/dreamer-cms:label} 
```

#### 效果
![channel标签](https://oss.iteachyou.cc/20191219111039.png "tag标签")
