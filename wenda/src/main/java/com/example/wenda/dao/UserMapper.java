package com.example.wenda.dao;

import com.example.wenda.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    User insertUser(User user);

    //    User regist(@Param("username") String name, @Param("password") String password);
    void  regist(User user);

    Boolean login(@Param("username") String username,@Param("password") String password);

    User selectUserByName(@Param("username") String username);
}
