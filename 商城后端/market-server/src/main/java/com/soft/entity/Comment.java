package com.soft.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Byterain
 * @since 2024-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "comm_Id", type = IdType.AUTO)
    private Integer commId;

    @TableField("score")
    private Integer score;

    @TableField("details")
    private String details;

    @TableField("business_id")
    private Long businessId;

    @TableField("goods_id")
    private Integer goodsId;

    @TableField("created")
    private LocalDateTime created;

    @TableField("comm_image")
    private String commImage;

    @TableField("account_id")
    private String accountId;


}
