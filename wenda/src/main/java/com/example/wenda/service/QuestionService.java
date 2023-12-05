package com.example.wenda.service;

import com.example.wenda.dao.QuestionMapper;
import com.example.wenda.domain.HostHolder;
import com.example.wenda.domain.Question;
import com.example.wenda.domain.User;
import com.example.wenda.domain.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private SensitiveService sensitiveService;

    private static int ANONY_USERID=0;

    public List<Question> selectById(int userId) {
        return questionMapper.selectById(userId);
    }

    public List<ViewObject> getQuestion(int userId) {
        List<Question> questionsList = selectById(userId);
        List<ViewObject> viewObjects = new ArrayList<ViewObject>();
        for (Question question : questionsList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.selectById(question.getUserId()));
            viewObjects.add(vo);
        }
        return viewObjects;
    }

    public ViewObject addQuestion(String title, String content) {
        //敏感词过滤
        title = sensitiveService.filter(title);
        content = sensitiveService.filter(content);

        Question question = new Question();

        User user = hostHolder.getUser();
        if (null != user) {
            question.setUserId(user.getId());
        } else {
            question.setUserId(ANONY_USERID);
        }

        question.setContent(HtmlUtils.htmlEscape(content));

        question.setTitle(HtmlUtils.htmlEscape(title));

        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        question.setCreatedDate(date);

        questionMapper.addQuestion(question);

        ViewObject viewObject = new ViewObject();
        viewObject.set("succes",question);
        viewObject.set("code",200);
        return viewObject;
    }
}
