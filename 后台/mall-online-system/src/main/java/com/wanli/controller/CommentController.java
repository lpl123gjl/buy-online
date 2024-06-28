package com.wanli.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.entity.Comment;
import com.wanli.entity.Account;
import com.wanli.service.AccountService;
import com.wanli.service.CommentService;
import com.wanli.service.BusinessService;
import com.wanli.service.GoodsService;
import com.wanli.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    // 处理评论表中的数据
    @Autowired
    private CommentService commentService;

    // 处理sys_account表中的数据
    @Autowired
    private AccountService accountService;

    // 处理sys_business
    @Autowired
    private BusinessService businessService;

    // 处理sys_goods
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result list(@RequestParam(name = "details", required = false) String details,
                       @RequestParam(name = "current", defaultValue = "1") int current,
                       @RequestParam(name = "size", defaultValue = "5") int size) {
        // 创建分页对象
        Page<Comment> page = new Page<>(current, size);
        // 创建查询条件包装
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        // 通过对评论进行添加模糊查询条件
        if (details != null && !details.isEmpty()) {
            wrapper.like("details", details);
        }
        // 执行分页查询
        Page<Comment> commentPage = commentService.page(page, wrapper);
        // 处理查询结果，评论的账户名、商家名和商品名
        List<Comment> comments = commentPage.getRecords().stream().map(comment -> {
            Account account = accountService.getById(comment.getAccountId());
            if (account != null) {
                comment.setAccountName(account.getAccountName());
            }
            String businessName = businessService.getById(comment.getBusinessId()).getBusinessName();
            String goodsName = goodsService.getById(comment.getGoodsId()).getGoodsName();
            comment.setBusinessName(businessName);
            comment.setGoodsName(goodsName);
            return comment;
        }).collect(Collectors.toList());
        // 设置处理后的评论列表
        commentPage.setRecords(comments);
        // 返回成功结果
        return Result.success(commentPage);
    }

    // 用户评论商家不能修改，不然容易刷分
    /*
    @PostMapping("/update")
    public Result update(@RequestBody Comment comment) {
        return commentService.updateById(comment) ? Result.success(null) : Result.fail("更新失败");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        return commentService.removeByIds(ids) ? Result.success(null) : Result.fail("删除失败");
    }
    */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Integer id) {
        // 获取评论
        Comment comment = commentService.getById(id);
        // 获取评论对应的账户名，从sys_account表中查询
        Account account = accountService.getById(comment.getAccountId());
        if (account != null) {
            comment.setAccountName(account.getAccountName());
        }
        // 根据business_id和goods_id从sys_account和sys_goods表中获取评论对应的商家名和商品名
        String businessName = businessService.getById(comment.getBusinessId()).getBusinessName();
        String goodsName = goodsService.getById(comment.getGoodsId()).getGoodsName();
        comment.setBusinessName(businessName);
        comment.setGoodsName(goodsName);
        // 返回成功结果
        return Result.success(comment);
    }
}
