package com.sky.service;

import com.sky.entity.Post;

import java.util.List;

public interface PostService {
    void updateImage(Long userId, String filePath);


    void publishPost(Post post);


    List<Post> getPostsByUserId(Long userId);

    List<Post> getAllPosts();
}
