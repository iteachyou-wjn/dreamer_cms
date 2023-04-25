<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 核心文件
<p>
模版引擎相关代码都在cc.iteachyou.cms.taglib包下，如下
</p>

```html?linenums
taglib
	annotation				//注解包
		Tag.java			//标签注解
		Attribute.java		//属性注解
	enums						//枚举包
		FieldEnum.java		//字段正则枚举
	tags						//标签解析包
		AbstractChannelTag.java
		AbstractListTag.java
		ArticleTag.java						//文章标签
		AttachmentTag.java                  //附件标签
		CategoryTag.java					//栏目标签
		ChannelArtListTag.java				//栏目列表标签
		ChannelTag.java						//频道标签
		GlobalTag.java						//全局设置标签
		IfTag.java							//判断标签
		IncludeTag.java						//引入文件标签
		LabelTag.java                       //关键字标签
		ListTag.java						//列表标签
		LocationTag.java                    //当前位置标签
		PageListTag.java					//分页列表标签
		PaginationTag.java					//分页显示标签
		PrevNextTag.java                    //上一篇、下一篇标签
		SqlTag.java                         //自定义Sql标签
		TemplateTag.java					//模版目录标签
		TopCategoryTag.java                 //顶级栏目标签
		TypeTag.java						//单个频道标签
		VariableTag.java					//变量标签
	utils
		PinyinUtils.java				//汉字转拼音工具类
		RegexUtil.java					//正则工具类
	IParse.java			//接口
	ParseEngine.java		//解析引擎
```
