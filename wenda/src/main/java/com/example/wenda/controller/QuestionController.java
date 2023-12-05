package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.domain.ViewObject;
import com.example.wenda.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        ViewObject viewObject = questionService.addQuestion(title, content);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("data",viewObject.get("succes"));
        return jsonObject.toJSONString();
    }
}
