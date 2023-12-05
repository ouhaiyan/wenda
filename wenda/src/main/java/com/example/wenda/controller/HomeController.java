package com.example.wenda.controller;

import com.example.wenda.domain.Question;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
//@Controller
public class HomeController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model, HttpSession httpSession) {

//        model.addAttribute("vos", questionService.getQuestion(1));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public List<Question> userIndex(Model model, @PathVariable("userId") int userId) {
//        Model vos = model.addAttribute("vos", questionService.getQuestion(userId));

        return questionService.selectById(userId);
    }
}
