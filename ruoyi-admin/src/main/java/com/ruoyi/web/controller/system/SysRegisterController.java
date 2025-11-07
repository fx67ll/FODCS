package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 超神用户专属注册接口
     */
    @PostMapping("/registerChaoshen")
    public AjaxResult registerChaoshen(@RequestBody RegisterBody user) {
        // 校验超神用户注册开关是否开启
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUserChaoshen")))) {
            return error("当前系统没有开启超神用户注册功能！");
        }
        String msg = registerService.registerChaoshenUser(user);
        return StringUtils.isEmpty(msg) ? success("超神用户注册成功") : error(msg);
    }
}
