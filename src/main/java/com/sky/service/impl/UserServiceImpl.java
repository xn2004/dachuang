package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;




    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空,如果为空表示登录失败,抛出业务异常
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        //如果是新用户,自动完成注册
        if (user == null){
            user =  User.builder()
                    .openid(openid)
                    .phone(userLoginDTO.getPhone())
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回这个用户对象
        return user;
    }

    /**
     * 调用微信接口服务,获得当前微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口服务,获得当前微信用户的openid
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN,map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    /**
     * 更新认证状态
     * 当用户不存在时，返回"认证失败"；当用户存在但已认证时，返回"用户已认证"；
     * 当用户存在且未认证时，将认证状态更新为认证成功，
     * 并返回"未认证,系统已查询到你的信息,现已认证成功"。
     * @param
     * @return
     */
    @Override
    public User authenticate(String idNumber, String bookId) {
        // 根据学生证号和书籍ID从数据库中查询用户信息
        return userMapper.findByStudentIdAndBookId(idNumber, bookId);
    }

    @Override
    public String updateUserAuthentication(String idNumber, String bookId) {
        // 根据学生证号和书籍ID从数据库中查询用户信息
        User user = userMapper.findByStudentIdAndBookId(idNumber, bookId);

        // 如果用户存在且尚未认证，则将认证状态更新为认证成功
        if (user != null && !user.isAuthenticated()) {
            user.setAuthenticated(true);
            userMapper.update(user);
            return "未认证，但系统已查询到您的信息，现已认证成功";
        } else if (user != null && user.isAuthenticated()) {
            return "用户已认证";
        } else {
            return "认证失败";
        }
    }




    //插入数据
    @Override
    public void createUser(User user) {
        userMapper.insert(user);
    }

    //修改信息
    @Override
    public void updateUser(User user) {
        // 查询原有的用户信息
        User existingUser = userMapper.findById(user.getId());
        if (existingUser == null) {
            try {
                throw new NotFoundException("User not found");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        // 将传入的用户信息中非空的字段覆盖原有的用户信息
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getSex() != null) {
            existingUser.setSex(user.getSex());
        }
        if (user.getAvatar() != null) {
            existingUser.setSex(user.getAvatar());
        }
        if (user.getClazz() != null) {
            existingUser.setSex(user.getClazz());
        }
        if (user.getIdNumber() != null) {
            existingUser.setSex(user.getIdNumber());
        }
        if (user.getStudentId() != null) {
            existingUser.setSex(user.getStudentId());
        }
        if (user.getSchool() != null) {
            existingUser.setSex(user.getSchool());
        }
        if (user.getMajor() != null) {
            existingUser.setSex(user.getMajor());
        }
        if (user.getBookId() != null) {
            existingUser.setSex(user.getBookId());
        }



        // 添加其他字段的更新逻辑

        // 更新用户信息
        userMapper.update(existingUser);
    }

    //删除信息
    @Override
    public void deleteUserById(Long userId) {
        userMapper.deleteById(userId);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }


    @Override
    public void updateUserAvatar(Long userId,String avatarPath) {
        // 根据用户ID查询用户信息
        User user = userMapper.findById(userId);

        // 如果用户存在，则更新用户头像路径
        if (user != null) {
            user.setAvatar(avatarPath); // 更新用户头像路径
            userMapper.update(user); // 将更新后的用户信息保存到数据库
            log.info("用户头像路径更新成功：{}", avatarPath);
        } else {
            log.error("用户不存在，无法更新头像路径");
        }
    }
}
