package com.kylin.upms.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.kylin.upms.biz.dto.MenuDto;
import com.kylin.upms.biz.dto.UserDto;
import com.kylin.upms.biz.entity.User;
import com.baomidou.mybatisplus.service.IService;
import com.kylin.upms.biz.entity.UserAndRole;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.awt.print.Pageable;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
public interface IUserService extends IService<User> {


    Page<UserAndRole> getUserRole(Page<UserAndRole> page, String username);


    boolean updateUserWithRole(UserDto userDto);

    boolean addUser(UserDto userDto);

    AggregatedPage<UserAndRole> selectuser(int pageNum, int pageSize, String username);

    //用户重置密码
    boolean PasswordResend(Integer uid);

    //用户修改密码
    boolean editPassword(String username,String password);
}
