package cn.itechyou.cms.controller.admin;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.itechyou.cms.common.CmsStatus;
import cn.itechyou.cms.common.Json;
import cn.itechyou.cms.common.Result;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.UserService;
import cn.itechyou.cms.vo.UserPasswordVO;

/**
 * 用户管理
 * 
 * @author 王俊南
 * @date 2018-07-30
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    //    private final Logger logger = LoggerUtils.getPlatformLogger();

    @Resource
    private UserService userService;

    @RequestMapping("toUpdatePwd")
    public String toUpdatePwd(ModelAndView mv) {
        mv.addObject("user", TokenManager.getToken());
        return "admin/user/password";
    }

    @ResponseBody
    @RequestMapping("/updatePwd")
    public Result updatePwd(@RequestBody UserPasswordVO user) {

        User user2 = userService.getByID(user.getId());
        ByteSource oldSalt = ByteSource.Util.bytes(user2.getUsername() + user.getOldPwd());
        SimpleHash sh = new SimpleHash("MD5", user.getOldPwd(), oldSalt, 1024);

        if (!user2.getPassword().equals(sh.toString())) {
            return Json.failed(CmsStatus.USER_OLDPWD_ERROR);
        }

        ByteSource newSalt = ByteSource.Util.bytes(user2.getUsername() + user.getNewPwd());
        SimpleHash sh1 = new SimpleHash("MD5", user.getNewPwd(), newSalt, 1024);
        user2.setSalt(newSalt.toString());
        user2.setPassword(sh1.toString());
        userService.save(user2);

        return Json.ok();
    }

}
