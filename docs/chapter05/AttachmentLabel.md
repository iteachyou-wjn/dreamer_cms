<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Attachment附件标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
附件标签
</p>

#### 功能
<p>
用于在页面中展示指定的附件，可用于下载功能。
</p>

#### 语法
```html?linenums
{dreamer-cms:attachment key="ez159g51"}
	<a href="[field:dlurl /]" target="_blank" class="btn btn-primary main-btn bg-main">一键运行版下载</a>
{/dreamer-cms:attachment}
```

#### 函数介绍
```html?linenums
使用示例：
[field:filename function="substring(0,50,'...')" /]
或
[field:createtime function="format('yyyy-MM-dd')" /]

1. function="substring(参数1,参数2,参数3)"
    函数说明：截取字段为指定长度，function=""，必须为双引号包裹。
    参数1：开始截取的位置
    参数2：截取长度
    参数3：替换字符，如...。注：参数3需要用英文单引号包裹，例：'...'
2. function="format(参数)"
    函数说明：格式化日期数据为指定格式
    参数：格式化字符串，参照SimpleDateFormat类，注：参数需要用英文单引号包裹，例：'yyyy-MM-dd'
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.AttachmentTag
```

#### 参数
参数名|描述
:--:|:--
key|后台附件管理中的编码，通过唯一编码，获取指定附件

#### 底层字段
字段名|描述
:--:|:--
id|附件ID
filename|附件名称
filetype|附件类型（为MultipartFile类的ContentType）
filesize|附件大小
dlurl|下载连接
createby|创建人
createtime|创建时间

#### 范例
```html?linenums
{dreamer-cms:attachment key="ez159g51"}
	<a href="[field:dlurl /]" target="_blank" class="btn btn-primary main-btn bg-main">一键运行版下载</a>
{/dreamer-cms:attachment}
```

#### 效果
![attachment标签](https://oss.iteachyou.cc/20191225142748.png "attachment标签")
