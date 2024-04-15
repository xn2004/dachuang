package com.sky.service.impl;

import com.sky.entity.Post;
import com.sky.entity.User;
import com.sky.mapper.PostMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class PostServiceimpl implements PostService {

    @Autowired
    private PostMapper postMapper;




    public void publishPost(Post post) {
        post.setCreateTime(LocalDateTime.now());
        postMapper.insert(post);
    }

    /**
     * 获取某人发的所有帖子
     * @param userId
     * @return
     */
    public List<Post> getPostsByUserId(Long userId) {
        return postMapper.findByUserId(userId);
    }


    /**
     * 广场帖子
     * @return
     */
    public List<Post> getAllPosts() {
        return postMapper.getAllPosts();
    }


    @Override
    public void updateImage(Long userId, String filePath) {
        Post post = postMapper.findById(userId);
        // 如果用户存在，则更新用户头像路径
        if (post != null) {
            post.setPhotoPath(filePath); // 更新用户头像路径
            postMapper.update(post); // 将更新后的用户信息保存到数据库
            log.info("图片更新成功：{}", filePath);
        } else {
            log.error("用户不存在，无法更新图片");
        }
    }
}

