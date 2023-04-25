<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 标签分类
<p>
每个标签都会有他的作用域，我们已经了解系统文档页面可以分为：封面、列表、内容几个部分，每个页面的模板则会涉及到不同的标签，所以标签也有了他的作用域，我们在模板制作过程中主要根据模板的作用域来对模板进行如下划分：
</p>

#### 全局标签
<p>
可以在前台文档任意页面使用的模板标记，例如：global、variable、type、template、list、channel、categoryartlist、include等。
</p>

#### 分页标签
<p>
可以在除首页外的其它任意模板中使用的标签，例如：category、topcategory、location。
</p>

#### 列表标签
<p>
仅在模板*_list.htm中可以使用的标签，例如：pagelist、pagination。
</p>

#### 内容标签
<p>
仅在模板*_.article.html中可以使用的模板标记，例如：article。
</p>
