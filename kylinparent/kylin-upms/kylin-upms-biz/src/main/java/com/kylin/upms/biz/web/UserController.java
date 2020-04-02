package com.kylin.upms.biz.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.kylin.upms.biz.annotation.log;

import com.kylin.upms.biz.dto.UserDto;
import com.kylin.upms.biz.entity.Log;
import com.kylin.upms.biz.entity.User;
import com.kylin.upms.biz.entity.UserAndRole;
import com.kylin.upms.biz.entity.UserSecurity;
import com.kylin.upms.biz.enums.OperationType;
import com.kylin.upms.biz.enums.OperationUnit;
import com.kylin.upms.biz.service.EmailService;
import com.kylin.upms.biz.service.IUserService;
import com.kylin.upms.biz.service.impl.LogsServiceImpl;
import com.kylin.upms.biz.vo.ResEntity;
import io.minio.MinioClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

import java.util.Map;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
@Api("操作用户")
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    IUserService iUserService;
    @Autowired
    IUserService userService;
    @Autowired
    MinioClient minioClient;


    @Autowired
    LogsServiceImpl logsService;

    @Autowired
    EmailService emailService;


    @ApiOperation("根据查询条件分页查询用户列表")
    @RequestMapping(method = RequestMethod.GET,value = "/page")
    public ResEntity get(UserDto u){
        Page<User> page = new Page<User>(u.getPageNum(),6);
        User user  = new User();
        BeanUtils.copyProperties(u,user);
        EntityWrapper entityWrapper = new EntityWrapper(user);
        entityWrapper.like("username",user.getUsername());
        Page page1 = iUserService.selectPage(page, entityWrapper);
        return ResEntity.ok(page1);
    }

    //新增
    @RequestMapping(method = RequestMethod.POST)
    public ResEntity add(@RequestBody UserDto userDto){
         userService.addUser(userDto);
        return ResEntity.ok("新增成功");

    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResEntity update(@RequestBody UserDto userDto ){
        userService.updateUserWithRole(userDto);
        return ResEntity.ok("更新成功");

    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResEntity del(UserDto userDto){

        return null;

    }

    @RequestMapping(method = RequestMethod.GET,value = "/getById")
    public ResEntity get(Integer id){
        return null;

    }
    //用户和角色
    @ApiOperation("查询用户和角色列表")
    @log(detail = "请求的方法为getUserRole",operationUnit = OperationUnit.ADMIN,operationType = OperationType.SELECT)
    @RequestMapping(method = RequestMethod.GET,value = "/userandrole")
    public ResEntity getUserRole(UserDto userDto){
        Page<UserAndRole> page = new Page<UserAndRole>(userDto.getPageNum(),userDto.getPageSize());
        return ResEntity.ok(userService.getUserRole(page,userDto.getUsername()));
    }

    //文件上传
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public Object uploadFile(MultipartFile file) throws Exception{
        Map<String,Object> map=new HashMap<>();
        map.put("res",true);
        minioClient.putObject("kylin",file.getOriginalFilename(),
                file.getInputStream(),file.getContentType());
        String kylin=minioClient.getObjectUrl("kylin",file.getOriginalFilename());
        map.put("url",kylin);
        map.put("fileName",kylin);
        return map;
    }

    //es   高亮
    @RequestMapping(method = RequestMethod.GET,value = "/page1")
    @CrossOrigin(origins = {"http://localhost:8080"})
    public ResEntity getes(UserDto userDto){
        AggregatedPage<UserAndRole> selectuser = iUserService.selectuser(userDto.getPageNum(),userDto.getPageSize(),userDto.getUsername());
        return ResEntity.ok("查询成功",selectuser);
    }


    //log 日志
    @RequestMapping(method = RequestMethod.GET,value = "/log")
    public ResEntity getlog( Integer page, Integer size){

        PageRequest of = PageRequest.of(page, size);
        org.springframework.data.domain.Page<Log> finall = logsService.finall(of);

        return ResEntity.ok(finall);
    }


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("/send")
    public ResEntity send(){
        //发送消息到test topic
        kafkaTemplate.send("test01", "test01");

        return ResEntity.ok("我已经接收到了");
    }


   //用户重置密码

    @RequestMapping(method = RequestMethod.GET,value = "/resend")
    public ResEntity PasswordResend( Integer uid){
        userService.PasswordResend(uid);
        return ResEntity.ok();
    }


  //发送email
  @log(detail = "mail",operationUnit = OperationUnit.ADMIN,operationType = OperationType.SELECT)
  @ApiOperation("邮箱发送")
  @RequestMapping(value = "/mail",method = RequestMethod.GET)
  public ResEntity mailtest(){
      emailService.sendMsg();
      return ResEntity.ok("发送成功");
  }


//用户修改密码
@RequestMapping(method = RequestMethod.GET,value = "/editPassword")
public ResEntity editPassword(String password){
    UserSecurity user = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    userService.editPassword(user.getUsername(),password);
    return ResEntity.ok();
}


}
