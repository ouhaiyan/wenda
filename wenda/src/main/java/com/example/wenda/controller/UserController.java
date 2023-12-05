package com.example.wenda.controller;


import com.example.wenda.dao.LoginTicketMapper;
import com.example.wenda.dao.UserMapper;
import com.example.wenda.domain.HostHolder;
import com.example.wenda.domain.ViewObject;
import com.example.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(path = "/regist", method = RequestMethod.POST)
    public String regist(String username, String password, HttpServletResponse response) {
        ViewObject viewObject = userService.regist(username, password);
        if (viewObject.get("error") != null) {
            return viewObject.get("error").toString();
        }
        String ticket = viewObject.get("data").toString();
        Cookie cookie = new Cookie("ticket", ticket);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "注册成功";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpServletResponse response) {
        ViewObject viewObject = userService.login(username, password);
        if (viewObject.get("error") != null) {
            return viewObject.get("error").toString();
        }

        String ticket = viewObject.get("data").toString();
        Cookie cookie = new Cookie("ticket", ticket);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "登陆成功";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout() {
        ViewObject viewObject = userService.logout();
        if (200 == (Integer) viewObject.get("code")) {
            return viewObject.get("succes").toString();
        }
        return "退出失败";
    }

    @RequestMapping(path = {"/relogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }
}
