/*
* 获取application中的JWT配置信息*/
package com.miaoyu.naigos.service;

import com.miaoyu.naigos.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String printAppConfig() {
        System.out.println("jwt key: " + jwtConfig.getKey());
        return null;
    }
    public String getKey(){
        return jwtConfig.getKey();
    }
}
