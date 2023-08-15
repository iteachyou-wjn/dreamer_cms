<div style="display: flex;">
<img src="https://oss.iteachyou.cc/logo.png" height="30" />
<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------

### 1. system_setting（网站配置表）

| 字段名         | 类型           | 键  | 默认值      | 字段说明           |
|-------------|--------------|----|----------|----------------|
| id          | varchar(32)  | 主键 |          | 主键             |
| website     | varchar(128) |    |          | 网址             |
| title       | varchar(64)  |    |          | 网站标题           |
| keywords    | varchar(128) |    |          | 网站关键字          |
| describe    | varchar(256) |    |          | 网站描述           |
| icp         | varchar(32)  |    |          | ICP备案号         |
| copyright   | varchar(128) |    |          | 版权             |
| uploaddir   | varchar(32)  |    | uploads/ | 上传目录           |
| staticdir   | varchar(32)  |    | htmls    | 静态化目录          |
| browse_type | int(11)      |    | 1        | 浏览方式（1：动态2：静态） |
| appid       | varchar(128) |    |          | 畅言appid（已废弃）   |
| appkey      | varchar(128) |    |          | 畅言appkey（已废弃）  |

### 2. system_user（用户表）

| 字段名             | 类型          | 键  | 默认值                 | 字段说明         |
|-----------------|-------------|----|---------------------|--------------|
| id              | varchar(32) | 主键 |                     | 主键           |
| username        | varchar(32) |    |                     | 用户名          |
| password        | varchar(32) |    |                     | 密码           |
| mobile          | varchar(32) |    |                     | 手机           |
| realname        | varchar(32) |    |                     | 真实姓名         |
| status          | int(11)     |    |                     | 状态（1：启用0：禁用） |
| page_style      | varchar(32) |    |                     | 页面风格（已废弃）    |
| salt            | varchar(64) |    | uploads/            | 密码盐          |
| last_login_ip   | varchar(32) |    | htmls               | 上次登录IP       |
| last_login_time | varchar(32) |    | 1                   | 上次登录时间       |
| portrait        | varchar(32) |    |                     | 头像           |
| create_by       | varchar(32) |    |                     | 创建人          |
| create_time     | timestamp   |    | 2018-01-01 00:00:00 | 创建时间         |
| update_by       | varchar(32) |    |                     | 修改人          |
| update_time     | timestamp   |    | CURRENT_TIMESTAMP   | 修改时间         |

### 3. system_role（角色表）

| 字段名         | 类型          | 键  | 默认值                 | 字段说明 |
|-------------|-------------|----|---------------------|------|
| id          | varchar(32) | 主键 |                     | 主键   |
| role_name   | varchar(64) |    |                     | 角色名称 |
| role_code   | varchar(8)  |    |                     | 角色编码 |
| create_by   | varchar(32) |    |                     | 创建人  |
| create_time | timestamp   |    | 2018-01-01 00:00:00 | 创建时间 |
| update_by   | varchar(32) |    |                     | 修改人  |
| update_time | timestamp   |    | CURRENT_TIMESTAMP   | 修改时间 |

### 4. system_user_role（用户角色关联表）

| 字段名     | 类型          | 键  | 默认值 | 字段说明 |
|---------|-------------|----|-----|------|
| id      | varchar(32) | 主键 |     | 主键   |
| user_id | varchar(32) |    |     | 用户ID |
| role_id | varchar(32) |    |     | 角色ID |

### 5. system_menu（菜单表）

| 字段名         | 类型           | 键  | 默认值                 | 字段说明 |
|-------------|--------------|----|---------------------|------|
| id          | varchar(32)  | 主键 |                     | 主键   |
| menu_name   | varchar(64)  |    |                     | 菜单名称 |
| menu_code   | varchar(32)  |    |                     | 菜单编码 |
| menu_url    | varchar(512) |    |                     | 菜单路径 |
| parent_id   | varchar(32)  |    |                     | 上级菜单 |
| menu_icon   | varchar(64)  |    |                     | 菜单图标 |
| sort        | int(11)      |    |                     | 排序   |
| remark      | text         |    |                     | 备注   |
| create_by   | varchar(32)  |    |                     | 创建人  |
| create_time | timestamp    |    | 2018-01-01 00:00:00 | 创建时间 |
| update_by   | varchar(32)  |    |                     | 修改人  |
| update_time | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间 |

### 6. system_permission（权限表）

