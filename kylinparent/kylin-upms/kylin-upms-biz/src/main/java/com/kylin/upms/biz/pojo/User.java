package com.kylin.upms.biz.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
@Document(indexName = "tensquare_article",type = "article")
public class User implements Serializable {
    @Id
    private String id;

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String name;

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
