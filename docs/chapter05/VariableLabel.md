<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Variable全局变量标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
全局变量标签
</p>

#### 功能
<p>
用于获取系统后台变量管理中的配置信息
</p>

#### 语法
```html?linenums
{dreamer-cms:variable name='cfg_gitee_addr'/}
或
{dreamer-cms:variable name='cfg_gitee_addr'}{/dreamer-cms:variable}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.VariableTag
```

#### 参数
参数名|描述
:--:|:--
name|变量管理中的变量名

#### 底层字段
无

#### 范例
```html?linenums
<section id="action" class="call-to-action-2">
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="call-to-action-wrap">
                    <div class="call-to-action-title">
                        <div class="text-center"><a href="{dreamer-cms:variable name='cfg_gitee_addr'/}" class="btn btn-primary main-btn">源码下载</a></div>
                        <h3>梦想家CMS内容管理系统支持码云、Github等多种渠道下载。</h3>
                    </div>
                    <div class="list-wrap">
                        <ul class="list-inline applications">
                            <li class="list-inline-item"><a href="{dreamer-cms:variable name='cfg_gitee_addr'/}"><img src="{dreamer-cms:template /}images/app1.png" alt="Application"></a></li>
                            <li class="list-inline-item"><a href="{dreamer-cms:variable name='cfg_github_addr'/}"><img src="{dreamer-cms:template /}images/app2.png" alt="Application"></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
```

#### 效果
![variable标签](https://oss.iteachyou.cc/1-1ZQ91JT40-L.png "variable标签")
