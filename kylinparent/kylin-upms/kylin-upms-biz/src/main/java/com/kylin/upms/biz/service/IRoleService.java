package com.kylin.upms.biz.service;

import com.kylin.upms.biz.dto.RoleDto;
import com.kylin.upms.biz.entity.Role;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
public interface IRoleService extends IService<Role> {
    List<Role> getRoleByUserName(String userName);

    //查询所有角色
    List<Role> getRoleAll();

    boolean updateRoleandMenu(RoleDto roleDto);
}
