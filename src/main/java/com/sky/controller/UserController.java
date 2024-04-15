package com.sky.controller;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "*",methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@RestController
@RequestMapping("/user")
@Api(tags = "c端用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录:{}",userLoginDTO.getCode());

        //微信登录
        User user = userService.wxLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }


    @GetMapping("/authenticate")
    @ApiOperation("是否认证")
    public String authenticate(@RequestParam("idNumber") String idNumber, @RequestParam("bookId") String bookId) {
        // 调用 UserService 的方法根据学号和书籍ID进行认证
        User authenticatedUser = userService.authenticate(idNumber, bookId);

        if (authenticatedUser != null) {
            if (!authenticatedUser.isAuthenticated()) {
                // 用户存在且未认证，更新认证状态为认证成功
                userService.updateUserAuthentication(idNumber, bookId);
                return "未认证，但系统已查询到您的信息，现已认证成功";
            } else {
                // 用户已认证
                return "用户已认证";
            }
        } else {
            // 用户不存在
            return "认证失败,未查询到信息";
        }
    }




    @PostMapping("/create")
    @ApiOperation("新增用户信息")
    public Result<String> createUser(@RequestBody User user) {
        userService.createUser(user);
        return Result.success("用户信息创建成功");
    }

    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<String> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("用户信息更新成功");
    }

    @DeleteMapping("delete/{userId}")
    @ApiOperation("根据用户ID删除用户信息")
    public Result<String> deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return Result.success("用户信息删除成功");
    }


    @GetMapping("/info")
    public User getUserInfo(@RequestParam("userId") Long userId) {
        // 根据用户ID从数据库中查询用户信息
        return userService.findById(userId);
    }


}
