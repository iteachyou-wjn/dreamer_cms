<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：类似产品中心，带多级子栏目的筛选功能如何开发？
产品中心的栏目结构如下：

```javascript
产品中心（typeid=A54547W2）
    |- 上衣
    |- 裤子
    |- 鞋袜
    |- 内衣
    |- 帽子
    |- 饰品
```

在以上各栏目中，均添加有不同的产品，具体实现方式如下：

1. 利用Channel标签，渲染产品分类栏目，如下：

```javascript
<ul class="product-condition">
    <!--渲染全部-->
    <li>
        {dreamer-cms:type typeid="A54547W2"}<a href="[field:typeurl/]">全部</a>{/dreamer-cms:type}
    </li>
    <!--渲染子栏目-->
    {dreamer-cms:channel typeid="A54547W2" showall="false" type="son"}
        <li><a href="[field:typeurl/]">[field:typenamecn /]</a></li>
    {/dreamer-cms:channel}
</ul>
<!--产品列表-->        
<div class="product-list">
    
</div>
```

2. 利用pagelist标签，渲染产品列表
产品列表，渲染在上方预留的div中，需要注意，在pagelist标签上，一定要使用cascade="true"属性。

```javascript
<!--产品列表-->        
<div class="product-list">
    {dreamer-cms:pagelist cascade="true"}
    <div class="product-list-item">
        <div class="product-list-item-image">
            <img src="[field:litpic /]" width="180" height="120" />
        </div>
        <div class="product-list-item-title">
            <a href="[field:arcurl /]" title="[field:title /]">[field:title /]</a>
        </div>
    </div>
    {/dreamer-cms:pagelist}
</div>
<!--分页-->
<div class="product-page"></div>
```

3. 添加分页

```javascript
<!--分页-->
<div class="product-page">
    {dreamer-cms:pagination /}
</div>
```
