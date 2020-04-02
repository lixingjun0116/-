package com.kylin.upms.biz.service;

import com.kylin.upms.biz.dto.MenuDto;
import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.entity.Menu;
import com.baomidou.mybatisplus.service.IService;
import com.kylin.upms.biz.entity.MenuRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenuAll();
    List<Menu> getMenuBuUserID(String username);

    List<Menu> getMenu();



}
