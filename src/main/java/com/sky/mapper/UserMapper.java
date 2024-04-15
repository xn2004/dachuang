package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.xml.soap.SAAJResult;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);


    @Select("select * from sky_take_out.user where id = #{id}")
    User getById(Long userId);

    /**
     * 根据动态条件统计用户数量
     * @return
     */
    Integer countByMap(Map map);

    @Select("select *from sky_take_out.user where phone = #{phone}")
    User findByPhone(String phone);

    /**
     * 保存信息
     */


    @Insert("INSERT INTO sky_take_out.user (id,openid, name, phone, sex, id_number, avatar, create_time, student_id, school, major, clazz, authenticated,book_id) " +
            "VALUES (#{id},#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime}, #{studentId}, #{school}, #{major}, #{clazz}, #{authenticated},#{bookId})")
    void save(User user);

    /**
     * 更新修改信息
     * @param user
     */
    void update(User user);

    /**
     * 删除
     * @param userId
     */
    void deleteById(Long userId);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    User findById(Long id);

    @Select("select * from sky_take_out.user where id_number = #{idNumber} and book_id = #{bookId}")
    User findByStudentIdAndBookId(@Param("idNumber") String idNumber, @Param("bookId") String bookId);

}

