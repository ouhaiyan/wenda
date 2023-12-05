package com.example.wenda.util;

import com.alibaba.fastjson.JSONObject;

public class WendaUtil {
    public static String getJSONString(int code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject.toJSONString();
    }
}
