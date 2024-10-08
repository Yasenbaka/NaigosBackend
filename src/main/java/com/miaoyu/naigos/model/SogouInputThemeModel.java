/*
* 搜狗输入法的皮肤模型*/
package com.miaoyu.naigos.model;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.IOException;
import java.util.List;

public class SogouInputThemeModel {
    private String name;
    private String url;
    private String introduce;
    private String header_image;
    private String details_image;
    private int cost;
    private String eject_image;
    private String theme_id;

    public String getDetails_image() {
        return details_image;
    }

    public void setDetails_image(String details_image) {
        this.details_image = details_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

//    public String getDetails_image() {
//        return details_image;
//    }

//    public void setDetails_image(String details_image) {
//        this.details_image = details_image;
//    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getEject_image() {
        return eject_image;
    }

    public void setEject_image(String eject_image) {
        this.eject_image = eject_image;
    }

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }
}
