<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Global全局配置标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
全局配置标签
</p>

#### 功能
<p>
用于获取系统后台系统设置中的配置信息
</p>

#### 语法
```html?linenums
{dreamer-cms:global name="title" /}
或
{dreamer-cms:global name="title"}{/dreamer-cms:global}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.GlobalTag
```

#### 参数
参数名|描述
:--:|:--
name|系统设置中的配置信息的Key

#### 底层字段
字段名|描述
:--:|:--
website|网站地址
title|网站标题
keywords|网站关键字
describe|网站描述
icp|网站备案号
copyright|网站版权
uploaddir|网站上传目录
appid|暂无作用
appkey|暂无作用

#### 范例
```html?linenums
<footer class="footer">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-5 col-md-6 col-sm-5 align-self-center">
            	<div>
            		<p>
            			{dreamer-cms:global name="copyright" /}
            			备案号：{dreamer-cms:global name="icp" /}
            		</p>
            	</div>

            </div>
            <div class="col-lg-7 col-md-6 col-sm-7 align-self-center">
                <ul class="list-inline footer-menu">
                	{dreamer-cms:channel type='top' currentstyle=""}
                    <li class="list-inline-item"><a href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a></li>
                    {/dreamer-cms:channel}
                </ul>
            </div>
        </div>
    </div>
</footer>
```

#### 效果
![global标签](https://oss.iteachyou.cc/20190820164930.png "global标签")
