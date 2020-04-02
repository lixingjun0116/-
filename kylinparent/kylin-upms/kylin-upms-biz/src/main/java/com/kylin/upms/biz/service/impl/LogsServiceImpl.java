package com.kylin.upms.biz.service.impl;

import com.kylin.upms.biz.dao.LogDao;
import com.kylin.upms.biz.entity.Log;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.List;
@Service
public class LogsServiceImpl {

    @Autowired
    LogDao logDao;


    public Page<Log> finall(Pageable pageable)
    {
        return  logDao.findAll(pageable);
    }

}
