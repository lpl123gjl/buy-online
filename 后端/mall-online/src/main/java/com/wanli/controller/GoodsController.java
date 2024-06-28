package com.wanli.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.config.Const;
import com.wanli.entity.Goods;
import com.wanli.service.GoodsService;
import com.wanli.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    // 自动注入 GoodsService，用于处理商品相关的业务逻辑
    @Autowired
    private GoodsService goodsService;

    //    获取商家列表，使用分页显示
    @GetMapping("/list")
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "current", defaultValue = "1") Integer current,
                       @RequestParam(value = "size", defaultValue = "10") Integer size){
        // 创建分页对象
        Page<Goods> page = new Page<>(current, size);
        // 创建查询条件包装器
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        // 根据goodsName进行模糊查询
        if (name != null && !name.isEmpty()) {
            wrapper.lambda().like(Goods::getGoodsName, name);
        }
        // 执行分页查询
        Page<Goods> goodsPage = goodsService.page(page, wrapper);
        // 返回成功结果
        return Result.success(goodsPage);
    }

    //    根据id获取商品
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        // 根据ID获取商品
        Goods goods = goodsService.getById(id);
        // 返回成功结果或失败结果
        return goods != null ? Result.success(goods) : Result.fail("商品不存在");
    }

    //    新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        // 设置创建时间和更新时间为当前时间
        goods.setCreated(LocalDateTime.now());
        goods.setUpdated(LocalDateTime.now());
        // 保存商品
        boolean success = goodsService.save(goods);
        // 返回成功结果或失败结果
        return success ? Result.success("商品新增成功") : Result.fail("商品新增失败");
    }

    //    更改
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        // 设置更新时间为当前时间
        goods.setUpdated(LocalDateTime.now());
        // 更新商品
        boolean success = goodsService.updateById(goods);
        // 返回成功结果或失败结果
        return success ? Result.success("商品更新成功") : Result.fail("商品更新失败");
    }

    //    删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        // 删除商品
        boolean success = goodsService.removeByIds(Arrays.asList(ids));
        // 返回的结果
        return success ? Result.success("商品删除成功") : Result.fail("商品删除失败");
    }
}