| 字段名             | 类型           | 键  | 默认值                 | 字段说明 |
|-----------------|--------------|----|---------------------|------|
| id              | varchar(32)  | 主键 |                     | 主键   |
| permission_name | varchar(128) |    |                     | 权限名称 |
| permission_code | varchar(32)  |    |                     | 权限编码 |
| menu_id         | varchar(32)  |    |                     | 所属菜单 |
| permission_url  | varchar(512) |    |                     | 权限路径 |
| remark          | text         |    |                     | 备注   |
| create_by       | varchar(32)  |    |                     | 创建人  |
| create_time     | timestamp    |    | 2018-01-01 00:00:00 | 创建时间 |
| update_by       | varchar(32)  |    |                     | 修改人  |
| update_time     | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间 |

### 7. system_role_permission（角色权限关联表）

| 字段名           | 类型          | 键  | 默认值 | 字段说明          |
|---------------|-------------|----|-----|---------------|
| id            | varchar(32) | 主键 |     | 主键            |
| role_id       | varchar(32) |    |     | 角色ID          |
| permission_id | varchar(32) |    |     | 权限ID          |
| type          | int(11)     |    |     | 类型（1：菜单；2：权限） |

### 8. system_scheduled（定时任务表）

| 字段名             | 类型           | 键  | 默认值                 | 字段说明           |
|-----------------|--------------|----|---------------------|----------------|
| id              | varchar(32)  | 主键 |                     | 主键             |
| task_name       | varchar(128) |    |                     | 任务名称           |
| clazz_name      | varchar(512) |    |                     | 完整类路径          |
| cron_expression | varchar(128) |    |                     | cron表达式        |
| remark          | text         |    |                     | 备注             |
| status          | varchar(32)  |    |                     | 状态（1：启用；0：禁用；） |
| create_by       | varchar(32)  |    |                     | 创建人            |
| create_time     | timestamp    |    | 2018-01-01 00:00:00 | 创建时间           |
| update_by       | varchar(32)  |    |                     | 修改人            |
| update_time     | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间           |

### 9. system_category（栏目分类表）

| 字段名            | 类型           | 键  | 默认值                 | 字段说明                    |
|----------------|--------------|----|---------------------|-------------------------|
| id             | varchar(32)  | 主键 |                     | 主键                      |
| cnname         | varchar(128) |    |                     | 栏目中文名称                  |
| enname         | varchar(64)  |    |                     | 栏目英文名称                  |
| code           | varchar(32)  |    |                     | 编码                      |
| cat_seq        | varchar(128) |    |                     | 层级关系                    |
| form_id        | varchar(32)  |    |                     | 表单ID                    |
| image_path     | varchar(128) |    |                     | 栏目图片                    |
| description    | varchar(128) |    |                     | 栏目描述                    |
| link_target    | varchar(8)   |    |                     | 链接目标                    |
| page_size      | int(11)      |    | 20                  | 分页数量                    |
| cat_model      | int(11)      |    | 1                   | 栏目模型（1：封面；2：列表；3：外部链接；） |
| visit_url      | varchar(128) |    |                     | 访问路径                    |
| cover_temp     | varchar(128) |    |                     | 封面模版                    |
| list_temp      | varchar(128) |    |                     | 列表模版                    |
| article_temp   | varchar(32)  |    |                     | 内容页模版                   |
| link_url       | varchar(128) |    |                     | 链接路径                    |
| default_editor | varchar(10)  |    |                     | 默认编辑器                   |
| md_content     | text         |    |                     | md内容                    |
| html_content   | text         |    |                     | html内容                  |
| parent_id      | varchar(128) |    |                     | 上级栏目                    |
| is_show        | int(11)      |    | 1                   | 公开选项（1：显示；0：隐藏；）        |
| level          | varchar(10)  |    |                     | 栏目级别                    |
| sort           | int(11)      |    | 50                  | 排序                      |
| is_input       | int(11)      |    | 0                   | 是否允许投稿                  |
| create_by      | varchar(32)  |    |                     | 创建人                     |
| create_time    | timestamp    |    | 2018-01-01 00:00:00 | 创建时间                    |
| update_by      | varchar(32)  |    |                     | 修改人                     |
| update_time    | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间                    |
| ext01          | varchar(32)  |    |                     | 扩展字段1                   |
| ext02          | varchar(32)  |    |                     | 扩展字段2                   |
| ext03          | varchar(32)  |    |                     | 扩展字段3                   |
| ext04          | varchar(32)  |    |                     | 扩展字段4                   |
| ext05          | varchar(32)  |    |                     | 扩展字段5                   |

