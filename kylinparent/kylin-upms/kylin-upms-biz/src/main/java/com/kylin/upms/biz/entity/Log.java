package com.kylin.upms.biz.entity;

import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "user")
public class Log implements Serializable {

    @Id
    private String _id;

    private String args;

    private String operationType;

    private String method;

    private String createTime;
}
