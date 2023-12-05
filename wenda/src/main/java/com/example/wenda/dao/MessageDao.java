package com.example.wenda.dao;

import com.example.wenda.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDao {
    int addUser(User user);

}
