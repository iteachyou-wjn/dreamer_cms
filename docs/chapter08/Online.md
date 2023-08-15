<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 应用打包
1. 启动应用
```shell
cd /opt/programs/
# 普通打包，以下命令根据自己实际情况来选择
nohup java -jar dreamer-cms.jar > dreamer-cms.log &
# 将jar包单独打到目录中
# nohup java -Dloader.path=libs/ -jar dreamer-cms.jar > dreamer-cms.log &
```

2. 配置Nginx
    1. 普通方式（Http）
   ```shell
   server {
        listen       80;
        server_name  www.iteachyou.cc iteachyou.cc;

        location / {
            add_header X-Via $upstream_addr;
            proxy_ignore_client_abort on;
            proxy_set_header Host  $http_host;
            proxy_set_header Cookie $http_cookie;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $http_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_pass http://127.0.0.1:8888;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
   }
   ```
    2. SSL方式（Https）
   ```shell
   server {
        listen       80;
        server_name  www.iteachyou.cc iteachyou.cc;
        proxy_set_header X-Real-IP $remote_addr;

        rewrite ^(.*)$ https://$host$1 permanent; # 强制http进入https 

        error_page  403 404  /update.html;
            location = /update.html {
            root html;
        }
        # redirect server error pages to the static page /50x.html
        error_page   500 502 503 504  /50x.html;
            location = /50x.html {
            root   html;
        }
   }
   server {
      #SSL 访问端口号为 443
      listen 443 ssl;
      #填写绑定证书的域名
      server_name www.iteachyou.cc iteachyou.cc;
      #证书文件名称（修改为自己的路径）
      ssl_certificate /opt/ssl/iteachyou.cc/1_iteachyou.cc_bundle.crt;
      #私钥文件名称（修改为自己的路径）
      ssl_certificate_key /opt/ssl/iteachyou.cc/2_iteachyou.cc.key;
      ssl_session_timeout 5m;
      #请按照这个协议配置
      ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
      #请按照这个套件配置，配置加密套件，写法遵循 openssl 标准。
      ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
      ssl_prefer_server_ciphers on;
      location / {
         add_header X-Via $upstream_addr;
         proxy_ignore_client_abort on;
         proxy_set_header Host  $http_host;
         proxy_set_header Cookie $http_cookie;
         proxy_set_header X-Real-IP $remote_addr;
         proxy_set_header X-Forwarded-For $http_x_forwarded_for;
         proxy_set_header X-Forwarded-Proto $scheme;
         proxy_pass http://127.0.0.1:8888;
         proxy_redirect ~^http://([^:]+)(:\d+)?(.*)$  https://$1$3;
      }
   }
   ```
    3. 配置Websocket，在nginx.conf中http模块中
   ```shell
   #websocket
   map $http_upgrade $connection_upgrade {
       default upgrade;
       '' close;
   }
   ```
    4. 配置Gzip压缩，在nginx.conf中http模块中
   ```shell
   # 开启gzip
   gzip on;
   # 启用gzip压缩的最小文件；小于设置值的文件将不会被压缩
   gzip_min_length 1k;
   # gzip 压缩级别 1-10 
   gzip_comp_level 4;
   # 进行压缩的文件类型。
   gzip_types text/plain application/javascript application/x-javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
   # 是否在http header中添加Vary: Accept-Encoding，建议开启
   gzip_vary on;
   ```
   
3. 上线后配置
项目通过以上步骤，则可以正常通过域名或IP访问后台，但是后台中还需要配置一些网址。网站配置=>网站地址需要修改为正式的域名或IP地址，如：https://www.iteachyou.cc/ 或 http://192.168.1.10:8888/
