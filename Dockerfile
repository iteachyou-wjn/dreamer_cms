FROM java:8

MAINTAINER www.iteachyou.cc<153095904@qq.com>

LABEL description="I Teach You, 我教你！- 专注于IT技术分享、免费教程、学习资源的博客。"

# 指定容器时区
ENV TZ=Asia/Shanghai
# 同步时间
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建工作目录
RUN mkdir -p /dreamer_cms
# 创建资源目录
RUN mkdir -p /dreamer_cms/resources
# 创建日志目录
RUN mkdir -p /dreamer_cms/logs

# 切换工作目录
WORKDIR /dreamer_cms

# 设置环境变量
ENV SERVER_PORT=8888
ENV RESOURCE_DIR=/dreamer_cms/resources

# 暴露端口
EXPOSE ${SERVER_PORT}

# 将jar包复制到Dockerfile所在的相对目录下目录下，可以是URL，也可以是tar.gz（自动解压）；（当使用本地目录为源目录时，推荐使用 COPY)
# ADD是复制指定路径到容器路径，COPY是复制本地主机的路径到容器的路径。
ADD ./target/dreamer-cms.jar ./

# 启动命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}", "-Dweb.resource-path=${RESOURCE_DIR}", "-jar", "dreamer-cms.jar > /dreamer_cms/logs/dreamer-cms.log"]