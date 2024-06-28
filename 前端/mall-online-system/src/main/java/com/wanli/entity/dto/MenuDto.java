package com.wanli.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto {
    private Long id;
    private String title;   //Menu类 name 菜单名称
    private String icon;
    private String path;
    private String name;    //Menu类 perms 权限编码
    private String component;

    //子菜单
    List<MenuDto> children = new ArrayList<>();
}
