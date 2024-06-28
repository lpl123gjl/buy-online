package com.wanli.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wanli.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzy
 * @since 2024-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_category")
public class Category extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @TableField("category_name")
    private String categoryName;

    @TableField("category_cover")
    private String categoryCover;


}
