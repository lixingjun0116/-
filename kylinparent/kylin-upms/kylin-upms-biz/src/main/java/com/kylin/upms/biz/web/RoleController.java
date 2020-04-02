package com.kylin.upms.biz.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.dto.UserDto;
import com.kylin.upms.biz.entity.Role;
import com.kylin.upms.biz.entity.User;
import com.kylin.upms.biz.service.IMenuService;
import com.kylin.upms.biz.service.IRoleService;
import com.kylin.upms.biz.vo.ResEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    IRoleService roleService;
    @RequestMapping(method = RequestMethod.GET)
    public ResEntity rolePage(){
        return ResEntity.ok();
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResEntity get(){
        List<Role> roleAll = roleService.getRoleAll();
        //System.out.println(roleAll);
        return ResEntity.ok(roleAll);
    }

    //修改
    @RequestMapping(method = RequestMethod.PUT)
    public ResEntity update(@RequestBody RoleDto roleDto ){
        roleService.updateRoleandMenu(roleDto);
        return ResEntity.ok("更新成功");

    }




}
