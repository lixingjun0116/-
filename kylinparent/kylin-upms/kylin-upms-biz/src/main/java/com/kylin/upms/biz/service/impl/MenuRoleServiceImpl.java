package com.kylin.upms.biz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kylin.upms.biz.entity.MenuRole;
import com.kylin.upms.biz.mapper.MenuRoleMapper;
import com.kylin.upms.biz.service.IMenuRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Override
    public List<MenuRole> midsbyrid(Integer rid) {
        EntityWrapper<MenuRole> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("rid",rid);
        List<MenuRole> menuRoles = this.baseMapper.selectList(entityWrapper);
        return menuRoles;
    }
}
