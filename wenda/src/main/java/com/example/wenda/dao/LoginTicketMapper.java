package com.example.wenda.dao;

import com.example.wenda.domain.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginTicketMapper {
    int addTicket(LoginTicket loginTicket);

    List<LoginTicket> selectTicket();

    List<LoginTicket> queryTicket(@Param("ticket")String ticket);
    LoginTicket queryTicketByUserId(@Param("userId")int userId);

    void updateLoginTicket(LoginTicket loginTicket);

}
