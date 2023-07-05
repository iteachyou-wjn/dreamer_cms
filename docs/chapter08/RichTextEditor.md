<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：后台管理系统中，富文本编辑器无法上传图片？
先在F12控制台中，查看url=upload/ueditorConfig的请求，如该请求返回配置“配置文件初始化失败”，则说明资源目录配置错误，导致资源目录中的config.json文件并没有正确读取到。

请查看 【 <a href="/chapter07/ResourceDir.html" title="资源目录问题">资源目录问题</a> 】