### 10. system_archives（栏目文章表）

| 字段名          | 类型            | 键  | 默认值                 | 字段说明             |
|--------------|---------------|----|---------------------|------------------|
| id           | varchar(32)   | 主键 |                     | 主键               |
| title        | varchar(128)  |    |                     | 文章标题             |
| properties   | varchar(32)   |    | n                   | 自定义属性            |
| image_path   | varchar(128)  |    |                     | 缩略图              |
| tag          | varchar(128)  |    |                     | 标签               |
| description  | varchar(256)  |    |                     | 内容摘要             |
| category_id  | varchar(32)   |    |                     | 上级栏目             |
| comment      | int(11)       |    | 1                   | 允许评论             |
| subscribe    | int(11)       |    | 1                   | 允许订阅             |
| clicks       | int(11)       |    | 0                   | 浏览量              |
| weight       | int(11)       |    | 0                   | 权重               |
| status       | int(11)       |    | 1                   | 状态（1：已发布；0：未发布；） |
| category_ids | varchar(1024) |    |                     | 栏目层级             |
| create_by    | varchar(32)   |    |                     | 创建人              |
| create_time  | timestamp     |    | 2018-01-01 00:00:00 | 创建时间             |
| update_by    | varchar(32)   |    |                     | 修改人              |
| update_time  | timestamp     |    | CURRENT_TIMESTAMP   | 修改时间             |

### 11. system_labels（文章标签表）

| 字段名         | 类型           | 键  | 默认值                 | 字段说明   |
|-------------|--------------|----|---------------------|--------|
| id          | varchar(32)  | 主键 |                     | 主键     |
| tag_name    | varchar(128) |    |                     | 标签名称   |
| pinyin      | varchar(256) |    |                     | 拼音     |
| first_char  | varchar(2)   |    |                     | 首字母    |
| tag_count   | varchar(64)  |    |                     | 标签使用次数 |
| create_by   | varchar(32)  |    |                     | 创建人    |
| create_time | timestamp    |    | 2018-01-01 00:00:00 | 创建时间   |
| update_by   | varchar(32)  |    |                     | 修改人    |
| update_time | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间   |

### 12. system_attachment（附件表）

| 字段名         | 类型           | 键  | 默认值                 | 字段说明 |
|-------------|--------------|----|---------------------|------|
| id          | varchar(32)  | 主键 |                     | 主键   |
| code        | varchar(32)  |    |                     | 标识码  |
| filename    | varchar(64)  |    |                     | 文件名  |
| filesize    | int(11)      |    |                     | 文件大小 |
| filetype    | varchar(128) |    |                     | 文件类型 |
| filepath    | varchar(256) |    |                     | 文件路径 |
| create_by   | varchar(32)  |    |                     | 创建人  |
| create_time | timestamp    |    | 2018-01-01 00:00:00 | 创建时间 |
| update_by   | varchar(32)  |    |                     | 修改人  |
| update_time | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间 |

### 13. system_forms（表单模型表）

| 字段名         | 类型           | 键  | 默认值                 | 字段说明                |
|-------------|--------------|----|---------------------|---------------------|
| id          | varchar(32)  | 主键 |                     | 主键                  |
| form_name   | varchar(32)  |    |                     | 表单名                 |
| table_name  | varchar(128) |    |                     | 表名                  |
| code        | varchar(32)  |    |                     | 编码系统自动生成            |
| type        | int(11)      |    | 1                   | 类型（0：系统模型；1：自定义模型；） |
| create_by   | varchar(32)  |    |                     | 创建人                 |
| create_time | timestamp    |    | 2018-01-01 00:00:00 | 创建时间                |
| update_by   | varchar(32)  |    |                     | 修改人                 |
| update_time | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间                |
| ext01       | varchar(32)  |    |                     | 扩展字段1               |
| ext02       | varchar(32)  |    |                     | 扩展字段2               |
| ext03       | varchar(32)  |    |                     | 扩展字段3               |
| ext04       | varchar(32)  |    |                     | 扩展字段4               |
| ext05       | varchar(32)  |    |                     | 扩展字段5               |

### 14. system_fields（表单字段表）

