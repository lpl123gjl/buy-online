package com.wanli.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.wanli.common.BaseController;
import com.wanli.common.Result;
import com.wanli.config.Const;
import com.wanli.entity.User;
import com.wanli.entity.UserBusiness;
import com.wanli.service.UserBusinessService;
import com.wanli.service.UserService;
import com.wanli.util.OSSUtil;
import com.wanli.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class IndexController extends BaseController {
    @Autowired
    private Producer producer;
    @Autowired
    private UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private OSSUtil ossUtil;
    @Autowired
    private UserBusinessService userBusinessService;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {

        String code = producer.createText();
        String key = UUID.randomUUID().toString();

        redisUtil.hset(Const.CAPTCHA,key,code,60);

        BufferedImage image = producer.createImage(code);
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);

        String str = "data:image/jpg;base64,";
        String base64Image = str + encoder.encode(out.toByteArray());

        log.info("验证码--{}--{}",key,code);
        return Result.success(MapUtil.builder().put("key", key).put("captcha", base64Image).build());
    }
    @GetMapping("/userinfo")
    public Result info(Principal principal){
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<UserBusiness> business = userBusinessService.list(new QueryWrapper<UserBusiness>().eq("user_id",user.getId()));
        user.setBusiness(business);
        return Result.success(user);
    }
    @PostMapping("/updatePass")
    public Result updatePass(Principal principal){
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        String password = request.getParameter("password");
        String newPassword =  request.getParameter("newPassword");
        if(bCryptPasswordEncoder.matches(password,user.getPassword())){
            if(!newPassword.equals(password)){
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                QueryWrapper<User> qw = new QueryWrapper<>();
                qw.eq("id",user.getId());
                userService.update(user,qw);
            }else{
                return Result.fail("新密码不能与旧密码相同");
            }
        }else{
            return Result.fail("旧密码不正确");
        }
    return Result.success(20000,"修改成功",null);
    }
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file){
        //将上传file保存OSS
        String imgPath = ossUtil.uploadOneFile(file);
        if(imgPath ==null){
            return Result.fail("文件上传失败");
        }
        return Result.success(imgPath);
    }
}
