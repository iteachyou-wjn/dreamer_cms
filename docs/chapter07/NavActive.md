<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：导航中的高亮active如何开发？
一般情况下，导航都是使用 dreamer-cms:channel 或 dreamer-cms:categoryartlist 两个标签来完成的循环，如果要开发导航高亮，则只需要按以下步骤来实现即可。

`注：梦想家提供的标签，都是服务端解析并执行，要优先于客户端的javascript执行，利用该特性，可以服务端来准备数据，客户端来做效果`

1. 循环导航，并在导航项目中添加自定义属性，代码如下：

```html
<div class="navigation">
    <div>
        <a href="/">首页</a>
    </div>
    {dreamer-cms:categoryartlist length="7"}
    <div class="dropdown" ref="[field:typeid /]">
        {dreamer-cms:if test="('true' eq [field:haschildren/])"}
        <a href="javascript:void(0)" title="[field:typenamecn /]">[field:typenamecn /]</a>
        <ul>
            {dreamer-cms:channel}
            <li><a href="[field:typeurl/]">[field:typenamecn /]</a></li>
            {/dreamer-cms:channel}
        </ul>
        {/dreamer-cms:if}
        {dreamer-cms:if test="('false' eq [field:haschildren/])"}
        <a href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a>
        {/dreamer-cms:if}
    </div>
    {/dreamer-cms:categoryartlist}
</div>
```

`注：其中的ref="[field:typeid /]"，该ref属性则为自定义属性。`

2. 在body标签中，添加自定义属性，代码如下：

```html
<header typeid="{dreamer-cms:category field='typeid' /}" parentId="{dreamer-cms:category field='parentid' /}">
    导航栏代码
</header>
```

`注：其中typeid和parentId均为自定义属性，该代码没必要一定加在header中，也可以加在页面根标签上，如body。要保证每个页面都有该属性即可。`

3. 在Javascript中，进行判断，并添加高亮样式，代码如下：

```javascript
$(document).ready(function () {
    var header = $("header");
    var typeId = header.attr("typeid");
    var parentId = header.attr("parentid");
    var isHome = true;

    var navigation = $(".navigation>div");
    for(var i = 0;i < navigation.length;i++){
        var ref = $(navigation[i]).attr("ref");
        if(ref === typeId || ref === parentId){
            isHome = false
            $(navigation[i]).addClass("active")
        }
    }
    if(isHome){
        $(navigation[0]).addClass("active")
    }
})
```

4. 在CSS中，定义.active类样式，代码如下：

```css
.active {
    color: #199fff;
    font-weight: bold;
}
```
