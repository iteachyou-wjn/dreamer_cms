<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 应用打包
1. 在IDE中将项目中的配置文件都修改好，然后执行maven打包命令，打包前强烈建议先clean。打包前切换SpringBoot环境为prd，并对application-prd.yml中的各配置项进行配置。

2. 以下为可选配置 
   1. 将依赖jar包单独打到目录中，请参照 [Springboot打包 依赖包到单独文件夹](https://iteachyou.cc/article/a3ef3eb4ce5d467b927ecd5c69a43035)

3. 开启防火墙和安全组
   1. 防火墙需要开启80和443端口
   2. 云服务器需要在管理界面中安全组中开启80和443端口

4. 上传应用和资源目录
   1. 在/opt目录中创建programs和resources两个目录
   ```shell
      /opt
          /programs # 应用上传到该目录
          /resources # 资源目录上传到该目录
   ```
   2. 最终的目录结构
   ```shell
      /opt
          /programs # 应用上传到该目录
              /dreamer-cms.jar
          /resources # 资源目录上传到该目录
              /www.iteachyou.cc
                  /backups   # 备份目录
                  /config.json   # 富文本配置文件
                  /htmls  # 静态化目录
                  /templates   # 模板目录
                  /uploads  # 上传目录
   ```
