package com.example.wenda.service;

import org.springframework.stereotype.Service;

@Service
public class WendaService {
    public String getUser(String userId){
        return "hellow："+userId;
    }
}
