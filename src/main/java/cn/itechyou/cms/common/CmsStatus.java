/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.common;

/**
 * 状态返回码数据字典，code状态返回码，description返回码描述 code状态码为4位数字，前两位为状态大类，后两位为对应具体返回状态码
 */
public enum CmsStatus {

    //HTTP返回值
    HTTP_SUCCESS(200, "操作成功"),
    HTTP_ERROR(500, "操作失败"),
    HTTP_EXCEPTION(400, "服务器错误"),
    HTTP_NOTUPDATE(304, "资源未修改"),
    HTTP_NOTNOTAUTHORIZE(401, "请求未授权"),
    HTTP_FORBIDDEN(403, "禁止访问"),
    HTTP_NOTFOUND(404, "资源不存在"),

    //通用返回值Code 10
    RESULTFALSE(1000, "false"),
    RESULTTRUE(1001, "true"),
    ARGUMENTERROR(1002, "缺少参数或参数值不符合要求"),
    SYS_VERCODEERROR(1003, "验证码错误"),
    TOKENINVALID(1004, "缺少签名或签名无效"),
    ENCRYPTERROR(1005, "加密验证错误"),
    UPLOADERROR(1006, "上传文件失败"),
    NOT_PAR(1007, "缺少参数"),
    UPLOAD_FAILURE(1008, "图片上传失败"),
    RESULT_EMPTY(1009, "查询结果集为空"),
    CHECKCODE_ERROR(1010, "校验码验证失败"),
    CHECKTOKEN_ERROR(1011, "校验码验证失败"),
    AUTHEN_ERROR(1012, "用户认证失败"),
    SYS_ERROR(1013, "系统认证失败"),
    INTERFACE_ERROR(1014, "第三方接口异常"),

    //用户信息Code 20
    USER_LOGIN_SUCCESS(2001, "登录成功"),
    USER_USER_REGISTERED(2002, "用户已注册"),
    USER_USER_INEXISTENCE(2003, "用户不存在"),
    USER_CODE_ERROR(2005, "验证码输入有误"),
    USER_PASSWORD_ERROR(2004, "用户名或密码错误"),
    USER_MOBILE_EMPTY(2006, "您输入的账号不存在"),
    USER_MOBILE_EXCEPTION(2007, "此用户状态异常,请联系管理员"),
    USER_LOGIN_TYPE_ERROR(2008, "登录类型出错"),
    USER_LOGIN_KICKOUT(2009, "已经在其他地方登录，请重新登录"),
    USER_INFO_NOT_FOUND(2010, "未查找到当前用户信息"),
    USER_USER_NOLOGIN(2011, "用户未登录"),
    USER_USER_NOPERMISSION(2012, "用户未授权"),
    USER_VCODE_ERROR(2013, "服务器验证码出现异常"),
    USER_OLDPWD_ERROR(2014, "原密码输入错误"),

    //用户信息Code 30
    APP_TYPE_ERROR(3001, "错误:没有版本发布"),

    //资源信息Code 40
    RESOURCE_DELETE_FAILD(4001, "资源删除失败：有引用"),
    RESOURCE_ALREADY_EXISTED(4002, "资源已存在");

    private final int code;
    private final String description;

    private CmsStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
