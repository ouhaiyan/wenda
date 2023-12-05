package com.example.wenda.controller;

import com.example.wenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SettingController {
    @Autowired
    private WendaService wendaService;

    @RequestMapping(value = "/setting",method = {RequestMethod.GET})
    public String setting(HttpSession httpSession){
        return "setting OK!"+wendaService.getUser(String.valueOf(1));
    }
}
