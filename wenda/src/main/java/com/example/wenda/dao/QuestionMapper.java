package com.example.wenda.dao;

import com.example.wenda.domain.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,head_url,create_time";
    String SELECT_FIELDS = "id, "+INSERT_FIELDS;

    List<Question> selectById(@Param("userId") int userId);
    void addQuestion(Question question);

}
