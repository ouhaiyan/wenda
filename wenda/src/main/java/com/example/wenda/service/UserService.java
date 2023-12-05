package com.example.wenda.service;

import com.example.wenda.dao.LoginTicketMapper;
import com.example.wenda.dao.UserDao;
import com.example.wenda.dao.UserMapper;
import com.example.wenda.domain.HostHolder;
import com.example.wenda.domain.LoginTicket;
import com.example.wenda.domain.User;
import com.example.wenda.domain.ViewObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.*;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private HostHolder hostHolder;

    public User selectById(int userId) {
        return userDao.selectById(userId);
    }

    public ViewObject regist(String username, String password) {
        ViewObject viewObject = new ViewObject();
        if (StringUtils.isBlank(username)) {
            viewObject.set("error","用户名不能为空");
            viewObject.set("code", 500);
            return viewObject;
        }

        if (StringUtils.isBlank(password)) {
            viewObject.set("error","密码不能为空");
            viewObject.set("code", 500);
            return viewObject;
        }

        User user = userMapper.selectUserByName(username);

        if (user != null) {
            viewObject.set("error","用户已存在");
            viewObject.set("code", 500);
            return viewObject;
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        user.setCreatedDate(date);
        userMapper.regist(user);
//            Integer id = user.getId();//此时user的id是空的

        user = userMapper.selectUserByName(username);
        Integer id = user.getId();
        String tocket = addLoginTocket(id);
        viewObject.set("data",tocket);
        viewObject.set("code", 200);
        return viewObject;

    }

    public ViewObject login(String username, String password) {
        ViewObject viewObject = new ViewObject();
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            viewObject.set("error", "用户名不存在");
            viewObject.set("code", 500);
            return viewObject;
        }
        if (!(password.equals(user.getPassword()))) {
            viewObject.set("error","密码不正确");
            viewObject.set("code", 500);
            return viewObject;
        }
        hostHolder.setUser(user);
        LoginTicket loginTicket = loginTicketMapper.queryTicketByUserId(user.getId());
        loginTicket.setStatus(0);

        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 100 * 100); // 添加100天
        Date newDate = calendar.getTime();
        loginTicket.setExpiredDate(newDate);

        loginTicketMapper.updateLoginTicket(loginTicket);

        viewObject.set("data", loginTicket.getTicket());
        viewObject.set("code", 200);
        return viewObject;
    }

    //token
    public String addLoginTocket(int userId) {

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);

        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 100 * 100); // 添加100天
        Date newDate = calendar.getTime();
        loginTicket.setExpiredDate(newDate);

        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));

        loginTicketMapper.addTicket(loginTicket);
        String ticket = loginTicket.getTicket();
        return ticket;
    }

    public ViewObject logout(){
        ViewObject viewObject = new ViewObject();
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(1);

        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        loginTicket.setExpiredDate(date);

        loginTicketMapper.updateLoginTicket(loginTicket);
        hostHolder.clear();
        viewObject.set("succes","退出登录,token失效");
        viewObject.set("code",200);
        return viewObject;
    }

    public Map<String, List<LoginTicket>> selectTicket() {
        List<LoginTicket> loginTickets = loginTicketMapper.selectTicket();
        Map<String, List<LoginTicket>> map = new HashMap<>();
        map.put("loginTickets", loginTickets);
        return map;
    }
}
