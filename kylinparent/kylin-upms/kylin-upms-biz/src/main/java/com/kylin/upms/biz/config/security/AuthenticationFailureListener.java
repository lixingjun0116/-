package com.kylin.upms.biz.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        //从坏的凭证中得到身份验证的主要
        String count = event.getAuthentication().getPrincipal().toString();

        //第一次失败  失败内容
        redisTemplate.opsForValue().increment(count+"_",1);
        //获取到失败的次数
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
        Integer i = (Integer)redisTemplate.opsForValue().get(count + "_");

        if(i>=3 && i<=5 && redisTemplate.hasKey(count+"_")){
            redisTemplate.opsForValue().set(count+"Three",0,60, TimeUnit.SECONDS);//提示锁定两分钟
            redisTemplate.opsForValue().set(count+"_",i,60,TimeUnit.SECONDS);
        }else if(i>5 && redisTemplate.hasKey(count+"_")){
            redisTemplate.opsForValue().set(count+"Three",0,120, TimeUnit.SECONDS);//提示锁定两分钟
            redisTemplate.opsForValue().set(count+"_",i,120,TimeUnit.SECONDS);
        }
    }
}
