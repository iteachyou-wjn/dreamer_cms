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
判断标签
</p>

#### 功能
<p>
用于在页面中作简单的判断，目前只支持eq和neq，且只用于在categoryartlist标签内使用。
</p>


#### 语法
```html?linenums
{dreamer-cms:if test="('false' eq [field:haschildren/])"}
	<li><a ref="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a></li>
 {/dreamer-cms:if}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.IfTag
```

#### 参数
参数名|描述
:--:|:--
test|表达式（注：一定要用小括号包裹，目前只支持eq和neq。）

#### 底层字段
无

#### 范例
```html?linenums
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
```

#### 效果
无
