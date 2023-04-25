<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Sql标签

#### 适用版本
<p>
Previous_Releases_3.5.1 + 
</p>


#### 名称
<p>
自定义Sql标签
</p>


#### 功能
<p>
用于在页面中作简单的查询，例如获取某栏目下的单个文章，可以在后台文章列表页面，复制文章ID，写在Sql中。注：Sql中的单引号须转义成[]来实现。
</p>



#### 语法
```html?linenums
{dreamer-cms:sql sql="select username as username,sex from system_user where id = [1]"}
	<div>[field:username /]</div>
{/dreamer-cms:sql}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.SqlTag
```

#### 参数
参数名|描述
:--:|:--
sql|sql语句，注：Sql中的单引号须转义成[]来实现。

#### 底层字段
底层字段取决于查询的字段，查询语句中如有用到as别名，则使用[field:别名 /]来获取；如没用到别名，则使用真实字段名[field:真实字段名 /]来获取。

#### 范例
```html?linenums
{dreamer-cms:sql sql="select username as username,sex from system_user where id = [1]"}
	<div>[field:username /]</div>
{/dreamer-cms:sql}
```

#### 效果
无
