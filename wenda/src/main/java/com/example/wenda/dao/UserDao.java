package com.example.wenda.dao;

import com.example.wenda.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    //简单的直接用注解，复杂一线的最好使用xml文件

    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,head_url,created_date";
    String SELECT_FIELDS = "id, "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{headUrl},#{createdDate})"})
    int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
    User selectById(int id);

}
