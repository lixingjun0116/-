package com.kylin.upms.biz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.entity.MenuRole;
import com.kylin.upms.biz.entity.Role;
import com.kylin.upms.biz.mapper.RoleMapper;
import com.kylin.upms.biz.service.IMenuRoleService;
import com.kylin.upms.biz.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    IMenuRoleService menuRoleService;

    @Override
    public List<Role> getRoleByUserName(String userName) {
        return this.baseMapper.getRoleByUserName(userName);
    }

    @Override
    public List<Role> getRoleAll() {
        return this.baseMapper.getRoleAll();
    }

    @Override
    @Transactional
    public boolean updateRoleandMenu(RoleDto roleDto) {
        Role role=new Role();
        BeanUtils.copyProperties(roleDto,role);
        //this.updateById(role);

        Integer[] mid = roleDto.getMid();

        EntityWrapper<MenuRole> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("rid",role.getId());
        menuRoleService.delete(entityWrapper);

        for (Integer i : mid) {
            MenuRole menuRole=new MenuRole();
            menuRole.setRid(role.getId());
            menuRole.setMid(i);
            menuRoleService.insert(menuRole);
        }
        return true;
    }
}
