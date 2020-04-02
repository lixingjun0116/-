package com.kylin.upms.biz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.kylin.upms.biz.annotation.log;
import com.kylin.upms.biz.dto.UserDto;
import com.kylin.upms.biz.entity.*;
import com.kylin.upms.biz.enums.OperationType;
import com.kylin.upms.biz.enums.OperationUnit;
import com.kylin.upms.biz.mapper.UserMapper;
import com.kylin.upms.biz.service.EmailService;
import com.kylin.upms.biz.service.IRoleService;
import com.kylin.upms.biz.service.IUserRoleService;
import com.kylin.upms.biz.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.util.Password;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    IRoleService roleService;

    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    EmailService emailService;
    //登录
    @Override
    @log(detail = "请求的参数{{{s}}}",level = 1,operationUnit = OperationUnit.USER,operationType = OperationType.SELECT)
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("用户名为:{}",s);
        EntityWrapper wrapper = new EntityWrapper(new User());
        wrapper.eq("username",s);
        User user = this.selectOne(wrapper);
        //判断状态是否等于0并且redis三天时间已经到期
        String redis = redisTemplate.opsForValue().get("userid_"+user.getId());
        String remark = user.getRemark();

        if(redis==null && remark.equals("0")){
            throw  new UsernameNotFoundException("此用户重置密码后没有及时更改 请申请再次重置！");
        }


        if (user==null){
            throw  new UsernameNotFoundException("用户不存在");
        }

        if (redisTemplate.hasKey(s+"Three")){
            System.out.println("此用户被锁定");
            throw new LockedException("此用户已被锁定");
        }


        List<Role> roleByUserName = roleService.getRoleByUserName(s);
        String[] roles = new String[roleByUserName.size()];
        int i = 0;
        for (Role role:roleByUserName){
            roles[i] = role.getName();
            i++;
        }

        UserSecurity userSecurity = new UserSecurity(s,user.getPassword(), AuthorityUtils.createAuthorityList(roles));
        return userSecurity;
    }

    @Override
    @log(detail = "请求的方法为getUserRole",operationUnit = OperationUnit.ADMIN,operationType = OperationType.SELECT)
    public Page<UserAndRole> getUserRole(Page<UserAndRole> page,String username) {

        Page<UserAndRole> userAndRolePage = page.setRecords(this.baseMapper.getUserRole(page, username));
        return userAndRolePage;
    }

    @Override
    @Transactional
    @log(detail = "请求的方法为updateUserWithRole",operationUnit = OperationUnit.USER,operationType = OperationType.UPDATE)
    public boolean updateUserWithRole(UserDto userDto) {

        User user = new User();
        BeanUtils.copyProperties(userDto,user);

        this.updateById(user);

        Integer[] checks = userDto.getChecks();

        EntityWrapper<UserRole> userRoleEntityWrapper = new EntityWrapper<>();

        userRoleEntityWrapper.eq("uid",userDto.getId());
        userRoleService.delete(userRoleEntityWrapper);

        for (Integer check : checks) {
            UserRole userRole = new UserRole();
            userRole.setRid(check);
            userRole.setUid(userDto.getId());
            userRoleService.insert(userRole);
        }


        return true;
    }

    @Override
    @Transactional
    @log(detail = "请求的方法为addUser",operationUnit = OperationUnit.ADMIN,operationType = OperationType.INSERT)
    public boolean addUser(UserDto userDto) {
        //创建新对象  准备接受前台新增的值
        User user=new User();
        //前台传来的对象拷贝到新对象中
        BeanUtils.copyProperties(userDto,user);
        //新增用户
        String newpwd = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(newpwd);
        this.insert(user);
        //查出刚刚新增进去那个用户名名称  username
        EntityWrapper wrapper=new EntityWrapper();
        //封装条件
        wrapper.eq("username",user.getUsername());
        //查询对象
        User one = this.selectOne(wrapper);



        Integer[] checks = userDto.getChecks();

        for (Integer check : checks) {
            UserRole userRole=new UserRole();
            userRole.setUid(one.getId());
            userRole.setRid(check);
            userRoleService.insert(userRole);
        }
        return true;
    }

    //            es
    @Override
    @log(detail = "请求的方法为selectuser",operationUnit = OperationUnit.ADMIN,operationType = OperationType.SELECT)
    public AggregatedPage<UserAndRole> selectuser(int pageNum, int pageSize,  String username) {
        // 创建Pageable对象
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        // 高亮拼接的前缀与后缀
        String preTags = "<font color=\"red\">";
        String postTags = "</font>";

        //创建查询
        if(username==null || "".equals(username)){
            // 创建SearchQuery对象
            SearchQuery query = new NativeSearchQueryBuilder().withPageable(pageable).build();
            // 执行分页查询
            AggregatedPage<UserAndRole> pageInfo = elasticsearchTemplate.queryForPage((SearchQuery) query, UserAndRole.class);
            return  pageInfo;
        }

        String fieldNames[] = { "name", "username","phone","userface","telephone","address"};

        // 创建queryBuilder对象
        QueryBuilder queryBuilder =QueryBuilders.multiMatchQuery(username, fieldNames);

        // 创建SearchQuery对象
        SearchQuery query = new NativeSearchQueryBuilder().
                withQuery(queryBuilder).withHighlightFields(
                        new HighlightBuilder.Field(fieldNames[0]).preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field(fieldNames[1]).preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field(fieldNames[2]).preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field(fieldNames[3]).preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field(fieldNames[4]).preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field(fieldNames[5]).preTags(preTags).postTags(postTags)
        ).withPageable(pageable).build();
        //执行分页查询
        AggregatedPage<UserAndRole> pageInfo=elasticsearchTemplate.queryForPage(query, UserAndRole.class,
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass,Pageable pageable) {
                        //定义查询出内容存储的集合
                        List<UserAndRole> content = new ArrayList<>();
                        // 获取高亮的结果
                        SearchHits searchHits = response.getHits();
                        // 判断高亮结果
                        if(searchHits!=null){
                            // 获取高亮中所有内容
                            SearchHit[] hits = searchHits.getHits();
                            //判断结果是否为空
                            if(hits.length>0){
                                for (SearchHit hit : hits) {
                                    UserAndRole userAndRole=new UserAndRole();
                                    // 高亮结果的主键id值
                                    String id = hit.getId();
                                    //主键id
                                    userAndRole.setId(Integer.parseInt(id));
                                    // 获取第一个字段的高亮内容
                                    HighlightField highlightField = hit.getHighlightFields().get(fieldNames[0]);
                                    //判断获取内容是否为空
                                    if(highlightField!=null){
                                        userAndRole.setName(highlightField.getFragments()[0].toString());
                                    }else {
                                        userAndRole.setName((String) hit.getSourceAsMap().get(fieldNames[0]));
                                    }

                                    // 获取第二个字段的高亮内容
                                    HighlightField highlightField1 = hit.getHighlightFields().get(fieldNames[1]);
                                    if(highlightField1!=null){
                                        userAndRole.setUsername(highlightField1.getFragments()[0].toString());
                                    }else {
                                        userAndRole.setUsername((String) hit.getSourceAsMap().get(fieldNames[1]));
                                    }
                                    userAndRole.setPhone((String) hit.getSourceAsMap().get(fieldNames[2]));
                                    userAndRole.setUserface((String) hit.getSourceAsMap().get(fieldNames[3]));
                                    userAndRole.setTelephone((String) hit.getSourceAsMap().get(fieldNames[4]));
                                    userAndRole.setAddress((String) hit.getSourceAsMap().get(fieldNames[5]));
                                    content.add(userAndRole);
                                }
                            }
                        }
                        return new AggregatedPageImpl<>((List<T>)content);
                    }
                });
        return pageInfo;
    }

    //重置用户密码
    @Override
    public boolean PasswordResend(Integer uid) {
        //设置默认密码
        String password = new BCryptPasswordEncoder().encode("123456");
        //根据userid  修改用户的密码
        this.baseMapper.PasswordResend(password,uid);
        //redis 设置时间
        redisTemplate.opsForValue().set("userid_"+uid,uid+"",3, TimeUnit.DAYS);
        //发送email
        emailService.sendMsg();
        return true;
    }

    //用户修改密码
    @Override
    public boolean editPassword(String username,String password) {
        EntityWrapper wrapper = new EntityWrapper(new User());
        wrapper.eq("username",username);
        User user = this.selectOne(wrapper);
        String editpass = new BCryptPasswordEncoder().encode(password);
        //修改密码
        this.baseMapper.editPassword(user.getId(),editpass);
        redisTemplate.delete("userid_"+user.getId());
        return true;
    }
}
