<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### CategoryArtListLabel栏目内容列表标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
栏目内容列表标签
</p>

#### 功能
<p>
该标签是系统中唯一的一个支持嵌套的标签，这个标签通常使用在首页（含封面首页），用于输出一组栏目内容列表，主要用于获取下级栏目的内容列表标签并适用于所有模版，通常用于网站内容列表等信息
</p>

#### 语法
```html?linenums
{dreamer-cms:categoryartlist length="1"}
	{dreamer-cms:if test="('true' eq [field:haschildren/])"}
	<li><a href="[field:typeurl/]">[field:typenamecn /]</a>
		<ul>
			{dreamer-cms:channel}
			<li><a href="[field:typeurl/]">[field:typenamecn /]</a></li>
			{/dreamer-cms:channel}
		</ul>
	</li>
	{/dreamer-cms:if}
{/dreamer-cms:categoryartlist}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.ChannelArtListTag
```

#### 参数
参数名|描述
:--:|:--
typeid|后台栏目管理列表页面中的编码，通过唯一编码，获取指定栏目
length|获取个数
showall|是否获取全部栏目（包括隐藏栏目）showall="1"

#### 底层字段
参照channel和list标签返回字段说明

#### 范例
```html?linenums
<ul class="nav navbar-nav mr-auto">
	<li class="nav-item active"><a class="nav-link scroll" href="/">首页</a></li>
	{dreamer-cms:categoryartlist length="1"}
		{dreamer-cms:if test="('true' eq [field:haschildren/])"}
		<li class="dropdown nav-item"><a href="[field:typeurl/]" class="nav-link dropdown-toggle" data-toggle="dropdown">[field:typenamecn /]</a>
			<ul class="dropdown-menu">
				{dreamer-cms:channel}
				<li><a class="dropdown-item" href="[field:typeurl/]"><i class="ion ion-chevron-right"></i>[field:typenamecn /]</a></li>
				{/dreamer-cms:channel}
			</ul>
		</li>
		{/dreamer-cms:if}
		{dreamer-cms:if test="('false' eq [field:haschildren/])"}
		<li class="nav-item"><a class="nav-link scroll" href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a></li>
		{/dreamer-cms:if}
	{/dreamer-cms:categoryartlist}
</ul>
```

#### 效果
![categoryartlist标签](https://oss.iteachyou.cc/20190820095818.png "categoryartlist标签")
