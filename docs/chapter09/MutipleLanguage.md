<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：如何开发多语言的网站？
梦想家内容管理系统，对于网站前台是支持多语言的，但是后台目前无法做到国际化。

### 前台多语言网站如休开发？

#### 1. 在后台栏目/文章模块下，录入两个顶级栏目，栏目的栏目模型要选择`封面`，如下

* 中文
* English

#### 2. 假设前台的网站导航为：

| 中文站点： | 首页  | 关于我们 | 产品中心 | 成功案例| 解决方案 | 联系我们  |
|-------|-----|------|------|------|------|-------|

---

| 英文站点： | Home | About | Product | Case | Solution | Contact |
|------|------|------|------|------|------|------|

#### 3. 分别在中文和English顶级栏目下，录入第二步的栏目导航，形成如下结构

```javascript
中文(4ol61bbh)
    关于我们
    产品中心
    成功案例
    解决方案
    联系我们
English(he9g4d0j)
    About
    Product
    Case
    Solution
    Contact
```

#### 4. 新建前台模板文件，注意：中文和英文分开两个模板，如首页（index.html和index_en.html），其它栏目同理。

#### 5. 开发模板即可，在中文站模板中，使用标签时，typeid均为中文顶级栏目的typeid，英文站模板均为English的typeid，如：

```html
    <!--中文站-->
    <ul>
        <li>
            {dreamer-cms:type typeid="4ol61bbh"}
            <a href="[field:typeurl /]" title="首页">首页</a>
            {/dreamer-cms:type}
        </li>
        {dreamer-cms:channel typeid="4ol61bbh" type="son"}
        <li>
            <a href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a>
        </li>
        {/dreamer-cms:channel}
    </ul>

    <!--英文站-->
    <ul>
        <li>
            {dreamer-cms:type typeid="he9g4d0j"}
            <a href="[field:typeurl /]" title="Home">Home</a>
            {/dreamer-cms:type}
        </li>
        {dreamer-cms:channel typeid="he9g4d0j" type="son"}
        <li>
            <a href="[field:typeurl /]" title="[field:typenamecn /]">[field:typenamecn /]</a>
        </li>
        {/dreamer-cms:channel}
    </ul>
```

#### 6. 其它需要在栏目内添加文章的，需要同一篇文章，分别在中英文对应的栏目中分别录入。
