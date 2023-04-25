<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Channel频道列表标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
栏目标签
</p>

#### 功能
<p>
常用标记，通常用于网站顶部以获取站点栏目信息
</p>

#### 语法
```html?linenums
{dreamer-cms:channel typeid="TNLESKZ4" showall="true" type="son"}
	<li><a href="[field:typeurl/]">[field:typenamecn /]</a></li>
{/dreamer-cms:channel}
```

#### 函数介绍
```html?linenums
使用示例：
[field:typenamecn function="substring(0,50,'...')" /]
或
[field:createtime function="format('yyyy-MM-dd')" /]
或
[field:autoindex function="steps(5,1)" /]

1. function="substring(参数1,参数2,参数3)"
    函数说明：截取字段为指定长度，function=""，必须为双引号包裹。
    参数1：开始截取的位置
    参数2：截取长度
    参数3：替换字符，如...。注：参数3需要用英文单引号包裹，例：'...'
2. function="format(参数)"
    函数说明：格式化日期数据为指定格式
    参数：格式化字符串，参照SimpleDateFormat类，注：参数需要用英文单引号包裹，例：'yyyy-MM-dd'
3. function="steps(参数1, 参数2)"
    函数说明：指定autoindex的起始序号以及步长
    参数1：起始序号
    参数2：步长
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.ChannelTag
```

#### 参数
参数名|描述
:--:|:--
typeid|后台栏目管理列表页面中的编码，通过唯一编码，获取指定栏目
start|从第几个开始获取
length|获取个数
type|显示类型（top为顶级栏目，son在设置typeid时起作用，获取指定栏目的下级栏目）
showall|是否显示隐藏栏目（true或false）

#### 底层字段
字段名|描述
:--:|:--
typeid|栏目ID
typenamecn|栏目中文名称
typenameen|栏目英文名称
typecode|栏目编码（8位唯一）
typeseq|栏目的层级
typeimg|栏目图片
description|栏目描述
linktarget|链接目标
pagesize|分页大小（该属性控制列表页面默认的分页大小）
visiturl|访问URL（浏览器地址栏中将以该url显示，如果为空，则为栏目中文名称的拼音）
linkurl|链接URL
editor|编辑器（md或ue）
mdcontent|markdown内容
htmlcontent|生成的html内容
parentname|上级栏目名称
isshow|是否显示
level|栏目层级
sort|排序（默认为50）
createby|创建人
createtime|创建时间
updateby|更新人
updatetime|更新时间
typeurl|栏目URL
ext01|扩展字段1
ext02|扩展字段2
ext03|扩展字段3
ext04|扩展字段4
ext05|扩展字段5
haschildren|是否有下级栏目（该属性在dreamer-cms:type标签下不可用）

#### 范例
```html?linenums
<div class="popup" id="popup">
    <div class="popup-content"><div class="left-slide-sidebar ">
        <div class="canvas-title">DreamerCMS MENUS</div>
        <ul class="list-unstyled slide-sidebar">
            <li><a href="#">首页</a></li>
            {dreamer-cms:channel type='top' currentstyle=""}
            <li><a href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a></li>
            {/dreamer-cms:channel}
        </ul>
    </div>
        <a href="#close" class="popup-close">&times;</a>
    </div>
</div>
```

#### 效果
![channel标签](https://oss.iteachyou.cc/20190820092154.png "channel标签")
