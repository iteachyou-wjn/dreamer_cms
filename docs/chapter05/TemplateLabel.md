<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Template全局变量标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
模版路径标签
</p>

#### 功能
<p>
用于获取系统模版所在的根目录
</p>

#### 语法
```html?linenums
{dreamer-cms:template /}bootstrap/css/bootstrap.min.css
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.TemplateTag
```

#### 参数
无

#### 底层字段
无

#### 范例
```html?linenums
<link rel="stylesheet" href="{dreamer-cms:template /}bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="{dreamer-cms:template /}css/font-awesome.min.css">
<link rel="stylesheet" href="{dreamer-cms:template /}plugins/slick/slick.css">
<link rel="stylesheet" href="{dreamer-cms:template /}plugins/slick/slick-theme.css">
<link rel="stylesheet" href="{dreamer-cms:template /}css/app.css">
<link rel="stylesheet" href="{dreamer-cms:template /}css/responsive.css">
<script src="{dreamer-cms:template /}js/jquery-3.2.1.slim.min.js"></script>
<script src="{dreamer-cms:template /}bootstrap/js/bootstrap.min.js"></script>
<script src="{dreamer-cms:template /}js/popper.min.js"></script>
<script src="{dreamer-cms:template /}plugins/slick/slick.min.js"></script>
<script src="{dreamer-cms:template /}js/app.js"></script>
```

#### 效果
无
