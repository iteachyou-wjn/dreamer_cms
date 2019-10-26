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
package cn.itechyou.cms.controller.admin;

import static cn.itechyou.cms.common.CmsStatus.USER_MOBILE_EXCEPTION;
import static cn.itechyou.cms.common.CmsStatus.USER_PASSWORD_ERROR;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Producer;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.Json;
import cn.itechyou.cms.common.Result;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.utils.LoggerUtils;
import cn.itechyou.cms.vo.UserVO;

/**
 * 用户登录相关，不需要做登录限制
 * 
 */
@Controller
@RequestMapping("/admin/u")
public class UserLoginController extends BaseController {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerUtils.getLogger(UserLoginController.class);

    public static String URL404 = "/404.html";

    @Autowired
    private Producer producer;

    // 产生验证码
    @RequestMapping("/getVerifyCode")
    public void getKaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        // 得到文本内容
        String text = producer.createText();
        logger.info("生成的验证码为：" + text);
        session.setAttribute(Constant.KAPTCHA, text);
        // 形成一张图片
        BufferedImage image = producer.createImage(text);
        // 把图片写入到输出流中==》以流的方式响应到客户端
        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        outputStream.close();
    }

    /**
     * 登录跳转
     * 
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin() {
        SecurityUtils.getSubject().getPrincipal();
        return "admin/login";
    }

    /**
     * 首页跳转
     * 
     * @return
     */
    @RequestMapping("toIndex")
    public String toIndex() {
        return "admin/index";
    }

    /**
     * 登录提交
     * 
     * @param entity
     *            登录的UUser
     * @param rememberMe
     *            是否记住
     * @param request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody UserVO entity) {

        final User user = new User();

        try {

            ByteSource salt = ByteSource.Util.bytes(entity.getUsername() + entity.getPassword());

            SimpleHash sh = new SimpleHash("MD5", entity.getPassword(), salt, 1024);

            user.setUsername(entity.getUsername());
            user.setPassword(sh.toString());
            user.setSaltByte(salt);
            TokenManager.login(user, entity.isRememberMe(), salt);
            return Json.ok();
        }
        catch (DisabledAccountException e) {
            // 帐号已经禁用
            return Json.failed(USER_MOBILE_EXCEPTION);
        }
        catch (Exception e) {
            // 帐号或密码错误
            return Json.failed(USER_PASSWORD_ERROR);
        }
    }

    /**
     * 退出
     * 
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        try {
            TokenManager.logout();
        }
        catch (Exception e) {
            logger.error("errorMessage:" + e.getMessage());
        }
        return "redirect:/admin/toLogin";
    }
}
