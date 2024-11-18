package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * Get user by openid
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * Insert user
     * @param user
     */
    void insert(User user);

    /**
     * Get user by id
     * @param id
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);

    /**
     * count by time range
     * @param map
     */
    Integer countByMap(Map map);
}
