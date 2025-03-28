<div style="display: flex;">
	<img src="http://121.8.219.2:18887/logo.png" height="30" />
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
会返回上一篇、下一篇html片段，样式可自行控制，如
```html?linenums
<ul class='dreamer-prevnext'>
    <li class='dreamer-prev'>
        <span class='dreamer-prevnext-label'>上一篇：</span>
        <a href='/article/7a4bc5cade9845bbb2528783d995ce20' title='智慧校园管理系统平台组成内容有哪些'>智慧校园管理系统平台组成内容有哪些</a>
    </li>
    <li class='dreamer-next'>
        <span class='dreamer-prevnext-label'>下一篇：没有了</span>
    </li>
</ul>
```
