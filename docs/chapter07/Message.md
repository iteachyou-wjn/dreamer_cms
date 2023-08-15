<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 留言投稿
在网站开发时，难免会有留言投稿的需求，Dreamer CMS考虑到这点，为大家提供了留言投稿的功能。以下内容详细介绍在自定义模板中，如何使用留言投稿的功能。

模板文件：
```
新版：default_v2/list_case.html
旧版：default/list_message.html
```

#### 前端表单
在使用留言投稿功能时，为了防止恶意留言，我们必须要使用图形验证码功能进行校验。
1. 网站后台
```javascript?linenums
在网站后台栏目管理中，要针对留言的栏目，必须要开启允许投稿。
```
![list标签](https://oss.iteachyou.cc/20230227082602.png "留言投稿")

2. 验证码地址
```javascript?linenums
var captchaURL = "/getKaptcha?t=" + new Date().getTime();
// 建议每次切换都加上时间戳作为参数，避免缓存
```
3. 表单定义
```html?linenums
<form action="/input" method="post">
    <input type="hidden" name="typeid" value="wqz18j7q" />
    <input type="hidden" name="formkey" value="8t2lkr8d" />
    <div class="item">
        <input type="text" id="title" placeholder="填写您的姓名" name="title">
    </div>
    <div class="item">
        <input type="text" id="telephone" placeholder="填写您的联系电话" name="telephone">
    </div>
    <div class="item">
        <textarea id="content" name="content" class="form-control" placeholder="填写您的需求"></textarea>
    </div>
    <div class="captcha">
        <div class="login-form-vcode"><img alt="图片验证码" width="120" height="46" src="" id="vcode"></div>
        <div class="login-form-vcode-input"><input type="text" name="captcha" placeholder="验证码" required="required" class="form-control" /></div>
        <div class="clearFix"></div>
    </div>
    <div class="form-btn-group-left">
        <div class="submission set_5_button" id="essentialInformation">提交</div>
    </div>
</form>
```

注意：
1. 提交地址必须为/input，方式必须为POST。
2. captcha: 验证码字段名，不可改变。
3. typeid：使用隐藏域来定义该留言提交后，要提交到后台的哪一个栏目中，不可改变。
4. formkey：该栏目所使用的表单模型，指定后可使用表单模型中定义的字段名，不可改变。
5. title：公共字段，文章标题，不可改变。
6. telephone|content：表单模型中定义的字段。

#### 后端接收
```java?linenums
cc.iteachyou.cms.controller.FrontController
```
在该类中的input方法，为后端接收前台留言的控制器。返回JSON数据，格式如下：
```javascript?linenums
{
    "success": true,
    "state": "200",
    "data": null,
    "info": "操作成功"
}
// 前台只需要判断response.state === "200"即可，其它结果均为失败。
```

#### 范例
![list标签](https://oss.iteachyou.cc/20230227083501.png "留言投稿")
