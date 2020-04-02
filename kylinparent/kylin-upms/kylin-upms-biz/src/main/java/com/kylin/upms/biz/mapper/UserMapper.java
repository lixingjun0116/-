package com.kylin.upms.biz.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.kylin.upms.biz.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.kylin.upms.biz.entity.UserAndRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
public interface UserMapper extends BaseMapper<User> {

    List<UserAndRole> getUserRole(Pagination page,@Param("username") String username);


    boolean PasswordResend(String password,Integer uid);

    boolean editPassword(Integer uid,String password);
}
