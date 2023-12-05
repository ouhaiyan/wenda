package com.example.wenda.controller;

import com.example.wenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

//@RestController
public class IndexController {
    @Autowired
    private WendaService wendaService;

    @RequestMapping(path = {"/", "index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {
        return "hell now  " + wendaService.getUser("1")+httpSession.getAttribute("msg");
    }

//    @RequestMapping(path = "/index/{}", method = {RequestMethod.POST})
//    public String index(HttpSession httpSession) {
//        return wendaService.getUser(String.valueOf(2)) + "hello：" + httpSession.getAttribute("200");
//    }

    @RequestMapping(value = "/redirect/{code}", method = {RequestMethod.GET})
    @ResponseBody
    public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession) {
        httpSession.setAttribute("msg", "hhhhhhhhh");
        RedirectView redirectView = new RedirectView("/",true);
        if (code==301){
             redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        }
        return redirectView;

    }


    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}
