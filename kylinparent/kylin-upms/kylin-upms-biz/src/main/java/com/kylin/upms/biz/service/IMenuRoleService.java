package com.kylin.upms.biz.service;

import com.kylin.upms.biz.dto.MenuDto;
import com.kylin.upms.biz.entity.MenuRole;
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
public interface IMenuRoleService extends IService<MenuRole> {


    List<MenuRole> midsbyrid(Integer rid);
}
