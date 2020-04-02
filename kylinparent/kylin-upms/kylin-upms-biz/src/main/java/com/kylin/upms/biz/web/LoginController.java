package com.kylin.upms.biz.web;


import com.kylin.upms.biz.vo.ResEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {


    @RequestMapping("/api/login_p")
    public ResEntity loginP(){
        return ResEntity.error("尚未登录");
    }
}
