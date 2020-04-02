package com.kylin.upms.biz.mapper;

import com.kylin.upms.biz.entity.Menu;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuAll();

    List<Menu> getMenu();
    List<Menu> getMenuBuUserID(String username);


}
