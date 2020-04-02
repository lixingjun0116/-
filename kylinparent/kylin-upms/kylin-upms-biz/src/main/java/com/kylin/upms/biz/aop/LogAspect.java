package com.kylin.upms.biz.aop;

import com.alibaba.fastjson.JSON;
import com.kylin.upms.biz.annotation.log;
import com.kylin.upms.biz.enums.OperationLog;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //链接mongo服务器
    MongoClient mongoClient=new MongoClient("127.0.0.1");
    //连接到数据库
    MongoDatabase database = mongoClient.getDatabase("test");
    MongoCollection<Document> spit=database.getCollection("user");
    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@annotation(com.kylin.upms.biz.annotation.log)")
    public void operationLog(){}


    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            res =  joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        } finally {
            try {
                //方法执行完成后增加日志
                addOperationLog(joinPoint,res,time);
            }catch (Exception e){
                System.out.println("LogAspect 操作失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addOperationLog(JoinPoint joinPoint, Object res, long time){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        OperationLog operationLog = new OperationLog();
        operationLog.setRunTime(time);
        operationLog.setReturnValue(JSON.toJSONString(res));
        operationLog.setId(UUID.randomUUID().toString());
        //operationLog.setArgs(JSON.toJSONString(joinPoint.getArgs()));
        operationLog.setArgs(authentication.getName());
        operationLog.setCreateTime(new Date());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        operationLog.setUserId("#{currentUserId}");

        operationLog.setUserName(authentication.getName());
        log annotation = signature.getMethod().getAnnotation(log.class);
        if(annotation != null){
            operationLog.setLevel(annotation.level());
            operationLog.setDescribe(getDetail(((MethodSignature)joinPoint.getSignature()).getParameterNames(),joinPoint.getArgs(),annotation));
            operationLog.setOperationType(annotation.operationType().getValue());
            operationLog.setOperationUnit(annotation.operationUnit().getValue());
        }
        //TODO 这里保存日志
        Map<String,Object> map=new HashMap<>();

        map.put("_id",operationLog.getId().toString());
        kafkaTemplate.send("_id",String.valueOf(operationLog.getId().toString()));
        map.put("createTime",operationLog.getCreateTime().toString());
        kafkaTemplate.send("createTime",String.valueOf(operationLog.getCreateTime().toString()));
        map.put("args",operationLog.getArgs().toString());
        kafkaTemplate.send("args",String.valueOf(operationLog.getArgs().toString()));
        map.put("level",operationLog.getLevel().toString());
        kafkaTemplate.send("level",String.valueOf(operationLog.getLevel().toString()));
        map.put("operationUnit",operationLog.getOperationUnit().toString());
        kafkaTemplate.send("operationUnit",String.valueOf(operationLog.getOperationUnit().toString()));
        map.put("method",operationLog.getMethod().toString());

        //kafka
        kafkaTemplate.send("method", String.valueOf(operationLog.getMethod().toString()));


        map.put("operationType",operationLog.getOperationType().toString());
        kafkaTemplate.send("operationType",String.valueOf(operationLog.getOperationType().toString()));
        //map.put("returnValue",operationLog.getReturnValue().toString());
        Document document=new Document(map);
        spit.insertOne(document);
        System.out.println("记录日志:" + operationLog.toString());
       // mongoClient.close();
//        operationLogService.insert(operationLog);
    }

    /**
     * 对当前登录用户和占位符处理
     * @param argNames 方法参数名称数组
     * @param args 方法参数数组
     * @param annotation 注解信息
     * @return 返回处理后的描述
     */
    private String getDetail(String[] argNames, Object[] args, log annotation){

        Map<Object, Object> map = new HashMap<>(4);
        for(int i = 0;i < argNames.length;i++){
            map.put(argNames[i],args[i]);
        }

        String detail = annotation.detail();
        try {
            detail = "'" + "#{currentUserName}" + "'=》" + annotation.detail();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                detail = detail.replace("{{" + k + "}}", JSON.toJSONString(v));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        System.out.println("进入方法前执行.....");

    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        System.out.println("方法的返回值 : " + ret);
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing("operationLog()")
    public void throwss(JoinPoint jp){
        System.out.println("方法异常时执行.....");
    }


    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");
    }

}