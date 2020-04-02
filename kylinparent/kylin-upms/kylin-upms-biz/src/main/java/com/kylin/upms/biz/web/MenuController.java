package com.kylin.upms.biz.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.kylin.upms.biz.dto.MenuDto;
import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.dto.UserDto;
import com.kylin.upms.biz.entity.Menu;
import com.kylin.upms.biz.entity.MenuRole;
import com.kylin.upms.biz.entity.UserSecurity;
import com.kylin.upms.biz.service.IMenuRoleService;
import com.kylin.upms.biz.service.IMenuService;
import com.kylin.upms.biz.service.IRoleService;
import com.kylin.upms.biz.service.IUserService;
import com.kylin.upms.biz.vo.ResEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IMenuService iMenuService;
    @Autowired
    IMenuRoleService menuRoleService;

    @RequestMapping("/getMenuByUserID")
   public ResEntity getMenuByUserID(){
        logger.info(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        UserSecurity user = (UserSecurity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menus = iMenuService.getMenuBuUserID(user.getUsername());
       return ResEntity.ok(menus);
   }

    //查询所有菜单  错误
    @RequestMapping(value = "/menuAll",method = RequestMethod.GET)
    public ResEntity getMenuAll(Menu menu){
        List<Menu> menuAll = iMenuService.getMenuAll();
        return ResEntity.ok(menuAll);
    }

    //查询所有菜单
    @RequestMapping(value = "/menu",method = RequestMethod.GET)
    public ResEntity getMenu(){
        List<Menu> menuAll = iMenuService.getMenu();
        return ResEntity.ok(menuAll);
    }

    //根据角色id查询所拥有的菜单id
    @RequestMapping(value = "/midsbyrid",method = RequestMethod.GET)
    public ResEntity getMenu(Integer rid){
        List<MenuRole> midsbyrid = menuRoleService.midsbyrid(rid);
        return ResEntity.ok(midsbyrid);
    }


}
