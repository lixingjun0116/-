package com.kylin.upms.biz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kylin.upms.biz.dto.MenuDto;
import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.entity.Menu;
import com.kylin.upms.biz.entity.MenuRole;
import com.kylin.upms.biz.entity.Role;
import com.kylin.upms.biz.mapper.MenuMapper;
import com.kylin.upms.biz.service.IMenuRoleService;
import com.kylin.upms.biz.service.IMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    IMenuRoleService menuRoleService;

    @Override
    public List<Menu> getMenuAll() {
        return this.baseMapper.getMenuAll();

    }

    @Override
    public List<Menu> getMenuBuUserID(String username) {
        return this.baseMapper.getMenuBuUserID(username);
    }

    @Override
    public List<Menu> getMenu() {
        return this.baseMapper.getMenu();
    }




}
