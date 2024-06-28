package com.wanli.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.entity.Category;
import com.wanli.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hzy
 * @since 2024-06-24
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/delete")
    public Result delete(@RequestBody Long categoryIds){
        categoryService.removeByIds(Arrays.asList(categoryIds));
        return Result.success("删除成功");

    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        //查询商家id对应的信息
        Category byId = categoryService.getById(id);
        return Result.success(byId);
    }

    @GetMapping("/list")
    public Result list(String name){
        //Page<Role> page= roleService.page(getPage(),new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        Page<Category> page=categoryService.page(getPage(),new QueryWrapper<Category>().like(StrUtil.isNotBlank(name),"category_name",name));
        return Result.success(page);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Category category){
        category.setCreated(LocalDateTime.now());
        categoryService.save(category);
        return Result.success("分类信息录入成功");
    }
    @PostMapping("/update")
    public Result update(@RequestBody Category category){
        category.setUpdated(LocalDateTime.now());
        categoryService.updateById(category);
        return Result.success("分类信息更新成功");
    }
}
