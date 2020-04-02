package com.kylin.upms.biz.dao;

import com.kylin.upms.biz.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LogDao extends MongoRepository<Log,String> {


}
