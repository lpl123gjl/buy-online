package com.wanli.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.List;

import com.wanli.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@TableName("sys_user")
public class User extends BaseEntity {
    //序列化和反序列化需要实体类版本号
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("avatar")
    private String avatar;

    @TableField("email")
    private String email;

    @TableField("tel")
    private String tel;

    @TableField("last_login")
    private LocalDateTime lastLogin;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<UserBusiness> business;
}
