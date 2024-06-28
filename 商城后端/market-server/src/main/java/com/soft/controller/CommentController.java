package com.soft.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.common.Result;
import com.soft.entity.Comment;
import com.soft.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.soft.common.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2024-06-25
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/list/{goodsId}")
    //通过goods_id查询评论
    public Result list(@PathVariable Long goodsId) {
        return Result.success(commentService.list(new QueryWrapper<Comment>().eq("goods_id",goodsId)));
    }
}
