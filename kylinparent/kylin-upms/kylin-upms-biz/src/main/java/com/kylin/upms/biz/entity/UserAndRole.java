package com.kylin.upms.biz.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Data
@Document(indexName = "tensquare_article",type = "article")
public class UserAndRole implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * hrID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String name;

    /**
     * 手机号码
     */
    @Field(index = true,analyzer = "ik_max_word")
    private String phone;

    /**
     * 住宅电话
     */
    @Field(index = true,analyzer = "ik_max_word")
    private String telephone;

    /**
     * 联系地址
     */
    @Field(index = true,analyzer = "ik_max_word")
    private String address;

    private Integer enabled;

    /**
     * 用户名
     */
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String username;

    /**
     * 密码
     */
    @Field(index = true,analyzer = "ik_max_word")
    private String password;

    @Field(index = true,analyzer = "ik_max_word")
    private String userface;

    @Field(index = true,analyzer = "ik_max_word")
    private String remark;


    private String rids;
    private String rnameZhs;
    private String rnames;


}
