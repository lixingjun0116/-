package com.kylin.upms.biz.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class RoleDto extends Model<RoleDto> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    /**
     * 角色名称
     */
    @TableField("nameZh")
    private String nameZh;

    private Integer[] mid;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
