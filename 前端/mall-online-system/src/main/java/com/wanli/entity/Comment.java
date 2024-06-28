package com.wanli.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wanli.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Byterain
 * @since 2024-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_comment")
public class Comment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "comm_Id", type = IdType.AUTO)
    private Integer commId;

    @TableField("account_id")
    private String accountId;

    @TableField("score")
    private Integer score;

    @TableField("details")
    private String details;

    @TableField("business_id")
    private Long businessId;

    @TableField("goods_id")
    private Integer goodsId;

    @TableField("comm_image")
    private String commImage;


     @TableField(exist = false)
     private LocalDateTime updated;

    @TableField(exist = false)
    private Integer statu;

    @TableField(exist = false)
    private String accountName; // 临时字段，不会映射到数据库
    @TableField(exist = false)
    private String goodsName;
    @TableField(exist = false)
    private String businessName;
}