| 字段名           | 类型           | 键  | 默认值                 | 字段说明     |
|---------------|--------------|----|---------------------|----------|
| id            | varchar(32)  | 主键 |                     | 主键       |
| form_id       | varchar(32)  |    |                     |          |
| field_text    | varchar(32)  |    |                     | 字段提示名    |
| field_name    | varchar(128) |    |                     | 字段名      |
| type          | int(11)      |    | 1                   | 类型（暂无使用） |
| data_type     | varchar(32)  |    |                     | 数据类型     |
| default_value | varchar(128) |    |                     | 默认值      |
| max_length    | int(11)      |    |                     | 最大长度     |
| create_by     | varchar(32)  |    |                     | 创建人      |
| create_time   | timestamp    |    | 2018-01-01 00:00:00 | 创建时间     |
| update_by     | varchar(32)  |    |                     | 修改人      |
| update_time   | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间     |
| ext01         | varchar(32)  |    |                     | 扩展字段1    |
| ext02         | varchar(32)  |    |                     | 扩展字段2    |
| ext03         | varchar(32)  |    |                     | 扩展字段3    |
| ext04         | varchar(32)  |    |                     | 扩展字段4    |
| ext05         | varchar(32)  |    |                     | 扩展字段5    |

### 15. system_theme（网站主题表）

| 字段名          | 类型           | 键  | 默认值                 | 字段说明          |
|--------------|--------------|----|---------------------|---------------|
| id           | varchar(32)  | 主键 |                     | 主键            |
| theme_name   | varchar(128) |    |                     | 风格名称          |
| theme_author | varchar(32)  |    |                     | 作者            |
| theme_img    | varchar(128) |    |                     | 缩略图           |
| theme_path   | varchar(128) |    |                     | 目录名           |
| status       | int(11)      |    | 0                   | 状态（1:启用；0:禁用） |
| create_by    | varchar(32)  |    |                     | 创建人           |
| create_time  | timestamp    |    | 2018-01-01 00:00:00 | 创建时间          |
| update_by    | varchar(32)  |    |                     | 修改人           |
| update_time  | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间          |

### 16. system_theme（网站主题表）

| 字段名         | 类型            | 键  | 默认值                 | 字段说明 |
|-------------|---------------|----|---------------------|------|
| id          | varchar(32)   | 主键 |                     | 主键   |
| item_name   | varchar(255)} |    |                     | 变量名称 |
| info        | varchar(255)} |    |                     | 描述   |
| type        | varchar(255)} |    |                     | 字段类型 |
| value       | varchar(255)} |    |                     | 变量值  |
| create_by   | varchar(32)   |    |                     | 创建人  |
| create_time | timestamp     |    | 2018-01-01 00:00:00 | 创建时间 |
| update_by   | varchar(32)   |    |                     | 修改人  |
| update_time | timestamp     |    | CURRENT_TIMESTAMP   | 修改时间 |

### 17. system_logger（系统日志表）

| 字段名         | 类型           | 键  | 默认值                 | 字段说明  |
|-------------|--------------|----|---------------------|-------|
| id          | varchar(32)  | 主键 |                     | 主键    |
| level       | varchar(32)  |    |                     | 级别    |
| oper_user   | varchar(64)  |    |                     | 操作用户  |
| oper_type   | varchar(32)  |    |                     | 操作类型  |
| oper_source | varchar(128) |    |                     | 操作源   |
| ip          | varchar(64)  |    |                     | IP    |
| module      | varchar(64)  |    |                     | 模块    |
| browser     | varchar(128) |    |                     | 浏览器   |
| platform    | varchar(128) |    |                     | 平台    |
| content     | text         |    |                     | 内容    |
| create_by   | varchar(32)  |    |                     | 创建人   |
| create_time | timestamp    |    | 2018-01-01 00:00:00 | 创建时间  |
| update_by   | varchar(32)  |    |                     | 修改人   |
| update_time | timestamp    |    | CURRENT_TIMESTAMP   | 修改时间  |
| extend1     | varchar(256) |    |                     | 扩展字段1 |
| extend2     | varchar(256) |    |                     | 扩展字段2 |
| extend3     | varchar(256) |    |                     | 扩展字段3 |

### 18. system_search（搜索记录表）

| 字段名         | 类型            | 键  | 默认值               | 字段说明 |
|-------------|---------------|----|-------------------|------|
| id          | varchar(32)   | 主键 |                   | 主键   |
| keywords    | varchar(128)} |    |                   | 关键字  |
| create_time | timestamp     |    | CURRENT_TIMESTAMP | 创建时间 |

### 19. 其它表

> 剩余其它表，均为表单模型创建生成，可以后台表单模型模块中进行查看。
