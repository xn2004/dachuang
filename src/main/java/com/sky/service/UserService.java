package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    //微信登录
    User wxLogin(UserLoginDTO userLoginDTO);
    //根据手机号匹配判断是否认证
    User authenticate(String idNumber, String bookId);
    String updateUserAuthentication(String idNumber, String bookId);
    //更新认证状态
   // String updateUserAuthentication(String phone);

    //插入数据
    void createUser(User user);

    //修改用户信息
    void updateUser(User user);

    //删除用户信息
    void deleteUserById(Long userId);

    User findById(Long id);

    void updateUserAvatar(Long userId,String filePath);


//    boolean mapCheckIn(Long taskId, Double latitude, Double longitude,Long userId);
}
