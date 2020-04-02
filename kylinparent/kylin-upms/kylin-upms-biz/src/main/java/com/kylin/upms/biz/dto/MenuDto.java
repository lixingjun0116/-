package com.kylin.upms.biz.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.kylin.upms.biz.entity.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxj
 * @since 2019-09-15
 */
@Data
@Accessors(chain = true)
public class MenuDto extends Model<MenuDto> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    private String path;

    private String component;

    private String name;

    @TableField("iconCls")
    private String iconCls;

    @TableField("keepAlive")
    private Integer keepAlive;

    @TableField("requireAuth")
    private Integer requireAuth;

    @TableField("parentId")
    private Integer parentId;

    private Integer enabled;

    private Date createTime;

    private  List<Role> roleList;


    private List<MenuDto> menuList;

    private List<MenuDto> menus;
    private Integer[] mid;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
