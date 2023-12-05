package com.example.wenda.interceptor;


import com.example.wenda.dao.LoginTicketMapper;
import com.example.wenda.dao.UserMapper;
import com.example.wenda.domain.HostHolder;
import com.example.wenda.domain.LoginTicket;
import com.example.wenda.domain.User;
import com.example.wenda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


//@Component
@Slf4j
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("token拦截开始");
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = (LoginTicket) loginTicketMapper.queryTicket(ticket);
            if (loginTicket == null || loginTicket.getExpiredDate().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            User user = userService.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
