<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### If判断标签

#### 适用版本
<p>
Previous_Releases_4.0.0 + 
</p>

#### 名称
<p>
当前位置标签
</p>

#### 功能
<p>
该标签用于在分页（封面页面和列表页面和文章页面）页面中展示当前位置
</p>

#### 语法
```html?linenums
{dreamer-cms:location lang="cn" /}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.LocationTag
```

#### 参数
参数名|描述
:--:|:--
lang|语言（cn或en）用于展示中文名称或英文名称

#### 底层字段
无

#### 范例
```html?linenums
{dreamer-cms:location lang="cn" /}
```

#### 效果
会返回当前位置html片段，样式可自行控制，如
```html?linenums
<ul class="dreamer-location">
    <li>
        <a href="/" title="首页">首页</a>
    </li>
    <li>
        <a href="/" title="更新记录">更新记录</a>
    </li>
</ul>
```
