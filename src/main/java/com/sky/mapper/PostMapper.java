package com.sky.mapper;

import com.sky.entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper {


    @Insert("INSERT INTO sky_take_out.post (title, content, user_id, create_time, photo_path) " +
            "VALUES (#{title}, #{content}, #{userId}, #{createTime}, #{photoPath})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Post post);

    @Select("SELECT * FROM sky_take_out.post WHERE user_id = #{userId}")
    List<Post> findByUserId(Long userId);

    @Select("SELECT sky_take_out.post.id, post.title, post.content, post.create_time,post.photo_path, user.name " +
            "FROM sky_take_out.post " +
            "JOIN sky_take_out.user ON post.user_id = user.id")
    List<Post> getAllPosts();


    Post findById(Long userId);

    void update(Post post);
}
